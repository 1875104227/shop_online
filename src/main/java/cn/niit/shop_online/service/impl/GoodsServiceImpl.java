package cn.niit.shop_online.service.impl;

import cn.niit.shop_online.common.exception.ServerException;
import cn.niit.shop_online.common.result.PageResult;
import cn.niit.shop_online.convert.GoodsConvert;
import cn.niit.shop_online.entity.*;
import cn.niit.shop_online.mapper.*;
import cn.niit.shop_online.query.Query;
import cn.niit.shop_online.query.RecommendByTabGoodsQuery;
import cn.niit.shop_online.service.GoodsService;
import cn.niit.shop_online.vo.GoodsVO;
import cn.niit.shop_online.vo.IndexTabGoodsVO;
import cn.niit.shop_online.vo.IndexTabRecommendVO;
import cn.niit.shop_online.vo.RecommendGoodsVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;


import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
        private final IndexRecommendMapper indexRecommendMapper;
        private final IndexRecommendTabMapper indexRecommendTabMapper;
        private final GoodsDetailMapper goodsDetailMapper;

        private final GoodsSpecificationMapper goodsSpecificationMapper;
        private final GoodsSpecificationDetailMapper goodsSpecificationDetailMapper;

    @Override
    public IndexTabRecommendVO getTabRecommendGoodsByTabId(RecommendByTabGoodsQuery query) {
        IndexRecommend indexRecommend = indexRecommendMapper.selectById(query.getSubType());
        if(indexRecommend == null){
            throw new ServerException("推荐分类不存在");
        }
        LambdaQueryWrapper<IndexRecommendTab> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IndexRecommendTab::getRecommendId,indexRecommend.getId());
        List<IndexRecommendTab> tabList = indexRecommendTabMapper.selectList(wrapper);
        if(tabList.size()==0){
            throw  new ServerException("该分类下不存在tab分类");
        }
//
        List<IndexTabGoodsVO> list = new ArrayList<>();
        for(IndexRecommendTab item:tabList){
            IndexTabGoodsVO tabGoods=new IndexTabGoodsVO();

            tabGoods.setId(item.getId());
            tabGoods.setName(item.getName());

            Page<Goods> page= new Page<>(query.getPage(),query.getPageSize());
            Page<Goods> goodsPage=baseMapper.selectPage(page,new LambdaQueryWrapper<Goods>().eq(Goods::getTabId,item.getId()));
            List<RecommendGoodsVO> goodsList = GoodsConvert.INSTANCE.convertToRecommendGoodsVOList(goodsPage.getRecords());
            PageResult<RecommendGoodsVO> result=new PageResult<>(page.getTotal(), query.getPageSize(), query.getPage(), page.getPages(),goodsList );
            tabGoods.setGoodsItems(result);
            list.add(tabGoods);
        }
        IndexTabRecommendVO recommendV0 = new IndexTabRecommendVO( );
        recommendV0.setId( indexRecommend. getId());
        recommendV0.setName( indexRecommend. getName( ));
        recommendV0.setCover( indexRecommend.getCover( ));
        recommendV0.setSubTypes( list);
        return recommendV0;
    }

    @Override
    public PageResult<RecommendGoodsVO> getRecommendGoodsByPage(Query query) {
        Page<Goods> page = new Page<>(query.getPage(), query.getPageSize());
        Page<Goods> goodsPage=baseMapper.selectPage(page,null);
        List<RecommendGoodsVO> result=GoodsConvert.INSTANCE.convertToRecommendGoodsVOList(goodsPage.getRecords());

        return new PageResult<>(page.getTotal(), query.getPageSize(), query.getPage(), page.getPages(),result);
    }

    @Override
    public GoodsVO getGoodsDetail(Integer id) {
        Goods goods = baseMapper.selectById(id);
        if(goods==null){
            throw new ServerException("商品不存在");
        }
        GoodsVO goodsVO = GoodsConvert.INSTANCE.convertToGoodsVO(goods);
        List<GoodsDetail> goodsDetails=goodsDetailMapper.selectList(new LambdaQueryWrapper<GoodsDetail>().eq(
                GoodsDetail::getGoodsId,goods.getId()
        ));
        goodsVO.setProperties(goodsDetails);
        List<GoodsSpecification> specificationList=goodsSpecificationMapper.selectList(
                new LambdaQueryWrapper<GoodsSpecification>().eq(GoodsSpecification::getGoodsId,goods.getId())
        );
        goodsVO.setSpecs(specificationList);
//      商品规格详情
        List<GoodsSpecificationDetail> goodsSpecificationDetails=goodsSpecificationDetailMapper.selectList(
                new LambdaQueryWrapper<GoodsSpecificationDetail>().eq(GoodsSpecificationDetail::getGoodsId,goods.getId())
        );
        goodsVO.setSkus(goodsSpecificationDetails);
//        查找同类商品，去除自身
        List<Goods> goodsList= baseMapper.selectList(new LambdaQueryWrapper<Goods>().eq(
                Goods::getCategoryId,goods.getCategoryId()).ne(Goods::getId,goods.getId()
        ));
        List<RecommendGoodsVO> goodsVOList=GoodsConvert.INSTANCE.convertToRecommendGoodsVOList(goodsList);
        goodsVO.setSimilarProducts(goodsVOList);
        return goodsVO;
    }
}
