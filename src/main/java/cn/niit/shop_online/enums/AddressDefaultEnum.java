package cn.niit.shop_online.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 86187
 * @Date 2023/11/11
 */
@Getter
@AllArgsConstructor
public enum AddressDefaultEnum {
    NOT_DEFAULT_ADDRESS(0, "不是默认地址"),
    DEFAULT_ADDRESS(1, "默认地址");
    private final Integer value;
    private final String name;
}
