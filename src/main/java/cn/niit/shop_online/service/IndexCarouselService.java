package cn.niit.shop_online.service;

import cn.niit.shop_online.entity.IndexCarousel;
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
public interface IndexCarouselService extends IService<IndexCarousel> {
    List<IndexCarousel> getList(Integer distributionSite);
}
