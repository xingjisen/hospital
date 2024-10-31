import request from "@/utils/axios";

const api_name = `/api/oss/file`;


/**
 * 根据ID获取用户信息
 * @param userInfo
 * @returns {Promise<AxiosResponse<any>>}
 */
export const upload = (formData) => {
  return request({
    url: `${api_name}/upload`,
    method: "post",
    // headers: {
    //   "Content-Type": "application/x-www-form-urlencoded"
    // },
    data: formData
  });
};
