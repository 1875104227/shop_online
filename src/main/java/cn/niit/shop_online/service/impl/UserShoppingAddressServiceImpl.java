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
            throw  new ServerException("地址不存在");
        }
        if(addressVO.getIsDefault()==AddressDefaultEnum.DEFAULT_ADDRESS.getValue()){
//            查询该用户是否已经存在默认地址
            UserShoppingAddress address=baseMapper.selectOne(new
                    LambdaQueryWrapper<UserShoppingAddress>().eq(UserShoppingAddress::getUserId,
                    addressVO.getUserId()).eq(UserShoppingAddress::getIsDefault,
                    AddressDefaultEnum.DEFAULT_ADDRESS.getValue()));
            if(address!=null){
                address.setIsDefault(AddressDefaultEnum.NOT_DEFAULT_ADDRESS.getValue());
                updateById(address);
            }
        }
        UserShoppingAddress address = AddressConvert.INSTANCE.convert(addressVO);
        updateById(address);
        return address.getId();
    }

//    收获地址列表
    @Override
    public List<AddressVO> getShoppingAddressList(Integer userId) {
        LambdaQueryWrapper<UserShoppingAddress> queryWrapper=new LambdaQueryWrapper<>();
        List<UserShoppingAddress> list=baseMapper.selectList(queryWrapper.eq(
                UserShoppingAddress::getUserId,userId).orderByDesc(UserShoppingAddress::getIsDefault));
        List<AddressVO> addressVOList=AddressConvert.INSTANCE.convertToAddressVOList(list);
        return addressVOList;
    }
//  获取地址详情
    @Override
    public AddressVO getAddressDetail(Integer id) {
        UserShoppingAddress address = baseMapper.selectById(id);
        if(address==null){
            throw new ServerException("地址不存在");
        }
        UserShoppingAddress userShoppingAddress = baseMapper.selectOne(new LambdaQueryWrapper<UserShoppingAddress>().eq(
                UserShoppingAddress::getId, address.getId()));
        AddressVO addressVO = AddressConvert.INSTANCE.convertToAddressVO(userShoppingAddress);

        return addressVO;
    }
    //  删除收货地址
    @Override
    public void deleteShoppingAddress(Integer id) {
//        逻辑删除，地址的delete_flag 为 1
        UserShoppingAddress address = baseMapper.selectById(id);
        if(address==null){
            throw new ServerException("地址不存在");
        }
        UserShoppingAddress userShoppingAddress =baseMapper.selectOne(new LambdaQueryWrapper<UserShoppingAddress>().eq(
                UserShoppingAddress::getId,address.getId()
        ));
        if(userShoppingAddress.getIsDefault()==AddressDefaultEnum.DEFAULT_ADDRESS.getValue()){
            throw new ServerException("默认地址不能删除");
        }else {
            userShoppingAddress.setDeleteFlag(AddressDefaultEnum.DEFAULT_ADDRESS.getValue());
            updateById(userShoppingAddress);
        }

    }



}
