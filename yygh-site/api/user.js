import request from "@/utils/axios";

const api_name = `/api/user`;

export const login = (userInfo) => {
  return request.post(`${api_name}/login`, userInfo);
};
