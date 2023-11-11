package cn.niit.shop_online.vo;

import cn.niit.shop_online.common.result.PageResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author 86187
 * @Date 2023/11/11
 */
@Data
public class IndexTabGoodsVO {
    @Schema(description = "主键id")
    private Integer id;
    @Schema(description = "tab名称")
    @JsonProperty("title")
    private String name;
    @Schema(description = "tab下对应的商品列表")
    private PageResult<RecommendGoodsVO> goodsItems;
}