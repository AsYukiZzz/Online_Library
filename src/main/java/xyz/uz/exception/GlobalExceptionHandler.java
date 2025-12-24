package xyz.uz.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import xyz.uz.result.Result;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 1. 处理自定义业务异常
     * 场景：手动 throw new BizException("余额不足");
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBizException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * 2. 处理参数校验异常 (JSON Body 传参)
     * 场景：@RequestBody + @Valid 校验失败
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String msg = getBindingResultMsg(e.getBindingResult());
        log.warn("参数校验失败(JSON): {}", msg);
        return Result.fail(400, msg);
    }

    /**
     * 3. 处理参数校验异常 (Form 表单/URL 传参)
     * 场景：@RequestParam + @Valid 校验失败
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e) {
        String msg = getBindingResultMsg(e.getBindingResult());
        log.warn("参数校验失败(Form): {}", msg);
        return Result.fail(400, msg);
    }

    /**
     * 5. 处理参数类型不匹配
     * 场景：需要 Integer 传了 String
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String msg = String.format("参数[%s]类型错误，应为[%s]", e.getName(), e.getRequiredType().getSimpleName());
        log.warn("参数类型错误: {}", msg);
        return Result.fail(400, msg);
    }

    /**
     * 6. 处理缺少必填参数
     * 场景：@RequestParam(required=true) 但没传
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        String msg = String.format("缺少必要参数[%s]", e.getParameterName());
        log.warn("缺少参数: {}", msg);
        return Result.fail(400, msg);
    }

    /**
     * 7. 处理请求方法不支持
     * 场景：接口只支持 POST，你用了 GET
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        String msg = String.format("不支持[%s]请求，仅支持[%s]", e.getMethod(), e.getSupportedHttpMethods());
        log.warn("请求方法错误: {}", msg);
        return Result.fail(405, msg);
    }

    /**
     * 8. 处理 JSON 解析错误
     * 场景：JSON 格式不对，或者缺少逗号/括号
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("JSON解析失败: {}", e.getMessage());
        return Result.fail(400, "请求体格式错误或数据类型不匹配");
    }

    /**
     * 9. 兜底处理：所有未知的系统异常
     * 注意：生产环境尽量不要直接返回 e.getMessage()，避免暴露堆栈信息
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e) {
        // 只有这里需要打印 error 级别的堆栈日志，方便排查 bug
        log.error("系统内部异常", e);
        return Result.fail(500, "系统繁忙，请稍后重试");
    }


    /**
     * 提取校验失败的提示信息（拼接多个错误）
     */
    private String getBindingResultMsg(BindingResult result) {
        return result.getAllErrors().stream()
                .map(error -> {
                    if (error instanceof FieldError) {
                        return ((FieldError) error).getField() + ": " + error.getDefaultMessage();
                    }
                    return error.getDefaultMessage();
                }).collect(Collectors.joining("; "));
    }
}
