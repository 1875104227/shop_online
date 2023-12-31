package cn.niit.shop_online.common.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author 86187
 * @Date 2023/11/7
 */
/*
* 一般的接口数据响应体包括 code、msg、data 这三个必不可少的信息，
* code 表示接口响应状态码、msg 表示接口响应内容、data 中包含的是接口响应数据
* */
@Data
@Schema(description = "响应数据")
public class Result<T> {
    @Schema(description = "编码 0 表示成功，其他值表示失败")
    private int code = 0;

    @Schema(description = "消息内容")
    private String msg = "success";

    @Schema(description = "响应数据")
    private T result;

    public static <T> Result<T> ok() {
        return ok(null);
    }


    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<>();
        result.setResult(data);
        return result;
    }


    public static <T> Result<T> error() {
        return error(ErrorCode.INTERNAL_SERVER_ERROR);
    }


    public static <T> Result<T> error(String msg) {
        return error(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), msg);
    }

    public static <T> Result<T> error(ErrorCode errorCode) {
        return error(errorCode.getCode(), errorCode.getMsg());
    }


    public static <T> Result<T> error(int code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }


}
