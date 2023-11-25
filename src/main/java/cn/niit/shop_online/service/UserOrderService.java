package cn.niit.shop_online.service;

import cn.niit.shop_online.common.result.PageResult;
import cn.niit.shop_online.entity.UserOrder;
import cn.niit.shop_online.query.CancelGoodsQuery;
import cn.niit.shop_online.query.OrderPreQuery;
import cn.niit.shop_online.query.OrderQuery;
import cn.niit.shop_online.vo.OrderDetailVO;
import cn.niit.shop_online.vo.SubmitOrderVO;
import cn.niit.shop_online.vo.UserAddressVO;
import cn.niit.shop_online.vo.UserOrderVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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
    /**
     * 填写订单-获取预付订单
     */
    SubmitOrderVO getPreOrderDetail(Integer userId);

    List<UserAddressVO> getAddressListByUserId(Integer userId,Integer addressId);

//    填写订单-获取立即购买订单
    SubmitOrderVO getPreNowOrderDetail(OrderPreQuery query);

//    填写订单-获取再次购买订单
    SubmitOrderVO getRepurchaseOrderDetail(Integer id);

    /**
     * 订单列表
     *
     * @param query
     * @return
     */
    PageResult<OrderDetailVO> getOrderList(OrderQuery query);

    /**
     * 取消订单
     *
     * @param query
     * @return
     */
    OrderDetailVO cancelOrder(CancelGoodsQuery query);
    /**
     * 删除订单
     *
     * @param ids
     */
    void deleteOrder(List<Integer> ids,Integer userId);

    /**
     * 订单支付
     *
     * @param id
     */
    void payOrder(Integer id);
}