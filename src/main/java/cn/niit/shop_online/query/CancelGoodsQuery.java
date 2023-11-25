package cn.niit.shop_online.query;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;



/**
 * @author 86187
 * @Date 2023/11/25
 */
@Data
public class CancelGoodsQuery {
    @Schema(description = "主键")
    @NotNull(message = "订单id不能为空")
    private Integer id;
    @Schema(description = "取消理由")
    @NotBlank(message = "取消理由不能为空")
    private String cancelReason;
}

