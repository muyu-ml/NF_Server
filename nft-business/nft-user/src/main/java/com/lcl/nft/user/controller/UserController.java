package com.lcl.nft.user.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.lcl.nft.file.FileService;
import com.lcl.nft.user.domain.entity.User;
import com.lcl.nft.user.domain.entity.convertor.UserConvertor;
import com.lcl.nft.user.domain.service.UserService;
import com.lcl.nft.user.infrastructure.exception.UserException;
import com.lcl.nft.user.param.UserAuthParam;
import com.lcl.nft.user.param.UserModifyParam;
import com.lcl.nft.web.vo.Result;
import com.lcl.nft.api.chain.request.ChainProcessRequest;
import com.lcl.nft.api.chain.response.ChainProcessResponse;
import com.lcl.nft.api.chain.response.data.ChainCreateData;
import com.lcl.nft.api.chain.service.ChainFacadeService;
import com.lcl.nft.api.user.request.UserActiveRequest;
import com.lcl.nft.api.user.request.UserAuthRequest;
import com.lcl.nft.api.user.request.UserModifyRequest;
import com.lcl.nft.api.user.response.UserOperatorResponse;
import com.lcl.nft.api.user.response.data.UserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import static com.lcl.nft.api.common.constant.CommonConstant.APP_NAME_UPPER;
import static com.lcl.nft.api.common.constant.CommonConstant.SEPARATOR;
import static com.lcl.nft.user.infrastructure.exception.UserErrorCode.*;

/**
 * 用户信息
 * @Author conglongli
 * @date 2025/1/20 14:28
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private ChainFacadeService chainFacadeService;

    @GetMapping("/getUserInfo")
    public Result<UserInfo> getUserInfo() {
        String userId = (String) StpUtil.getLoginId();
        User user = userService.findById(Long.valueOf(userId));

        if (user == null) {
            throw new UserException(USER_NOT_EXIST);
        }
        return Result.success(UserConvertor.INSTANCE.mapToVo(user));
    }

    @PostMapping("/modifyNickName")
    public Result<Boolean> modifyNickName(@Valid @RequestBody UserModifyParam userModifyParam) {
        String userId = (String) StpUtil.getLoginId();

        //修改信息
        UserModifyRequest userModifyRequest = new UserModifyRequest();
        userModifyRequest.setUserId(Long.valueOf(userId));
        userModifyRequest.setNickName(userModifyParam.getNickName());

        Boolean registerResult = userService.modify(userModifyRequest).getSuccess();
        return Result.success(registerResult);
    }

    @PostMapping("/modifyPassword")
    public Result<Boolean> modifyPassword(@Valid @RequestBody UserModifyParam userModifyParam) {
        //查询用户信息
        String userId = (String) StpUtil.getLoginId();
        User user = userService.findById(Long.valueOf(userId));

        if (user == null) {
            throw new UserException(USER_NOT_EXIST);
        }
        if (!StringUtils.equals(user.getPasswordHash(), DigestUtil.md5Hex(userModifyParam.getOldPassword()))) {
            throw new UserException(USER_PASSWD_CHECK_FAIL);
        }
        //修改信息
        UserModifyRequest userModifyRequest = new UserModifyRequest();
        userModifyRequest.setUserId(Long.valueOf(userId));
        userModifyRequest.setPassword(userModifyParam.getNewPassword());
        Boolean registerResult = userService.modify(userModifyRequest).getSuccess();
        return Result.success(registerResult);
    }

    @PostMapping("/modifyProfilePhoto")
    public Result<String> modifyProfilePhoto(@RequestParam("file_data") MultipartFile file) throws Exception {
        String userId = (String) StpUtil.getLoginId();
        String prefix = "https://nfturbo-file.oss-cn-hangzhou.aliyuncs.com/";

        if (null == file) {
            throw new UserException(USER_UPLOAD_PICTURE_FAIL);
        }
        String filename = file.getOriginalFilename();
        InputStream fileStream = file.getInputStream();
        String path = "profile/" + userId + "/" + filename;
        var res = fileService.upload(path, fileStream);
        if (!res) {
            throw new UserException(USER_UPLOAD_PICTURE_FAIL);
        }
        //修改信息
        UserModifyRequest userModifyRequest = new UserModifyRequest();
        userModifyRequest.setUserId(Long.valueOf(userId));
        userModifyRequest.setProfilePhotoUrl(prefix + path);
        Boolean registerResult = userService.modify(userModifyRequest).getSuccess();
        if (!registerResult) {
            throw new UserException(USER_UPLOAD_PICTURE_FAIL);
        }
        return Result.success(prefix + path);
    }

    @PostMapping("/auth")
    public Result<Boolean> auth(@Valid @RequestBody UserAuthParam userAuthParam) {
        String userId = (String) StpUtil.getLoginId();

        //实名认证
        UserAuthRequest userAuthRequest = new UserAuthRequest();
        userAuthRequest.setUserId(Long.valueOf(userId));
        userAuthRequest.setRealName(userAuthParam.getRealName());
        userAuthRequest.setIdCard(userAuthParam.getIdCard());
        UserOperatorResponse authResult = userService.auth(userAuthRequest);
        //实名认证成功，需要进行上链操作
        if (authResult.getSuccess()) {
            ChainProcessRequest chainCreateRequest = new ChainProcessRequest();
            chainCreateRequest.setUserId(userId);
            String identifier = APP_NAME_UPPER + SEPARATOR + authResult.getUser().getUserRole() + SEPARATOR + authResult.getUser().getUserId();
            chainCreateRequest.setIdentifier(identifier);
            ChainProcessResponse<ChainCreateData> chainProcessResponse = chainFacadeService.createAddr(
                    chainCreateRequest);
            if (chainProcessResponse.getSuccess()) {
                //激活账户
                ChainCreateData chainCreateData = chainProcessResponse.getData();
                UserActiveRequest userActiveRequest = new UserActiveRequest();
                userActiveRequest.setUserId(Long.valueOf(userId));
                userActiveRequest.setBlockChainUrl(chainCreateData.getAccount());
                userActiveRequest.setBlockChainPlatform(chainCreateData.getPlatform());
                UserOperatorResponse activeResponse = userService.active(userActiveRequest);
                if (activeResponse.getSuccess()) {
                    refreshUserInSession(userId);
                    return Result.success(true);
                }
                return Result.error(activeResponse.getResponseCode(), activeResponse.getResponseMessage());
            } else {
                throw new UserException(USER_CREATE_CHAIN_FAIL);
            }
        }
        return Result.error(authResult.getResponseCode(), authResult.getResponseMessage());
    }

    private void refreshUserInSession(String userId) {
        User user = userService.getById(userId);
        UserInfo userInfo = UserConvertor.INSTANCE.mapToVo(user);
        StpUtil.getSession().set(userInfo.getUserId().toString(), userInfo);
    }
}

