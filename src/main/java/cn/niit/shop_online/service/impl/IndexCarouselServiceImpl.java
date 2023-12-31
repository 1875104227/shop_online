package cn.niit.shop_online.service.impl;

import cn.niit.shop_online.entity.IndexCarousel;
import cn.niit.shop_online.mapper.IndexCarouselMapper;
import cn.niit.shop_online.service.IndexCarouselService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class IndexCarouselServiceImpl extends ServiceImpl<IndexCarouselMapper, IndexCarousel> implements IndexCarouselService {

    @Override
    public List<IndexCarousel> getList(Integer distributionSite) {
        LambdaQueryWrapper<IndexCarousel> wrapper= new LambdaQueryWrapper<IndexCarousel>();
        if(distributionSite !=null){
            wrapper.eq(IndexCarousel::getType,distributionSite);
        }
//        设置排序根据创建时间倒序排序
            wrapper.orderByDesc(IndexCarousel::getCreateTime);
//        查询广告列表
        List<IndexCarousel> list= baseMapper.selectList(wrapper);

        return list;
    }
}
