package cn.niit.shop_online.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author 86187
 * @Date 2023/11/11
 */
@Data
public class RecommendByTabGoodsQuery extends Query {
    @Schema(description = "分类 tabId")
    private Integer subType;
}
