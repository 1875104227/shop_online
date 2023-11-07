package cn.niit.shop_online.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
* 异常处理的目的是为了提高系统的稳定性、可靠性和用户体验；
* 可以避免系统崩溃或产生不友好的提示；自定义异常可以定制响应码和响应信息，
* 避免将详细的异常信息暴露给客户端，提高系统的安全性
* */
@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNAUTHTHORIZED(401,"登陆失败，请重新登录"),
    INTERNAL_SERVER_ERROR(500,"服务器异常，请稍后在试");

    private final int code;
    private final String msg;
}

