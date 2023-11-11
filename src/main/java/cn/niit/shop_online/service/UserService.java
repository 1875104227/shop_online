package cn.niit.shop_online.service;

import cn.niit.shop_online.entity.User;
import cn.niit.shop_online.query.UserLoginQuery;
import cn.niit.shop_online.vo.LoginResultVO;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.models.auth.In;

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
}
