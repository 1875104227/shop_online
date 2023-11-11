package cn.niit.shop_online.query;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author 86187
 * @Date 2023/11/11
 */
@Data
public class UserLoginQuery {
    @NotBlank(message = "code不能为空")
    private String code;
}
