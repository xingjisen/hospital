import request from "@/utils/axios";
import qs from "qs";
import axios from "axios";

const api_name = "/api/hosp/site";

/** 查询医院列表*/
export const getList = (param) => {
  if (process.server) {
    console.log("运行在服务端");
    const baseUrl = `http://${process.env.HOST}:${process.env.PORT}${process.env.BASE_API}`;
    return axios.get(`${baseUrl}${api_name}/list?${qs.stringify(param)}`);
  } else {
    console.log("运行在客户端");
    return request.get(`${api_name}/list?${qs.stringify(param)}`);
  }
};

/** 根据医院名称模糊查询*/
export const getByHospname = (hospname) => {
  return request.get(`${api_name}/findByName/${hospname}`);
};

/** 根据医院编号查询科室信息*/
export const getDept = (hoscode) => {
  return request.get(`${api_name}/dept/${hoscode}`);
};
/** 医院预约挂号详情信息*/
export const getHospDetail = (hoscode) => {
  return request.get(`${api_name}/hospDetail/${hoscode}`);
};
/** 获取可预约排班信息*/
export const bookingScheduleRule = (query) => {
  return request.get(`${api_name}/auth/getBookingScheduleRule?${qs.stringify(query)}`);
};

/** 获取排班信息*/
export const findScheduleList = (query) => {
  return request.get(`${api_name}/auth/findScheduleList?${qs.stringify(query)}`);
};

/** 根据排班ID获取排版数据*/
export const getSchedule = (id) => {
  return request.get(`${api_name}/getSchedule/${id}`);
};
