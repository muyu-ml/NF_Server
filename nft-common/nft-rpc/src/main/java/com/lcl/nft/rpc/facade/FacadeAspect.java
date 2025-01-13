package com.lcl.nft.rpc.facade;

import com.alibaba.fastjson2.JSON;
import com.lcl.nft.base.exception.BizException;
import com.lcl.nft.base.exception.SystemException;
import com.lcl.nft.base.response.BaseResponse;
import com.lcl.nft.base.response.ResponseCode;
import com.lcl.nft.base.utils.BeanValidator;
import jakarta.validation.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @Author conglongli
 * @date 2025/1/13 10:02
 */
@Aspect
@Component
public class FacadeAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(FacadeAspect.class);

    @Around("@annotation(com.lcl.nft.rpc.facade.Facade)")
    public Object facade(ProceedingJoinPoint proceedingJoinPoint) throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Method method = ((MethodSignature)proceedingJoinPoint.getSignature()).getMethod();
        Object[] args = proceedingJoinPoint.getArgs();
        LOGGER.info("start to execute , method = " + method.getName() + " , args = " + JSON.toJSONString(args));

        Class<?> returnType = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod().getReturnType();

        // 循环遍历所有参数，进行参数校验
        for(Object parameter : args) {
            try {
                BeanValidator.validateObject(parameter);
            } catch (ValidationException e) {
                printLog(stopWatch, method, args, "failed to valid", null, e);
                return getFailedResponse(returnType, e);
            }
        }

        try{
            // 目标方法执行
            Object response = proceedingJoinPoint.proceed();
            enrichObject(response);
            printLog(stopWatch, method, args, "end to execute", response, null);
            return response;
        } catch (Throwable e) {
            // 如果执行异常，则返回一个失败的response
            printLog(stopWatch, method, args, "failed to execute", null, e);
            throw new RuntimeException(e);
        }
    }


    /**
     * 将response的信息补全，主要是code和message
     * @param response
     */
    private void enrichObject(Object response) {
        if (response instanceof BaseResponse) {
            if (((BaseResponse) response).getSuccess()) {
                //如果状态是成功的，需要将未设置的responseCode设置成SUCCESS
                if (StringUtils.isEmpty(((BaseResponse) response).getResponseCode())) {
                    ((BaseResponse) response).setResponseCode(ResponseCode.SUCCESS.name());
                }
            } else {
                //如果状态是失败的，需要将未设置的responseCode设置成BIZ_ERROR
                if (StringUtils.isEmpty(((BaseResponse) response).getResponseCode())) {
                    ((BaseResponse) response).setResponseCode(ResponseCode.BIZ_ERROR.name());
                }
            }
        }
    }

    /**
     * 定义并返回一个通用的失败响应
     * @param returnType
     * @param throwable
     * @return
     */
    private Object getFailedResponse(Class<?> returnType, Throwable throwable)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // 如果返回值的类型为BaseResponse 的子类，则创建一个通用的失败响应
        if(returnType.getDeclaredConstructor().newInstance() instanceof BaseResponse){
            BaseResponse response = (BaseResponse) returnType.getDeclaredConstructor().newInstance();
            response.setSuccess(false);
            if(throwable instanceof BizException bizException){
                response.setResponseCode(bizException.getErrorCode().getCode());
                response.setResponseMessage(bizException.getMessage());
            } else if (throwable instanceof SystemException systemException) {
                response.setResponseCode(systemException.getErrorCode().getCode());
                response.setResponseMessage(systemException.getMessage());
            } else {
                response.setResponseMessage(throwable.toString());
                response.setResponseCode(ResponseCode.BIZ_ERROR.name());
            }
            return response;
        }
        LOGGER.error(
                "failed to getFailedResponse , returnType (" + returnType + ") is not instanceof BaseResponse");
        return null;
    }

    /**
     * 日志打印
     * @param stopWatch
     * @param method
     * @param args
     * @param action
     * @param response
     * @param throwable
     */
    private void printLog(StopWatch stopWatch, Method method, Object[] args, String action, Object response, Throwable throwable) {
        try {
            //因为此处有JSON.toJSONString，可能会有异常，需要进行捕获，避免影响主干流程
            LOGGER.info(getInfoMessage(action, stopWatch, method, args, response, throwable), throwable);
        } catch (Exception e1) {
            LOGGER.error("log failed", e1);
        }
    }

    /**
     * 统一格式输出，方便做日志统计
     * <p>
     * *** 如果调整此处的格式，需要同步调整日志监控 ***
     *
     * @param action    行为
     * @param stopWatch 耗时
     * @param method    方法
     * @param args      参数
     * @param response  响应
     * @return 拼接后的字符串
     */
    private String getInfoMessage(String action, StopWatch stopWatch, Method method, Object[] args, Object response, Throwable exception) {
        StringBuilder sb = new StringBuilder();
        // 方法与耗时
        sb.append(" ,method = ").append(method.getName());
        sb.append(" , cost = ").append(stopWatch.getTime()).append(" ms");
        // 请求结果状态
        if(response instanceof BaseResponse) {
            sb.append(" , success = ").append(((BaseResponse)response).getSuccess());
        }
        if(exception != null){
            sb.append(" , success = ").append(false);
        }
        // 请求参数
        sb.append(" ,args = ").append(JSON.toJSONString(Arrays.toString(args)));
        // 请求结果
        if (response != null) {
            sb.append(" ,resp = ");
            sb.append(JSON.toJSONString(response));
        }

        if (exception != null) {
            sb.append(" ,exception = ");
            sb.append(exception.getMessage());
        }
        if (response instanceof BaseResponse) {
            BaseResponse baseResponse = (BaseResponse) response;
            if (!baseResponse.getSuccess()) {
                sb.append(" , execute_failed");
            }
        }

        return sb.toString();
    }


}
