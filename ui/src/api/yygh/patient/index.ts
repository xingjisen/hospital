import request from '@/axios'

const api_name = `/api/user/patient`;

/** 获取就诊人列表 */
export const list = () => {
    return request.get({url: `${api_name}/auth/allList`});
}
