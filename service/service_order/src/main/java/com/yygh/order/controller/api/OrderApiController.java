package com.yygh.order.controller.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yygh.common.result.Result;
import com.yygh.common.utils.AuthContextUtil;
import com.yygh.enums.OrderStatusEnum;
import com.yygh.model.order.OrderInfo;
import com.yygh.order.service.OrderService;
import com.yygh.vo.order.OrderQueryVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "根据订单ID查询订单详情")
    @GetMapping("/auth/getOrders/{orderId}")
    public Result getOrders(@PathVariable("orderId") String orderId) {
        OrderInfo orderInfo = orderService.getOrders(orderId);
        return Result.success(orderInfo);
    }

    @Operation(summary = "订单列表")
    @GetMapping("/auth")
    public Result list(OrderQueryVo orderQueryVo) {
        orderQueryVo.setUserId(AuthContextUtil.getUserId());
        IPage<OrderInfo> list = orderService.list(orderQueryVo);
        return Result.success(list);
    }

    @Operation(summary = "全部订单列表")
    @GetMapping("/auth/allList")
    public Result allList(OrderQueryVo orderQueryVo) {
        IPage<OrderInfo> list = orderService.list(orderQueryVo);
        return Result.success(list);
    }

    @Operation(summary = "获取订单状态")
    @GetMapping("/auth/getStatusList")
    public Result getStatusList() {
        return Result.success(OrderStatusEnum.getStatusList());
    }

}
