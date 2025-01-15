package com.lcl.nft.web.handler;

import com.google.common.collect.Maps;
import com.lcl.nft.base.exception.BizException;
import com.lcl.nft.base.exception.SystemException;
import com.lcl.nft.web.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

import static com.lcl.nft.base.response.ResponseCode.SYSTEM_ERROR;

/**
 * @Author conglongli
 * @date 2025/1/9 19:08
 */
@ControllerAdvice
@Slf4j
public class GlobalWebExceptionHandler {

    /**
     * 自定义方法参数校验异常处理器
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException occurred. ", ex);
        Map<String, String> errors = Maps.newHashMapWithExpectedSize(1);
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    /**
     * 自定义业务异常处理器
     * @param bizException
     * @return
     */
    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result exceptionHandler(BizException bizException) {
        log.error("bizException occurred.", bizException);
        Result result = new Result();
        result.setCode(bizException.getErrorCode().getCode());
        result.setMessage(bizException.getMessage());
        result.setSuccess(false);
        return result;
    }

    /**
     * 自定义系统异常处理器
     * @param systemException
     * @return
     */
    @ExceptionHandler(SystemException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result systemExceptionHandler(SystemException systemException) {
        log.error("systemException occurred.", systemException);
        Result result = new Result();
        result.setCode(systemException.getErrorCode().getCode());
        result.setMessage(systemException.getMessage());
        result.setSuccess(false);
        return result;
    }

    /**
     * 自定义系统异常处理器
     * @param throwable
     * @return
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result throwableHandler(Throwable throwable) {
        log.error("throwable occurred.", throwable);
        Result result = new Result();
        result.setCode(SYSTEM_ERROR.name());
        result.setMessage(throwable.getMessage());
        result.setSuccess(false);
        return result;
    }
}
