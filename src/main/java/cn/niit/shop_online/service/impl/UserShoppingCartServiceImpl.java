package cn.niit.shop_online.service.impl;

import cn.niit.shop_online.common.exception.ServerException;
import cn.niit.shop_online.entity.Goods;
import cn.niit.shop_online.entity.UserShoppingCart;
import cn.niit.shop_online.mapper.GoodsMapper;
import cn.niit.shop_online.mapper.UserShoppingCartMapper;
import cn.niit.shop_online.query.CartQuery;
import cn.niit.shop_online.query.EditCartQuery;
import cn.niit.shop_online.service.UserShoppingCartService;
import cn.niit.shop_online.vo.CartGoodsVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yaaii
 * @since 2023-11-07
 */
@Service
@AllArgsConstructor
public class UserShoppingCartServiceImpl extends ServiceImpl<UserShoppingCartMapper, UserShoppingCart> implements UserShoppingCartService {
    private final GoodsMapper goodsMapper;
    @Override
    public CartGoodsVO addShopCart(CartQuery query) {
//        查看商品是否存在，以及库存是否充足
        Goods goods = goodsMapper.selectById(query.getId());
        if(goods==null){
            throw  new ServerException("商品信息不存在");
        }
        if (query.getCount()>goods.getInventory()){
            throw new ServerException("商品库存不足");
        }
//        插入购物车信息
        UserShoppingCart userShoppingCart = new UserShoppingCart();
        userShoppingCart.setUserId(query.getUserId());
        userShoppingCart.setGoodsId(goods.getId());
        userShoppingCart.setPrice(goods.getPrice());
        userShoppingCart.setCount(query.getCount());
        userShoppingCart.setAttrsText(query.getAttrsText());
        userShoppingCart.setSelected(false);
        baseMapper.insert(userShoppingCart);

//        返回用户添加购物车信息
        CartGoodsVO goodsVO = new CartGoodsVO( );
        goodsVO.setId(userShoppingCart.getId());
        goodsVO. setName( goods.getName());
        goodsVO.setAttrsText( query.getAttrsText( ));
        goodsVO.setPrice(userShoppingCart.getPrice( ));
        goodsVO.setNowPrice( goods.getPrice());
        goodsVO.setSelected(userShoppingCart.getSelected( ));
        goodsVO.setStock( goods.getInventory( ));
        goodsVO.setCount(query.getCount( ));
        goodsVO. setPicture( goods.getCover( ));
        goodsVO.setDiscount( goods.getDiscount( ));
        return goodsVO;
    }

    @Override
    public List<CartGoodsVO> shopCartList(Integer userId) {
            List<CartGoodsVO> list=baseMapper.getCartGoodsInfo(userId);
            return list;
    }

    @Override
    public CartGoodsVO editCart(EditCartQuery query) {
        UserShoppingCart userShoppingCart = baseMapper.selectById(query.getId());
        if(userShoppingCart == null){
            throw new ServerException("购物车信息不存在");
        }
        userShoppingCart.setCount(query.getCount());
        userShoppingCart.setSelected(query.getSelected());
        baseMapper.updateById(userShoppingCart);
//        查询购物车信息
        Goods goods=goodsMapper.selectById(userShoppingCart.getGoodsId());
        if(query.getCount()>goods.getInventory()){
            throw new ServerException(goods.getName()+"库存数量不足");
        }
        CartGoodsVO goodsVO = new CartGoodsVO();
        goodsVO.setId(userShoppingCart.getId());
        goodsVO.setName(goods.getName());
        goodsVO.setAttrsText(userShoppingCart.getAttrsText());
        goodsVO.setPrice(userShoppingCart.getPrice());
        goodsVO.setNowPrice(goods.getPrice());
        goodsVO.setSelected(userShoppingCart.getSelected());
        goodsVO.setStock(goods.getInventory());
        goodsVO.setCount(query.getCount());
        goodsVO.setPicture(goods.getCover());
        goodsVO.setDiscount(goods.getDiscount());
        return goodsVO;
    }


}
