package cn.niit.shop_online.convert;

import cn.niit.shop_online.entity.UserShoppingAddress;
import cn.niit.shop_online.vo.UserAddressVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author 86187
 * @Date 2023/11/25
 */
@Mapper
public interface UserAddressConvert {

    UserAddressConvert INSTANCE = Mappers.getMapper(UserAddressConvert.class);


    UserAddressVO convertToUserAddressVO(UserShoppingAddress userShippingAddress);


    List<UserAddressVO> convertToUserAddressVOList(List<UserShoppingAddress> list);
}
