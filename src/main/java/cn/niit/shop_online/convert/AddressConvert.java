package cn.niit.shop_online.convert;

import cn.niit.shop_online.entity.UserShippingAddress;
import cn.niit.shop_online.vo.AddressVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author 86187
 * @Date 2023/11/11
 */
@Mapper
public interface AddressConvert {
    AddressConvert INSTANCE = Mappers.getMapper(AddressConvert.class);


    UserShippingAddress convert(AddressVO addressVO);


    List<AddressVO> convertToAddressVOList(List<UserShippingAddress> addressList);


    AddressVO convertToAddressVO(UserShippingAddress userShippingAddress);
}
