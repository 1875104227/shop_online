package cn.niit.shop_online.convert;

import cn.niit.shop_online.entity.UserShoppingAddress;
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


    UserShoppingAddress convert(AddressVO addressVO);


    List<AddressVO> convertToAddressVOList(List<UserShoppingAddress> addressList);


    AddressVO convertToAddressVO(UserShoppingAddress userShoppingAddress);
}
