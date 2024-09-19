import request from '@/axios'

/** 获取 */
export const getDictList = (id) => {
    return request.get({url: `/admin/cmn/dict/${id}`})
}
/** 删除 */
export const delDict = (id) => {
    return request.delete({url: `/admin/cmn/dict/${id}`})
}
/** 修改 */
export const putDict = (data) => {
    return request.put({url: '/admin/cmn/dict', data})
}
/** 新增 */
export const addDict = (data) => {
    return request.post({url: '/admin/cmn/dict', data})
}
/** 详情 */
export const detailDict = (id) => {
    return request.get({url: `/admin/cmn/dict/detail/${id}`})
}
/** 清除缓存刷新数据 */
export const clearCache = () => {
    return request.get({url: `/admin/cmn/dict/clearCache`})
}
