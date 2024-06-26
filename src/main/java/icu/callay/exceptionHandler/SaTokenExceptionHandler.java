package icu.callay.exceptionHandler;

import cn.dev33.satoken.exception.*;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SaTokenExceptionHandler {

    @ExceptionHandler(NotLoginException.class)
    public SaResult handlerNotLoginException(NotLoginException nle) {
        // 不同异常返回不同状态码
        String message = "";
        if (nle.getType().equals(NotLoginException.NOT_TOKEN)) {
            message = "未提供Token";
        } else if (nle.getType().equals(NotLoginException.INVALID_TOKEN)) {
            message = "未提供有效的Token";
        } else if (nle.getType().equals(NotLoginException.TOKEN_TIMEOUT)) {
            message = "登录信息已过期，请重新登录";
        } else if (nle.getType().equals(NotLoginException.BE_REPLACED)) {
            message = "您的账户已在另一台设备上登录，如非本人操作，请立即修改密码";
        } else if (nle.getType().equals(NotLoginException.KICK_OUT)) {
            message = "已被系统强制下线";
        } else {
            message = "当前会话未登录";
        }
        // 返回给前端
        return new SaResult(401, message,"");
    }

    @ExceptionHandler
    public SaResult handlerNotRoleException(NotRoleException e) {
        return new SaResult(403, "无此角色：" + e.getRole(),"");
    }

    @ExceptionHandler
    public SaResult handlerNotPermissionException(NotPermissionException e) {
        return new SaResult(403, "无此权限：" + e.getCode(),"");
    }

    @ExceptionHandler
    public SaResult handlerDisableLoginException(DisableServiceException e) {
        return new SaResult(401, "账户被封禁：" + e.getDisableTime() + "秒后解封","");
    }

    @ExceptionHandler
    public SaResult handlerNotSafeException(NotSafeException e) {
        return new SaResult(401, "二级认证异常：" + e.getMessage(),"");
    }
}
