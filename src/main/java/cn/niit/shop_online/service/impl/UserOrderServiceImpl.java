package cn.niit.shop_online.service.impl;

import cn.niit.shop_online.common.exception.ServerException;
import cn.niit.shop_online.convert.UserAddressConvert;
import cn.niit.shop_online.convert.UserOrderDetailConvert;
import cn.niit.shop_online.entity.*;
import cn.niit.shop_online.enums.OrderStatusEnum;
import cn.niit.shop_online.mapper.*;
import cn.niit.shop_online.service.UserOrderGoodsService;
import cn.niit.shop_online.service.UserOrderService;
import cn.niit.shop_online.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yaaii
 * @since 2023-11-07
 */
@Service
public class UserOrderServiceImpl extends ServiceImpl<UserOrderMapper, UserOrder> implements UserOrderService {
    @Autowired
    private UserShoppingCartMapper userShoppingCartMapper;
    @Autowired
   private UserOrderGoodsMapper userOrderGoodsMapper;
    @Autowired
    private UserShoppingAddressMapper userShoppingAddressMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private UserOrderGoodsService userOrderGoodsService;
    private ScheduledExecutorService executorService= Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> cancelTask;

    public UserOrderServiceImpl() {
    }

    @Async
    public void scheduOrderCancel(UserOrder userOrder){
        cancelTask=executorService.schedule(()->{
            if (userOrder.getStatus()== OrderStatusEnum.WAITING_FOR_PAYMENT.getValue()){
                userOrder.setStatus(OrderStatusEnum.CANCELLED.getValue());
                baseMapper.updateById(userOrder);
            }
        },30, TimeUnit.MINUTES);
    }
    public void cancelScheduledTask(){
        if(cancelTask !=null&&!cancelTask.isDone()){
            cancelTask.cancel(true);
        }
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addGoodsOrder(UserOrderVO orderVO) {
//            1、声明订单总支付费用、总运费、总购买件数
            BigDecimal totalPrice = new BigDecimal(0);
            Integer totalCount =0 ;
            BigDecimal totalFreight = new BigDecimal( 0);
            UserOrder userOrder = new UserOrder( );
        userOrder.setUserId(orderVO.getUserId( ));
        userOrder.setAddressId( orderVO.getAddressId( ));
//        订单编号使用uuid随机生成不重复的编号
        userOrder.setOrderNumber ( UUID.randomUUID( ).toString());
        userOrder.setDeliveryTimeType(orderVO.getDeliveryType());
//        提交订单黑认状态为待付款
        userOrder.setStatus(OrderStatusEnum.WAITING_FOR_PAYMENT.getValue( ));
        if (orderVO.getBuyerMessage( ) != null) {
            userOrder.setBuyerMessage(orderVO.getBuyerMessage( ));
        }
       userOrder.setPayType( orderVO. getPayType( ));
        userOrder.setPayChannel(orderVO.getPayChannel());
        baseMapper.insert(userOrder);
//        异步取消创建的订单，如果订单创建30分钟后用户没有付款，修改订单状态为取消
       scheduOrderCancel(userOrder);
        List<UserOrderGoods> orderGoodsList = new ArrayList<>( );
//        遍历用户购买的商品列表,订单-商品表批量添加数据
        for (com.soft2242.shop.query.OrderGoodsQuery goodsVO:orderVO.getGoods( )) {
            Goods goods = goodsMapper.selectById( goodsVO.getId());
            if (goodsVO.getCount() > goods.getInventory()) {
                throw new ServerException( goods.getName()+"库存数量不足" );
            }
            UserOrderGoods userOrderGoods = new UserOrderGoods( );
            userOrderGoods.setGoodsId( goods.getId( ));
            userOrderGoods.setName(goods.getName());
            userOrderGoods.setCover(goods.getCover());
            userOrderGoods.setOrderId(userOrder.getId());
            userOrderGoods.setCount(goodsVO.getCount());
            userOrderGoods.setAttrsText(goodsVO.getSkus());
            userOrderGoods.setFreight(goods.getFreight());
            userOrderGoods.setPrice(goods.getPrice());
            //计算总付款金额，使用BigDecimal，避免精度缺失
            BigDecimal freight = new BigDecimal(userOrderGoods.getFreight().toString());
            BigDecimal goodsPrice = new BigDecimal(userOrderGoods.getPrice().toString());
            BigDecimal count = new BigDecimal(userOrderGoods.getCount().toString());
//          减库存
            goods.setInventory(goods.getInventory()-goodsVO.getCount());
//           增加销量
            goods.setSalesCount(goodsVO.getCount());
            BigDecimal price = goodsPrice.multiply(count).add(freight);
            totalPrice = totalPrice.add(price);
            totalCount +=goodsVO.getCount();
            totalFreight = totalFreight.add(freight);
            orderGoodsList.add( userOrderGoods);
            goodsMapper.updateById(goods);
        }
        userOrderGoodsService.batchUserOrderGoods(orderGoodsList);
        userOrder.setTotalPrice(totalPrice.doubleValue( ));
        userOrder.setTotalCount(totalCount);
        userOrder.setTotalFreight(totalFreight.doubleValue( ));
        baseMapper.updateById(userOrder);
        return userOrder.getId();
    }

    @Override
    public OrderDetailVO getOrderDetail(Integer id) {
//        订单信息
        UserOrder userOrder=baseMapper.selectById(id);
        if(userOrder==null){
            throw new ServerException("订单信息不存在");
        }
        OrderDetailVO orderDetailVO= UserOrderDetailConvert.INSTANCE.convertToOrderDetailVO(userOrder);
        orderDetailVO.setTotalMoney(userOrder.getTotalPrice());
//        收货信息
        UserShoppingAddress userShoppingAddress=userShoppingAddressMapper.selectById(userOrder.getAddressId());
        if(userShoppingAddress==null){
            throw new ServerException("收货地址信息不存在");
        }
        orderDetailVO.setReceiverContact(userShoppingAddress.getReceiver());
        orderDetailVO.setReceiverMobile(userShoppingAddress.getContact());
        orderDetailVO.setReceiverAddress(userShoppingAddress.getAddress());
//        商品集合
        List<UserOrderGoods> list=userOrderGoodsMapper.selectList(new
                LambdaQueryWrapper<UserOrderGoods>().eq(UserOrderGoods::getOrderId,id));
        orderDetailVO.setSkus(list);
//        订单截至订单创建30分钟后
        orderDetailVO.setPayLatestTime(userOrder.getCreateTime().plusMinutes(30));
        if(orderDetailVO.getPayLatestTime().isAfter(LocalDateTime.now())){
            Duration duration=Duration.between(LocalDateTime.now(),orderDetailVO.getPayLatestTime());
//            倒计时秒数
            orderDetailVO.setCountdown(duration.toMillisPart());
        }
        return orderDetailVO;
    }

    @Override
    public SubmitOrderVO getPreOrderDetail(Integer userId) {
        SubmitOrderVO submitOrderVO = new SubmitOrderVO();
        //1.查询用户购物车中选中的商品列表，如果为空直接返回null
        List<UserShoppingCart> cartList = userShoppingCartMapper.selectList(
                new LambdaQueryWrapper<UserShoppingCart>().eq(UserShoppingCart::getUserId,
                        userId).eq(UserShoppingCart::getSelected, true));
        if (cartList.size() == 0) {
            return null;
        }
        //2.查询用户收货地址列表
        List<UserAddressVO> addressVOList = getAddressListByUserId(userId, null);

        //3.声明订单应付款，总运费金额
        BigDecimal totalPrice = new BigDecimal(0);
        Integer totalCount = 0;
        BigDecimal totalPayPrice = new BigDecimal(0);
        BigDecimal totalFreigt = new BigDecimal(0);
//        4，查询商品信息并计算每个选购商品的总费用
        List<UserOrderGoodsVO> goodList=new ArrayList<>();
        for(UserShoppingCart shoppingCart:cartList){
            Goods goods =goodsMapper.selectById(shoppingCart.getGoodsId());
            UserOrderGoodsVO userOrderGoodsVO=new UserOrderGoodsVO();
            userOrderGoodsVO.setId(goods.getId());
            userOrderGoodsVO.setName(goods.getName());
            userOrderGoodsVO.setPicture(goods.getCover());
            userOrderGoodsVO.setCount(shoppingCart.getCount());
            userOrderGoodsVO.setAttrsText(shoppingCart.getAttrsText());
            userOrderGoodsVO.setPrice(goods.getOldPrice());
            userOrderGoodsVO.setPayPrice(goods.getPrice());
            userOrderGoodsVO.setTotalPrice(goods.getFreight()+goods.getPrice()*shoppingCart.getCount());
            userOrderGoodsVO.setTotalPayPrice(userOrderGoodsVO.getTotalPrice());

            BigDecimal freight=new BigDecimal(goods.getFreight().toString());
            BigDecimal goodsPrice=new BigDecimal(goods.getPrice().toString());
            BigDecimal count=new BigDecimal(shoppingCart.getCount().toString());
            BigDecimal price=goodsPrice.multiply(count).add(freight);

            totalPrice = totalPrice.add(price);
            totalCount+=userOrderGoodsVO.getCount();
            totalPayPrice=totalPayPrice.add(new BigDecimal(userOrderGoodsVO.getTotalPayPrice().toString()));
            totalFreigt=totalFreigt.add(freight);
            goodList.add(userOrderGoodsVO);
        }
        //5.费用综合信息
        OrderInfoVO orderInfoVO=new OrderInfoVO();
        orderInfoVO.setGoodsCount(totalCount);
        orderInfoVO.setTotalPayPrice(totalPayPrice.doubleValue());
        orderInfoVO.setTotalPrice(totalPrice.doubleValue());
        orderInfoVO.setPostFee(totalFreigt.doubleValue());
        submitOrderVO.setUserAddresses(addressVOList);
        submitOrderVO.setGoods(goodList);
        submitOrderVO.setSummary(orderInfoVO);
        return submitOrderVO;
    }

    @Override
    public List<UserAddressVO> getAddressListByUserId(Integer userId, Integer addressId) {
//        根据用户id 查询该用户的收货地址
    List<UserShoppingAddress> list=userShoppingAddressMapper.selectList(new
            LambdaQueryWrapper<UserShoppingAddress>().eq(UserShoppingAddress::getUserId,userId));
    UserShoppingAddress userShoppingAddress=null;
    UserAddressVO userAddressVO;
    if(list.size()==0){
        return null;
    }
//    如果用户已经有选中的地址，将选中的地址是否选中属性设置为true
        if(addressId!=null){
            userShoppingAddress=list.stream().filter(item->
                    item.getId().equals(addressId)).collect(Collectors.toList()).get(0);
            list.remove(userShoppingAddress);
        }
        List<UserAddressVO> addressList= UserAddressConvert.INSTANCE.convertToUserAddressVOList(list);
        if(userShoppingAddress!=null){
            userAddressVO=UserAddressConvert.INSTANCE.convertToUserAddressVO(userShoppingAddress);
            userAddressVO.setSelected(true);
            addressList.add(userAddressVO);
        }

        return addressList;
    }
}
