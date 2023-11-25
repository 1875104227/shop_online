package cn.niit.shop_online.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author 86187
 * @Date 2023/11/25
 */
@Data
public class SubmitOrderVO {
    @Schema(description = "用户地址列表")
    private List<UserAddressVO> userAddresses;
    @Schema(description = "商品集合")
    private List<UserOrderGoodsVO> goods;
    @Schema(description = "综述信息")
    private OrderInfoVO summary;
}
