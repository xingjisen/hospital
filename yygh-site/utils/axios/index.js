import axios from '@nuxtjs/axios'

const service = axios.create({
  baseURL: 'http://localhost',
  timeout: 15000 // 请求超时时间
})

// http请求拦截器
service.interceptors.requestInterceptors.use(
  config => {
    return config
  },
  err => {
    return Promise.reject(err)
  }
)
// http相应拦截器
service.interceptors.response.use(
  response => {
    if (response.data.code !== 200) {
      ElMessage({
        message: response.data.message,
        type: 'error'
      })
    } else {
      return response.data
    }
  },
  err => {
    Promise.reject(err)
  }
)
return service
