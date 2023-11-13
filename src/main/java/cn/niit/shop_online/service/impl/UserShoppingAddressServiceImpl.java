package cn.niit.shop_online.service.impl;

import cn.niit.shop_online.common.exception.ServerException;
import cn.niit.shop_online.convert.AddressConvert;
import cn.niit.shop_online.entity.UserShoppingAddress;
import cn.niit.shop_online.enums.AddressDefaultEnum;
import cn.niit.shop_online.mapper.UserShoppingAddressMapper;
import cn.niit.shop_online.service.UserShoppingAddressService;
import cn.niit.shop_online.vo.AddressVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yaaii
 * @since 2023-11-07
 */
@Service
public class UserShoppingAddressServiceImpl extends ServiceImpl<UserShoppingAddressMapper, UserShoppingAddress> implements UserShoppingAddressService {

//    添加收获地址
    @Override
    public Integer saveShippingAddress(AddressVO addressVO) {
        UserShoppingAddress convert = AddressConvert.INSTANCE.convert(addressVO);
        if(addressVO.getIsDefault()== AddressDefaultEnum.DEFAULT_ADDRESS.getValue()){
            List<UserShoppingAddress> list =baseMapper.selectList(new
                    LambdaQueryWrapper<UserShoppingAddress>().eq(UserShoppingAddress::getIsDefault,
                    AddressDefaultEnum.DEFAULT_ADDRESS.getValue()));
            if(list.size()>0){
                throw new ServerException("已经存在默认地址，请勿重复操作");
            }
        }
        save(convert);
        return convert.getId();
    }
//     修改收货地址
    @Override
    public Integer editShippingAddress(AddressVO addressVO) {
        UserShoppingAddress userShoppingAddress = baseMapper.selectById(addressVO.getId());
        if(userShoppingAddress ==null){
            throw new ServerException("地址不存在");
        }
        if(addressVO.getIsDefault()==AddressDefaultEnum.DEFAULT_ADDRESS.getValue()){
//            查询该用户是否已经存在默认地址
            UserShoppingAddress address=baseMapper.selectOne(new
                    LambdaQueryWrapper<UserShoppingAddress>().eq(UserShoppingAddress::getUserId,
                    addressVO.getUserId()).eq(UserShoppingAddress::getIsDefault,
                    AddressDefaultEnum.DEFAULT_ADDRESS.getValue()));
            System.out.println("--------"+address);
            if(address!=null){
                address.setIsDefault(AddressDefaultEnum.NOT_DEFAULT_ADDRESS.getValue());
                updateById(address);
            }
        }
        UserShoppingAddress address = AddressConvert.INSTANCE.convert(addressVO);
        updateById(address);
        return address.getId();
    }

//    获取地址列表
    @Override
    public List<AddressVO> getList(Integer userId) {
        LambdaQueryWrapper<UserShoppingAddress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserShoppingAddress::getUserId, userId);
//        根据是否为默认地址和创建时间倒序排列
        wrapper.orderByDesc(UserShoppingAddress::getIsDefault);
        wrapper.orderByDesc(UserShoppingAddress::getCreateTime);
        List<UserShoppingAddress> list = baseMapper.selectList(wrapper);
        List<AddressVO> results = AddressConvert.INSTANCE.convertToAddressVOList(list);
        return results;
    }

//    获取地址详情
    @Override
    public AddressVO getAddressInfo(Integer id) {
        UserShoppingAddress userShippingAddress = baseMapper.selectById(id);
        if (userShippingAddress == null) {
            throw new ServerException("地址不存在");
        }
        AddressVO addressVO = AddressConvert.INSTANCE.convertToAddressVO(userShippingAddress);
        return addressVO;
    }

    //  删除收货地址
    @Override
    public void removeShippingAddress(Integer id) {
        removeById(id);
    }



}
