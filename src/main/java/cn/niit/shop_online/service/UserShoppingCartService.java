package cn.niit.shop_online.service;

import cn.niit.shop_online.entity.UserShoppingCart;
import cn.niit.shop_online.query.CartQuery;
import cn.niit.shop_online.vo.CartGoodsVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yaaii
 * @since 2023-11-07
 */
public interface UserShoppingCartService extends IService<UserShoppingCart> {
//    添加购物车
    CartGoodsVO addShopCart(CartQuery query);
}
