import request from "@/utils/axios";
import qs from "qs";

const api_name = "/api/order/orderInfo";

// 创建订单
export const saveOrder = (scheduleId, patientId) => {
  return request.post(`${api_name}/auth/submitOrder/${scheduleId}/${patientId}`);
};
// 根据订单ID查询订单详情
export const getOrders = (orderId) => {
  return request.get(`${api_name}/auth/getOrders/${orderId}`);
};

// 订单列表
export const getOrderList = (query) => {
  return request.get(`${api_name}/auth?${qs.stringify(query)}`);
};
// 获取订单状态
export const getStatusList = () => {
  return request.get(`${api_name}/auth/getStatusList`);
};
