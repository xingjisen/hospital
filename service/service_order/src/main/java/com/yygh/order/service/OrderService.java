package com.yygh.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yygh.model.order.OrderInfo;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.order.service
 * @Description
 * @date 2024-11-07 22:34
 */
public interface OrderService extends IService<OrderInfo> {
    Long saveOrder(String scheduleId, Long patientId);
}
