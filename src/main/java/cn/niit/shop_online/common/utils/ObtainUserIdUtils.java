package cn.niit.shop_online.common.utils;


import cn.niit.shop_online.common.exception.ServerException;
import cn.niit.shop_online.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public class ObtainUserIdUtils {

    public static Integer getUserId(HttpServletRequest request) {
        if (request.getAttribute("userId") == null) {
            throw new ServerException("用户不存在");
        }
        Integer userId = Integer.parseInt(request.getAttribute("userId").toString());
        if (userId == null) {
            throw new ServerException("用户不存在");
        }
        return userId;
    }


}