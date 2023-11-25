package cn.niit.shop_online.convert;

import cn.niit.shop_online.entity.UserOrder;
import cn.niit.shop_online.vo.OrderDetailVO;
import org.mapstruct.factory.Mappers;

/**
 * @author 86187
 * @Date 2023/11/25
 */
public class UserOrderDetailConvent {
    UserOrderDetailConvent INSTANCE= Mappers.getMapper(UserOrderDetailConvent.class);

    OrderDetailVO convertToOrderDetailVO(UserOrder userOrder) {
        return null;
    }
}
