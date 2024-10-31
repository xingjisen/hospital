// 在你的组件文件中引入 Axios
import axios from "axios";
import { Message } from "element-ui";
import cookie from "js-cookie";

const instance = axios.create({
  // baseURL:'http://localhost:9999',
  baseURL: process.env.BASE_API,
  timeout: 15000 // 请求超时时间
});


// instance.defaults.baseURL = process.env.CLIENT ? `${process.env.HOST}:${process.env.PORT}/${process.env.BASE_API}` : process.env.BASE_API;
// http请求拦截器
instance.interceptors.request.use(config => {
    // console.log("Request Config:", config);
    if (cookie.get("token")) {
      config.headers["yyghToken"] = `Bearer ${cookie.get("token")}`;
    }
    console.log("config", config);
    return config;
  },
  err => {
    return Promise.reject(err);
  }
);
// http相应拦截器
instance.interceptors.response.use(response => {
    console.log("响应内容", response);
    if (response.data.code === 208) {
      // 未登录
      loginEvent.$emit("loginDialogEvent");
      return;
    } else {
      if (response.data.code !== 200) {
        console.error("错误", response);
        Message({
          message: response.data.message,
          type: "error"
        });
      } else {
        return response.data;
      }
    }
  },
  err => {
    return Promise.reject(err);
  }
);
export default instance;
