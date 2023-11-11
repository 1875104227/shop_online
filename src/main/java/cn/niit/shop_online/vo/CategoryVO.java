package cn.niit.shop_online.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author 86187
 * @Date 2023/11/11
 */
@Data
public class CategoryVO {
    @Schema(description = "主键id")
    private Integer id;
    @Schema(description = "分类名称")
    private String name;
    @JsonProperty("picture")
    @Schema(description = "封面图")
    private String icon;
    @Schema(description = "分类子集列表")
    private List<CategoryChildrenGoodsVO> children;
}
