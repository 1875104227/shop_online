package cn.niit.shop_online.common.exception;

import cn.niit.shop_online.common.result.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 86187
 * @Date 2023/11/7
 */

/*
*自定义异常，可以根据业务需求自定义异常信息，包括错误码、错误消息等，
* 使异常信息更加贴合业务需求，有助于错误信息的准确传达和处理
* */
@Data
@EqualsAndHashCode
public class ServerException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private int code;
    private String msg;
    public ServerException(String msg) {
        super( msg );
        this.code = ErrorCode.INTERNAL_SERVER_ERROR.getCode();
        this.msg = msg;
    }
    public ServerException(ErrorCode errorCode) {
        super( errorCode.getMsg());
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg( );
    }
    public ServerException( String msg,Throwable e) {
        super(msg,e);
        this.code = ErrorCode.INTERNAL_SERVER_ERROR.getCode( );
        this.msg = msg;
    }
}
