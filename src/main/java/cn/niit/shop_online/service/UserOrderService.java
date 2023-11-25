package cn.niit.shop_online.service;

import cn.niit.shop_online.entity.UserOrder;
import cn.niit.shop_online.vo.OrderDetailVO;
import cn.niit.shop_online.vo.UserOrderVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yaaii
 * @since 2023-11-07
 */
public interface UserOrderService extends IService<UserOrder> {
//    提交订单
    Integer addGoodsOrder(UserOrderVO orderVO);
    OrderDetailVO getOrderDetail(Integer id);
}