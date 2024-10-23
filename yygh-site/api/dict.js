import request from '@/utils/axios'
import qs from 'qs'
const api_name = '/admin/cmn/dict'

/** 根据dictCode获取下级节点*/
export const findByDictCode = (dictCode)=>{
  return request.get(`${api_name}/findByDictCode/${dictCode}`)
}

/** 根据id获取下级节点*/
export function findByParentId(parentId){
  return request.get(`${api_name}/findChildData/${parentId}`)
}
