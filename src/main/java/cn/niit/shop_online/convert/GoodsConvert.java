package cn.niit.shop_online.convert;

import cn.niit.shop_online.entity.Goods;
import cn.niit.shop_online.vo.GoodsVO;
import cn.niit.shop_online.vo.RecommendGoodsVO;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

/**
 * @author 86187
 * @Date 2023/11/11
 */
@Mapper
public interface GoodsConvert {
    GoodsConvert INSTANCE = Mappers.getMapper(GoodsConvert.class);


    @Mapping(expression = "java(MapStruct.strToList(goods.getProductPictures()))", target = "productPictures")
    @Mapping(expression = "java(MapStruct.strToList(goods.getMainPictures()))", target = "mainPictures")
    GoodsVO convertToGoodsVO(Goods goods);

    List<RecommendGoodsVO> convertToRecommendGoodsVOList(List<Goods> goodsList);

    class MapStruct {
        public static List<String> strToList(String str) {
            if (StringUtils.isNotEmpty(str)) {
                return Arrays.asList(str.split(","));
            }
            return null;
        }
    }
}