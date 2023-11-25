package cn.niit.shop_online.service.impl;

import cn.niit.shop_online.entity.UserOrderGoods;
import cn.niit.shop_online.mapper.UserOrderGoodsMapper;
import cn.niit.shop_online.service.UserOrderGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author
 * @since 2023-11-25
 */
@Service
public class UserOrderGoodsServiceImpl extends ServiceImpl<UserOrderGoodsMapper, UserOrderGoods> implements UserOrderGoodsService {

    @Override
    public void batchUserOrderGoods(List<UserOrderGoods> list) {
        saveBatch(list);
    }
}
