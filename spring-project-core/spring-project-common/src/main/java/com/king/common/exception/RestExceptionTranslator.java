package com.king.common.exception;

import com.king.common.util.CollectionUtil;
import com.king.common.util.Func;
import com.king.common.util.Result;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.Servlet;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * 异常统一拦截器
 *
 * @author jinfeng.wu
 * @date 2020/9/15 9:59
 */
@Configuration
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@ConditionalOnWebApplication(
        type = ConditionalOnWebApplication.Type.SERVLET
)
@RestControllerAdvice
public class RestExceptionTranslator {

    private static final Logger log = LoggerFactory.getLogger(RestExceptionTranslator.class);

    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.OK)
    public Result handleError(MissingServletRequestParameterException e) {
        log.warn("缺少请求参数->【{}】", e.getMessage());
        String message = String.format("缺少必要的请求参数: %s", e.getParameterName());
        return Result.fail(ResultCode.PARAM_NOT_COMPLETE.code, message);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.OK)
    public Result handleError(MethodArgumentTypeMismatchException e) {
        log.warn("请求参数格式错误->【{}】", e.getMessage());
        String message = String.format("请求参数格式错误: %s", e.getName());
        return Result.fail(ResultCode.PARAM_IS_INVALID.code, message);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.OK)
    public Result handleError(MethodArgumentNotValidException e) {
        log.warn("参数验证失败->【{}】", e.getMessage());
        return this.handleError(e.getBindingResult());
    }

    @ExceptionHandler({BindException.class})
    @ResponseStatus(HttpStatus.OK)
    public Result handleError(BindException e) {
        log.warn("参数绑定失败->【{}】", e.getMessage());
        return this.handleError(e.getBindingResult());
    }

    private Result handleError(BindingResult result) {
        FieldError error = result.getFieldError();
        String message = null;
        if (error != null) {
            String fieldMessage = error.getDefaultMessage();
            boolean flag = Func.isNotBlank(fieldMessage) && fieldMessage.startsWith("!") && fieldMessage.length() > 1;
            message = String.format("%s:%s", error.getField(), fieldMessage);
        } else if (CollectionUtil.isNotEmpty(result.getAllErrors())) {
            message = ((ObjectError)result.getAllErrors().get(0)).getDefaultMessage();
        }

        return Result.fail(ResultCode.PARAM_IS_INVALID.code, message);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.OK)
    public Result handleError(ConstraintViolationException e) {
        log.warn("参数验证失败->【{}】", e.getMessage());
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = (ConstraintViolation)violations.iterator().next();
        String path = ((PathImpl)violation.getPropertyPath()).getLeafNode().getName();
        String message = String.format("%s:%s", path, violation.getMessage());
        return Result.fail(ResultCode.PARAM_IS_INVALID.code, message);
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result handleError(NoHandlerFoundException e) {
        log.error("404没找到请求:{}", e.getMessage());
        return Result.error(HttpStatus.NOT_FOUND, ResultCode.INTERFACE_ADDRESS_INVALID.getMessage());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.OK)
    public Result handleError(HttpMessageNotReadableException e) {
        log.error("消息不能读取:{}", e.getMessage());
        return Result.error(Result.STATUS_SUCCESS, ResultCode.PARAM_IS_INVALID.getMessage());
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result handleError(HttpRequestMethodNotSupportedException e) {
        log.error("不支持当前请求方法:{}", e.getMessage());
        return Result.error(HttpStatus.METHOD_NOT_ALLOWED, ResultCode.INTERFACE_NOT_SUPPORTED_METHOD_ERROR.getMessage());
    }

    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public Result handleError(HttpMediaTypeNotSupportedException e) {
        log.error("不支持当前媒体类型:{}", e.getMessage());
        return Result.error(ResultCode.INTERFACE_NOT_SUPPORTED_MEDIA_ERROR.code, ResultCode.INTERFACE_NOT_SUPPORTED_MEDIA_ERROR.getMessage());
    }

    @ExceptionHandler({ServiceException.class})
    @ResponseStatus(HttpStatus.OK)
    public Result handleError(ServiceException e) {
        log.error("业务异常", e);
        String message = e.getMessage();
        return Result.fail(e.getResultCode().getCode(), message);
    }

    @ExceptionHandler({Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handleError(Throwable e) {
        log.error("服务器异常", e);
        return Result.error(ResultCode.INTERFACE_INNER_INVOKE_ERROR.code ,ResultCode.INTERFACE_INNER_INVOKE_ERROR.getMessage());
    }
}
