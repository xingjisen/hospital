import request from '@/axios'
import type {UserType} from './types'

interface RoleParams {
    roleName: string
}

const api_name = "/admin/user/auth";

export const loginApi = (data: UserType): Promise<IResponse<UserType>> => {
    return request.post({url: `${api_name}/login`, data})
}
/*获取用户信息*/
export const getDetail = (id: String | number): Promise<IResponse<UserType>> => {
    return request.post({url: `${api_name}/detail/${id}`})
}

export const loginOutApi = (): Promise<IResponse> => {
    return request.get({url: '/mock/user/loginOut'})
}

export const getUserListApi = ({params}: AxiosConfig) => {
    return request.get<{
        code: string
        data: {
            list: UserType[]
            total: number
        }
    }>({url: '/mock/user/list', params})
}

export const getAdminRoleApi = (
    params: RoleParams
): Promise<IResponse<AppCustomRouteRecordRaw[]>> => {
    return request.get({url: '/mock/role/list', params})
}

export const getTestRoleApi = (params: RoleParams): Promise<IResponse<string[]>> => {
    return request.get({url: '/mock/role/list2', params})
}
