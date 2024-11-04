import request from '@/axios'
import qs from "qs";

const api_name = "/admin/user"

/** 获取用户列表 */
export const getUserList = (id) => {
    return request.get({url: `${api_name}?${qs.stringify(id)}`})
}
/** 状态解锁锁定 */
export const lockUser = (id, status) => {
    return request.put({url: `${api_name}/lock/${id}/${status}`})
}

/** 用户详情 */
export const detail = (id) => {
    return request.put({url: `${api_name}/detail/${id}`})
}

/** 认证审批 */
export const approval = (id, authStatus) => {
    return request.put({url: `${api_name}/approval/${id}/${authStatus}`})
}
