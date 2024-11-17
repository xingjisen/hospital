package com.yygh.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yygh.model.order.OrderInfo;
import com.yygh.vo.order.OrderQueryVo;
import org.apache.ibatis.annotations.Param;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.order.mapper
 * @Description
 * @date 2024-11-07 22:36
 */
public interface OrderMapper extends BaseMapper<OrderInfo> {
    IPage<OrderInfo> list(IPage iPage, @Param("orderQueryVo") OrderQueryVo orderQueryVo);
}
