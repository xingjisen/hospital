import {SUCCESS_CODE} from '@/constants'

const timeout = 1000

const List: {
    userName: string
    password: string
    role: string
    roleId: string
    permissions: string | string[]
}[] = [
    {
        userName: 'admin',
        password: 'admin',
        role: 'admin',
        roleId: '1',
        permissions: ['*.*.*']
    },
    {
        userName: 'test',
        password: 'test',
        role: 'test',
        roleId: '2',
        permissions: ['example:dialog:create', 'example:dialog:delete']
    }
]

export default [
    // 列表接口
    {
        url: '/mock/user/list',
        method: 'get',
        response: ({query}) => {
            const {userName, pageIndex, pageSize} = query

            const mockList = List.filter((item) => {
                if (userName && item.userName.indexOf(userName) < 0) return false
                return true
            })
            const pageList = mockList.filter(
                (_, index) => index < pageSize * pageIndex && index >= pageSize * (pageIndex - 1)
            )

            return {
                code: SUCCESS_CODE,
                data: {
                    total: mockList.length,
                    list: pageList
                }
            }
        }
    },
    // 登录接口
    {
        url: '/mock/user/login',
        method: 'post',
        timeout,
        response: ({body}) => {
            const data = body
            let hasUser = false
            for (const user of List) {
                if (user.userName === data.userName && user.password === data.password) {
                    hasUser = true
                    return {
                        code: SUCCESS_CODE,
                        data: user
                    }
                }
            }
            if (!hasUser) {
                return {
                    code: 500,
                    message: '账号或密码错误'
                }
            }
        }
    },
    // 退出接口
    {
        url: '/mock/user/loginOut',
        method: 'get',
        timeout,
        response: () => {
            return {
                code: SUCCESS_CODE,
                data: null
            }
        }
    }
]
