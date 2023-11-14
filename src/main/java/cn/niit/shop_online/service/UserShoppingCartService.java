package cn.niit.shop_online.service;

import cn.niit.shop_online.entity.UserShoppingCart;
import cn.niit.shop_online.query.CartQuery;
import cn.niit.shop_online.query.EditCartQuery;
import cn.niit.shop_online.vo.CartGoodsVO;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
//    购物车列表
    List<CartGoodsVO> shopCartList(Integer userId);
//    修改购物车
    CartGoodsVO editCart(EditCartQuery query);
//    删除/清空购物车单品
    void removeCartGoods(Integer userId,List<Integer> id);

}
