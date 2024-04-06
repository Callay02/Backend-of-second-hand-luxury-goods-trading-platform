package icu.callay.exceptionHandler;

import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * &#064;projectName:    springboot
 * &#064;package:        icu.callay.exceptionHandler
 * &#064;className:      ExceptionHandler
 * &#064;author:     Callay
 * &#064;description:  全局异常处理
 * &#064;date:    2024/4/6 12:09
 * &#064;version:    1.0
 */
@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public SaResult handlerRuntimeException(RuntimeException rte){
        return SaResult.error(rte.getMessage());
    }
}
