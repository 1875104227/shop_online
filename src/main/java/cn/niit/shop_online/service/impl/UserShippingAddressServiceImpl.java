package cn.niit.shop_online.service.impl;

import cn.niit.shop_online.common.exception.ServerException;
import cn.niit.shop_online.convert.AddressConvert;
import cn.niit.shop_online.convert.GoodsConvert;
import cn.niit.shop_online.entity.UserShippingAddress;
import cn.niit.shop_online.enums.AddressDefaultEnum;
import cn.niit.shop_online.mapper.UserShippingAddressMapper;
import cn.niit.shop_online.service.UserShippingAddressService;
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
public class UserShippingAddressServiceImpl extends ServiceImpl<UserShippingAddressMapper, UserShippingAddress> implements UserShippingAddressService {

//    添加收获地址
    @Override
    public Integer saveShippingAddress(AddressVO addressVO) {
        UserShippingAddress convert = AddressConvert.INSTANCE.convert(addressVO);
        if(addressVO.getIsDefault()== AddressDefaultEnum.DEFAULT_ADDRESS.getValue()){
            List<UserShippingAddress> list =baseMapper.selectList(new
                    LambdaQueryWrapper<UserShippingAddress>().eq(UserShippingAddress::getIsDefault,
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
        UserShippingAddress userShippingAddress = baseMapper.selectById(addressVO.getId());
        if(userShippingAddress==null){
            throw  new ServerException("地址不存在");
        }
        if(addressVO.getIsDefault()==AddressDefaultEnum.DEFAULT_ADDRESS.getValue()){
//            查询该用户是否已经存在默认地址
            UserShippingAddress address=baseMapper.selectOne(new
                    LambdaQueryWrapper<UserShippingAddress>().eq(UserShippingAddress::getUserId,
                    addressVO.getUserId()).eq(UserShippingAddress::getIsDefault,
                    AddressDefaultEnum.DEFAULT_ADDRESS.getValue()));
            if(address!=null){
                address.setIsDefault(AddressDefaultEnum.NOT_DEFAULT_ADDRESS.getValue());
                updateById(address);
            }
        }
        UserShippingAddress address = AddressConvert.INSTANCE.convert(addressVO);
        updateById(address);
        return address.getId();
    }

//    收获地址列表
    @Override
    public List<AddressVO> getList(Integer userId) {
        LambdaQueryWrapper<UserShippingAddress> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(UserShippingAddress::getUserId,userId).orderByDesc(UserShippingAddress::getIsDefault);
        List<UserShippingAddress> list=baseMapper.selectList(new LambdaQueryWrapper<UserShippingAddress>().eq(
                UserShippingAddress::getUserId,userId
        ));
        List<AddressVO> addressVOList=AddressConvert.INSTANCE.convertToAddressVOList(list);
        return addressVOList;
    }

    @Override
    public AddressVO getAddressDetail(Integer id) {
        UserShippingAddress address = baseMapper.selectById(id);
        if(address==null){
            throw new ServerException("商品不存在");
        }
        AddressVO addressVO = AddressConvert.INSTANCE.convertToAddressVO(address);
      
        return null;
    }
//
}
