import axios, {AxiosError} from 'axios'
import {defaultRequestInterceptors, defaultResponseInterceptors} from '@/axios/config'

import {AxiosInstance, AxiosResponse, InternalAxiosRequestConfig} from '@/axios/types'
import {ElMessage} from 'element-plus'
import {REQUEST_TIMEOUT} from '@/constants'
import service from "@/axios/service";

export const PATH_URL = import.meta.env.VITE_API_BASE_PATH

const abortControllerMap: Map<string, AbortController> = new Map()

const axiosInstance: AxiosInstance = axios.create({
    timeout: REQUEST_TIMEOUT,
    baseURL: PATH_URL
})

axiosInstance.interceptors.request.use((res: InternalAxiosRequestConfig) => {
    const controller = new AbortController()
    let url = res.url || ''
    res.signal = controller.signal
    abortControllerMap.set(
        import.meta.env.VITE_USE_MOCK === 'true' ? url.replace(`/mock`, '') : url,
        controller
    )
    return res
})

axiosInstance.interceptors.response.use(
    (res: AxiosResponse) => {
        const url = res.config.url || ''
        abortControllerMap.delete(url)
        // 这里不能做任何处理，否则后面的 interceptors 拿不到完整的上下文了
        return res
    },
    (error: AxiosError) => {
        console.log('err： ' + error) // for debug
        ElMessage.error(error.message)
        return Promise.reject(error)
    }
)

axiosInstance.interceptors.request.use(defaultRequestInterceptors)
axiosInstance.interceptors.response.use(defaultResponseInterceptors)

export const download = (url: string, filename: string = 'downloaded-file') => {
    return new Promise<void>((resolve, reject) => {
        service.request({
            url,
            method: 'GET',
            responseType: 'blob', // 指定返回类型为 Blob
        }).then((res: AxiosResponse<Blob>) => {
            // 创建 Blob 对象并触发下载
            const blob = new Blob([res.data], { type: res.headers['content-type'] });
            const downloadUrl = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = downloadUrl;
            link.setAttribute('download', filename);
            document.body.appendChild(link);
            link.click();

            // 释放 URL 和移除 link
            window.URL.revokeObjectURL(downloadUrl);
            document.body.removeChild(link);

            resolve();
        }).catch((error: AxiosError) => {
            ElMessage.error('Download failed: ' + error.message);
            reject(error);
        });
    });
}
export const downloadParam = (url: string, params?: any, filename: string = 'downloaded-file') => {
    return new Promise((resolve, reject) => {
        service.request({
            url,
            method: 'GET',
            params,
            responseType: 'blob', // 指定返回类型为 Blob
            interceptors: {
                responseInterceptors: (res) => {
                    // 创建 Blob 对象并触发下载
                    const blob = new Blob([res.data], { type: res.headers['content-type'] })
                    const downloadUrl = window.URL.createObjectURL(blob)
                    const link = document.createElement('a')
                    link.href = downloadUrl
                    link.setAttribute('download', filename)
                    document.body.appendChild(link)
                    link.click()

                    // 释放 URL 和移除 link
                    window.URL.revokeObjectURL(downloadUrl)
                    document.body.removeChild(link)

                    // **这里需要返回响应数据以满足类型检查**
                    return res
                }
            }
        }).then((res) => {
            resolve(res)
        }).catch((error: AxiosError) => {
            ElMessage.error('Download failed: ' + error.message)
            reject(error)
        })
    })
}
