import request from '@/axios'
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
