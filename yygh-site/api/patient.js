import request from "@/utils/axios";
import * as qs from "qs";

const api_name = `/api/user/patient`;


/**
 * 获取就诊人列表
 * @returns {Promise<AxiosResponse<any>>}
 */
export const list = (query) => {
  return request.get(`${api_name}/auth/list?${qs.stringify(query)}`);
};

/**
 * 添加就诊人
 * @returns {Promise<AxiosResponse<any>>}
 */
export const add = (data) => {
  return request.post(`${api_name}/auth/add`, data);
};


/**
 * 根据id获取就诊人信息
 * @returns {Promise<AxiosResponse<any>>}
 */
export const getById = (id) => {
  return request.get(`${api_name}/auth/getById/${id}`);
};


/**
 * 修改就诊人
 * @returns {Promise<AxiosResponse<any>>}
 */
export const put = (data) => {
  return request.put(`${api_name}/auth/put`, data);
};


/**
 * 删除就诊人
 * @returns {Promise<AxiosResponse<any>>}
 */
export const del = (id) => {
  return request.delete(`${api_name}/auth/del/${id}`);
};
