import request from "@/utils/axios";

const api_name = `/api/user`;

export const login = (userInfo) => {
  return request.post(`${api_name}/login`, userInfo);
};

/**
 * 完善用户信息
 * @param userInfo
 * @returns {Promise<AxiosResponse<any>>}
 */
export const userAuth = (data) => {
  return request.post(`${api_name}/auth/userAuth`, data);
};

/**
 * 根据ID获取用户信息
 * @param userInfo
 * @returns {Promise<AxiosResponse<any>>}
 */
export const getUserInfo = () => {
  return request.get(`${api_name}/auth/getUserInfo`);
};
