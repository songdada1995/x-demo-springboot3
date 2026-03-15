import { request } from '../utils/request'

export const userApi = {
  list(params: any) {
    return request.get('/upms/api/system/user/list', params)
  },
  getInfo(userId: number) {
    return request.get(`/upms/api/system/user/${userId}`)
  },
  add(data: any) {
    return request.post('/upms/api/system/user', data)
  },
  edit(data: any) {
    return request.put('/upms/api/system/user', data)
  },
  remove(userId: number) {
    return request.delete(`/upms/api/system/user/${userId}`)
  },
  resetPwd(data: any) {
    return request.put('/upms/api/system/user/resetPwd', data)
  },
  changeStatus(data: any) {
    return request.put('/upms/api/system/user/changeStatus', data)
  },
  getRoleIds(userId: number) {
    return request.get(`/upms/api/system/user/role/${userId}`)
  },
  getCurrentUser() {
    return request.get('/upms/api/system/user/current')
  },
}
