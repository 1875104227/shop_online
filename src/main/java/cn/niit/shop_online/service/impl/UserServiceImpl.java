package cn.niit.shop_online.service.impl;

import cn.niit.shop_online.entity.User;
import cn.niit.shop_online.mapper.UserMapper;
import cn.niit.shop_online.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yaaii
 * @since 2023-11-07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
