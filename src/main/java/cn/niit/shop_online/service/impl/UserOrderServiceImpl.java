package cn.niit.shop_online.service.impl;

import cn.niit.shop_online.entity.UserOrder;
import cn.niit.shop_online.mapper.UserOrderMapper;
import cn.niit.shop_online.service.UserOrderService;
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
public class UserOrderServiceImpl extends ServiceImpl<UserOrderMapper, UserOrder> implements UserOrderService {

}
