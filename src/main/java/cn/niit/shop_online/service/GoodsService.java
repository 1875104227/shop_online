package cn.niit.shop_online.service;

import cn.niit.shop_online.common.result.PageResult;
import cn.niit.shop_online.entity.Goods;
import cn.niit.shop_online.query.Query;
import cn.niit.shop_online.query.RecommendByTabGoodsQuery;
import cn.niit.shop_online.vo.GoodsVO;
import cn.niit.shop_online.vo.IndexTabRecommendVO;
import cn.niit.shop_online.vo.RecommendGoodsVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yaaii
 * @since 2023-11-07
 */
public interface GoodsService extends IService<Goods> {
    IndexTabRecommendVO getTabRecommendGoodsByTabId(RecommendByTabGoodsQuery query);
    PageResult<RecommendGoodsVO> getRecommendGoodsByPage(Query query);
    GoodsVO getGoodsDetail(Integer id);
}
