package cn.niit.shop_online.service;

import cn.niit.shop_online.entity.User;
import cn.niit.shop_online.query.UserLoginQuery;
import cn.niit.shop_online.vo.LoginResultVO;
import cn.niit.shop_online.vo.UserVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yaaii
 * @since 2023-11-07
 */
public interface UserService extends IService<User> {
    LoginResultVO login(UserLoginQuery query);
    User getUserInfo(Integer userId);
    UserVO editUserInfo(UserVO userVO);
    String editUserAvatar(Integer User, MultipartFile file);
}
