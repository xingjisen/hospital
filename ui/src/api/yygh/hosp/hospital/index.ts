import request from '@/axios'
import qs from "qs";
// import type {getHospList} from './types'
// : Promise<IResponse<getHospList[]>>
/** 获取 */
export const getHospList = (data) => {
    return request.post({url: '/admin/hosp/hospitalSet/findPage', data})
}
/** 删除 */
export const delHospList = (ids) => {
    // @ts-ignore
    return request.delete({url: '/admin/hosp/hospitalSet/dels', data: ids})
}
/** 状态解锁锁定 */
export const lockHosp = (id, status) => {
    return request.put({url: `/admin/hosp/hospitalSet/lock/${id}/${status}`})
}

/** 修改 */
export const putHosp = (data) => {
    return request.put({url: '/admin/hosp/hospitalSet', data})
}

/** 新增 */
export const addHosp = (data) => {
    return request.post({url: '/admin/hosp/hospitalSet/add', data})
}
/** 详情 */
export const detailHosp = (id) => {
    return request.get({url: `/admin/hosp/hospitalSet/${id}`})
}

/** 根据dictCode下级内容 */
export const findByDictCode = (dictCode) => {
    return request.get({url: `/admin/cmn/dict/findByDictCode/${dictCode}`})
}
/** 根据id查询子节点内容 */
export const listByParentId = (id) => {
    return request.get({url: `/admin/cmn/dict/${id}`})
}
/** 查询医院列表 */
export const getHospitalList = (hospitalQueryVo) => {
    return request.get({url: `/admin/hosp/hospital/list?${qs.stringify(hospitalQueryVo)}`})
}
/** 更新医院状态 */
export const updateStatus = (id, status) => {
    return request.put({url: `/admin/hosp/hospital/updateStatus/${id}/${status}`})
}
/** 获取详情信息 */
export const showDetail = (id) => {
    return request.get({url: `/admin/hosp/hospital/showDetail/${id}`})
}

/** 获取科室信息 */
export const getDeptTree = (hoscode) => {
    return request.get({url: `/admin/hosp/depart/getDeptTree/${hoscode}`})
}
/** 获取排班信息 */
export const getScheduleRule = (param) => {
    return request.get({url: `/admin/hosp/schedule/getScheduleRule?${qs.stringify(param)}`})
}
/** 获取排班详细信息 */
export const getDetailSchedule = (hoscode, depcode, workDate) => {
    return request.get({url: `/admin/hosp/schedule/getDetailSchedule/${hoscode}/${depcode}/${workDate}`})
}
