package com.pinyougou.seckill.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.common.utils.IdWorker;
import com.pinyougou.mapper.PayLogMapper;
import com.pinyougou.mapper.SeckillGoodsMapper;
import com.pinyougou.mapper.SeckillOrderMapper;
import com.pinyougou.pojo.PayLog;
import com.pinyougou.pojo.SeckillGoods;
import com.pinyougou.pojo.SeckillOrder;
import com.pinyougou.service.SeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service(interfaceName = "com.pinyougou.service.SeckillOrderService")
@Transactional
public class SeckillOrderServiceImpl implements SeckillOrderService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private PayLogMapper payLogMapper;
    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;
    @Override
    public void save(SeckillOrder seckillOrder) {

    }

    @Override
    public void update(SeckillOrder seckillOrder) {

    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    @Override
    public SeckillOrder findOne(Serializable id) {
        return null;
    }

    @Override
    public List<SeckillOrder> findAll() {
        return null;
    }

    @Override
    public List<SeckillOrder> findByPage(SeckillOrder seckillOrder, int page, int rows) {
        return null;
    }

    @Override
    public void submitOrder(Long id, String username) {
        SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps("seckillGoodsList").get(id);

        if(seckillGoods != null && seckillGoods.getStockCount()>0){
            seckillGoods.setStockCount(seckillGoods.getStockCount()-1);

            if(seckillGoods.getStockCount() <= 0){
                redisTemplate.boundHashOps("seckillGoodsList").delete(id);
            }else {
                redisTemplate.boundHashOps("seckillGoodsList").put(id,seckillGoods);
            }

            SeckillOrder seckillOrder = new SeckillOrder();
            long orderId = new IdWorker().nextId();
            seckillOrder.setId(orderId);
            seckillOrder.setSeckillId(seckillGoods.getId());
            seckillOrder.setMoney(seckillGoods.getCostPrice());
            seckillOrder.setUserId(username);
            seckillOrder.setSellerId(seckillGoods.getSellerId());
            seckillOrder.setCreateTime(new Date());
            seckillOrder.setStatus("0");

            redisTemplate.boundHashOps("seckillOrderList").put(orderId,seckillOrder);

            PayLog payLog = new PayLog();
            payLog.setOutTradeNo(String.valueOf(new IdWorker().nextId()));
            payLog.setCreateTime(new Date());

            payLog.setTotalFee(Long.valueOf((seckillOrder.getMoney().setScale(0,BigDecimal.ROUND_UP)).toString()));


            payLog.setUserId(username);
            payLog.setTradeState("0");
            payLog.setPayType("1");
            payLog.setOrderList(String.valueOf(seckillGoods.getId()));
            payLogMapper.insertSelective(payLog);
            redisTemplate.boundValueOps("payLog_"+username).set(payLog);

        }else {
            throw new RuntimeException();
        }
    }

    @Override
    public PayLog getPayLogByRedis(String user) {
        PayLog payLog = (PayLog) redisTemplate.boundValueOps("payLog_" + user).get();
        return payLog;
    }

    @Override
    public void updateStatus(String outTradeNo, String transaction_id) {
        PayLog payLog = payLogMapper.selectByPrimaryKey(outTradeNo);
        payLog.setPayTime(new Date());
        payLog.setTradeState("1");
        payLog.setTransactionId(String.valueOf(transaction_id));
        payLogMapper.updateByPrimaryKey(payLog);

        String orderList = payLog.getOrderList();

        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.boundHashOps("seckillOrderList").get(Long.valueOf(orderList));
        seckillOrder.setPayTime(new Date());
        seckillOrder.setTransactionId(transaction_id);
        seckillOrderMapper.insertSelective(seckillOrder);


        redisTemplate.delete("payLog_"+payLog.getUserId());
        redisTemplate.boundHashOps("seckillOrderList").delete(Long.valueOf(orderList));
    }

    @Override
    public List<SeckillOrder> findTimeOutOrder() {
        ArrayList<SeckillOrder> timeoutSecOrder = new ArrayList<>();
        List<SeckillOrder> seckillOrderList = redisTemplate.boundHashOps("seckillOrderList").values();
        if (seckillOrderList != null && seckillOrderList.size()>0){
            for (SeckillOrder seckillOrder : seckillOrderList) {
                Long time = new Date().getTime() - (5*60*1000);
                if(seckillOrder.getCreateTime().getTime()<time){
                    timeoutSecOrder.add(seckillOrder);
                }
            }
        }

        return timeoutSecOrder;
    }

    @Override
    public void deleteFromRedis(SeckillOrder seckillOrder) {

        redisTemplate.boundHashOps("seckillOrderList").delete(seckillOrder.getId());

        SeckillGoods goods = (SeckillGoods) redisTemplate.boundHashOps("seckillGoodsList").get(seckillOrder.getSeckillId());
        if(goods != null){
            goods.setStockCount(goods.getStockCount()+1);
        }else {
            goods = seckillGoodsMapper.selectByPrimaryKey(seckillOrder.getId());
            goods.setStockCount(1);

        }
        redisTemplate.boundHashOps("seckillGoodsList").put(goods.getId(),goods);

    }
}
