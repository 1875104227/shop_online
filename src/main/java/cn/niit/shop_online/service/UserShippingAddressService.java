package cn.niit.shop_online.service;

import cn.niit.shop_online.entity.UserShippingAddress;
import cn.niit.shop_online.vo.AddressVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yaaii
 * @since 2023-11-07
 */
public interface UserShippingAddressService extends IService<UserShippingAddress> {
    Integer saveShippingAddress(AddressVO addressVO);
    Integer editShippingAddress(AddressVO addressVO);
    List<AddressVO> getList(Integer userId);
}
