package cn.niit.shop_online.mapper;

import cn.niit.shop_online.entity.UserShoppingCart;
import cn.niit.shop_online.vo.CartGoodsVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yaaii
 * @since 2023-11-07
 */
public interface UserShoppingCartMapper extends BaseMapper<UserShoppingCart> {
    //    查询购物车信息
    List<CartGoodsVO> getCartGoodsInfo(@Param("id") Integer id);
}
