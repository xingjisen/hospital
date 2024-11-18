import request from '@/axios'
import qs from "qs";

const api_name = "/api/order/orderInfo"

/** 获取全部订单列表 */
export const getOrderAllList = (query) => {
    return request.get({url: `${api_name}/auth/allList?${qs.stringify(query)}`})
}
/** 获取订单状态 */
export const getStatusList = () => {
    return request.get({url: `${api_name}/auth/getStatusList`})
}

// 根据订单ID查询订单详情
export const getOrders = (orderId) => {
    return request.get({url: `${api_name}/auth/getOrders/${orderId}`});
};
