import request from "@/utils/axios";

const api_name = `/api/ucenter/wx`;

export const getWeixinLoginParam = (userInfo) => {
  return request.get(`${api_name}/getLoginParam`);
};
