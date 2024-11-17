package com.yygh.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yygh.model.order.OrderInfo;
import com.yygh.vo.order.OrderQueryVo;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.order.service
 * @Description
 * @date 2024-11-07 22:34
 */
public interface OrderService extends IService<OrderInfo> {
    Long saveOrder(String scheduleId, Long patientId);

    /**
     * 根据订单ID查询订单详情
     *
     * @param orderId
     * @return
     */
    OrderInfo getOrders(String orderId);

    IPage<OrderInfo> list(OrderQueryVo orderQueryVo);
}
