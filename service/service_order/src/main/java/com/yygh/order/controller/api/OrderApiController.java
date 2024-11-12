package com.yygh.order.controller.api;

import com.yygh.common.result.Result;
import com.yygh.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.order.api
 * @Description
 * @date 2024-11-07 22:32
 */
@Tag(name = "订单接口")
@RestController
@RequestMapping("/api/order/orderInfo")
@AllArgsConstructor
public class OrderApiController {
    private OrderService orderService;


    @Operation(summary = "创建订单")
    @PostMapping("/auth/submitOrder/{scheduleId}/{patientId}")

    public Result saveOrders(@PathVariable("scheduleId") String scheduleId, @PathVariable("patientId") Long patientId) {
        Long orderId = orderService.saveOrder(scheduleId, patientId);
        return Result.success(orderId);

    }

}