import request from "@/utils/axios";

const api_name = "/api/sms";

export const sendCode = (mobile) => {
  return request.get(`${api_name}/send/${mobile}`);
};
