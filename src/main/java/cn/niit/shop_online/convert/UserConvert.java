package cn.niit.shop_online.convert;

import cn.niit.shop_online.entity.User;
import cn.niit.shop_online.vo.LoginResultVO;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author 86187
 * @Date 2023/11/11
 */
@Mapper
public interface UserConvert {
    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);


    LoginResultVO convertToLoginResultVO(User user);
}
