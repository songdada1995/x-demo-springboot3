import axios from 'axios'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'

// 创建axios实例
const service: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  }
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // 在请求发送之前做一些处理
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    const res = response.data
    // 根据自定义错误码判断请求是否成功
    if (res.code === 200) {
      return res
    } else {
      // 处理业务错误
      return Promise.reject(new Error(res.message || 'Error'))
    }
  },
  (error) => {
    // 处理 HTTP 网络错误
    return Promise.reject(error)
  }
)

// 封装通用请求方法
export const request = {
  get<T = any>(url: string, params?: object): Promise<T> {
    return service.get(url, { params })
  },
  post<T = any>(url: string, data?: object): Promise<T> {
    return service.post(url, data)
  },
  put<T = any>(url: string, data?: object): Promise<T> {
    return service.put(url, data)
  },
  delete<T = any>(url: string, params?: object): Promise<T> {
    return service.delete(url, { params })
  }
}

export default service 