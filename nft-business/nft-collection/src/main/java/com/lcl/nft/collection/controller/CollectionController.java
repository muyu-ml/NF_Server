package com.lcl.nft.collection.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.lcl.nft.api.collection.model.CollectionVO;
import com.lcl.nft.api.collection.model.HeldCollectionVO;
import com.lcl.nft.api.collection.request.CollectionPageQueryRequest;
import com.lcl.nft.api.collection.request.HeldCollectionPageQueryRequest;
import com.lcl.nft.api.collection.service.CollectionFacadeService;
import com.lcl.nft.base.response.PageResponse;
import com.lcl.nft.base.response.SingleResponse;
import com.lcl.nft.collection.domain.service.impl.redis.CollectionInventoryRedisService;
import com.lcl.nft.web.util.MultiResultConvertor;
import com.lcl.nft.web.vo.MultiResult;
import com.lcl.nft.web.vo.Result;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author conglongli
 * @date 2025/3/3 21:52
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("collection")
public class CollectionController {

    @Autowired
    private CollectionFacadeService collectionFacadeService;

    @Autowired
    private CollectionInventoryRedisService collectionInventoryRedisService;

    /**
     * 藏品列表
     *
     * @param
     * @return 结果
     */
    @GetMapping("/collectionList")
    public MultiResult<CollectionVO> collectionList(@NotBlank String state, String keyword, int pageSize, int currentPage) {
        CollectionPageQueryRequest collectionPageQueryRequest = new CollectionPageQueryRequest();
        collectionPageQueryRequest.setState(state);
        collectionPageQueryRequest.setKeyword(keyword);
        collectionPageQueryRequest.setCurrentPage(currentPage);
        collectionPageQueryRequest.setPageSize(pageSize);
        PageResponse<CollectionVO> pageResponse = collectionFacadeService.pageQuery(collectionPageQueryRequest);
        return MultiResultConvertor.convert(pageResponse);
    }

    /**
     * 藏品详情
     *
     * @param
     * @return 结果
     */
    @GetMapping("/collectionInfo")
    public Result<CollectionVO> collectionInfo(@NotBlank String collectionId) {
        SingleResponse<CollectionVO> singleResponse = collectionFacadeService.queryById(Long.valueOf(collectionId));
        return Result.success(singleResponse.getData());
    }

    /**
     * 用户持有藏品列表
     *
     * @param
     * @return 结果
     */
    @GetMapping("/heldCollectionList")
    public MultiResult<HeldCollectionVO> heldCollectionList(String keyword, String state, int pageSize, int currentPage) {
        String userId = (String) StpUtil.getLoginId();
        HeldCollectionPageQueryRequest heldCollectionPageQueryRequest = new HeldCollectionPageQueryRequest();
        heldCollectionPageQueryRequest.setState(state);
        heldCollectionPageQueryRequest.setUserId(userId);
        heldCollectionPageQueryRequest.setCurrentPage(currentPage);
        heldCollectionPageQueryRequest.setPageSize(pageSize);
        heldCollectionPageQueryRequest.setKeyword(keyword);
        PageResponse<HeldCollectionVO> pageResponse = collectionFacadeService.pageQueryHeldCollection(heldCollectionPageQueryRequest);
        return MultiResultConvertor.convert(pageResponse);
    }

    /**
     * 用户持有藏品详情
     *
     * @param
     * @return 结果
     */
    @GetMapping("/heldCollectionInfo")
    public Result<HeldCollectionVO> heldCollectionInfo(@NotBlank String heldCollectionId) {
        SingleResponse<HeldCollectionVO> singleResponse = collectionFacadeService.queryHeldCollectionById(Long.valueOf(heldCollectionId));
        return Result.success(singleResponse.getData());
    }


}
