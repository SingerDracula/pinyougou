package com.pinyougou.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.cart.Cart;
import com.pinyougou.common.utils.IdWorker;
import com.pinyougou.mapper.OrderItemMapper;
import com.pinyougou.mapper.OrderMapper;
import com.pinyougou.mapper.PayLogMapper;
import com.pinyougou.pojo.Order;
import com.pinyougou.pojo.OrderItem;
import com.pinyougou.pojo.PayLog;
import com.pinyougou.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service(interfaceName = "com.pinyougou.service.OrderService")
public class OrderServiceImpl implements OrderService {
    @Autowired
    private PayLogMapper payLogMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public void save(Order order) {
        List<Cart> cartList = (List<Cart>) redisTemplate.boundValueOps("cart_" + order.getUserId()).get();
        ArrayList<String> orderList = new ArrayList<>();
        double totalMoney = 0;
        for (Cart cart : cartList) {
            long nextId = idWorker.nextId();
            orderList.add(String.valueOf(nextId));
            Order order1 = new Order();
            order1.setOrderId(nextId);
            order1.setUserId(order.getSellerId());
            order1.setPaymentType(order.getPaymentType());
            order1.setStatus("1");
            order1.setCreateTime(new Date());
            order1.setUpdateTime(order1.getCreateTime());
            order1.setReceiverAreaName(order.getReceiverAreaName());
            order1.setReceiverMobile(order.getReceiverMobile());
            order1.setReceiver(order.getReceiver());
            order1.setSourceType(order.getSourceType());
            order1.setSellerId(cart.getSellerId());

            double money = 0;
            for (OrderItem orderItem : cart.getOrderItems()) {
                orderItem.setId(idWorker.nextId());
                orderItem.setOrderId(nextId);
                money += orderItem.getTotalFee().doubleValue();
                orderItemMapper.insertSelective(orderItem);
            }

            totalMoney += money;
            order1.setPayment(new BigDecimal(money));
            orderMapper.insertSelective(order1);


        }

        if("1".equals(order.getPaymentType())){
            PayLog payLog = new PayLog();
            payLog.setOutTradeNo(String.valueOf(idWorker.nextId()));
            payLog.setCreateTime(new Date());
            payLog.setTotalFee((long) (totalMoney*100));
            payLog.setUserId(order.getSellerId());
            payLog.setTradeState("0");
            payLog.setPayType("1");
            payLog.setOrderList(orderList.toString().replaceAll("\\[","").replaceAll("]","").replaceAll(" ",""));
            payLogMapper.insertSelective(payLog);
            redisTemplate.boundValueOps("payLog_"+order.getUserId()).set(payLog);
        }
        redisTemplate.delete("cart_"+order.getUserId());
    }

    @Override
    public void update(Order order) {

    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    @Override
    public Order findOne(Serializable id) {
        return null;
    }

    @Override
    public List<Order> findAll() {
        return null;
    }

    @Override
    public List<Order> findByPage(Order order, int page, int rows) {
        return null;
    }

    @Override
    public PayLog getPayLogByRedis(String user) {
        return (PayLog) redisTemplate.boundValueOps("payLog_"+user).get();
    }

    @Override
    public void updateStatus(String outTradeNo, String transaction_id) {
        PayLog payLog = payLogMapper.selectByPrimaryKey(outTradeNo);
        payLog.setPayTime(new Date());
        payLog.setTradeState("1");
        payLog.setTransactionId(String.valueOf(transaction_id));
        payLogMapper.updateByPrimaryKey(payLog);

        String[] orderList = payLog.getOrderList().split(",");
        for (String id : orderList) {
            Order order = new Order();
            order.setOrderId(Long.valueOf(id));
            order.setPaymentTime(new Date());
            order.setStatus("2");
            orderMapper.updateByPrimaryKeySelective(order);
        }

        redisTemplate.delete("payLog_"+payLog.getUserId());

    }

}
