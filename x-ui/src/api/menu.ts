import { request } from '../utils/request'

export const menuApi = {
  list(params?: any) {
    return request.get('/upms/api/system/menu/list', params)
  },
  getInfo(menuId: number) {
    return request.get(`/upms/api/system/menu/${menuId}`)
  },
  add(data: any) {
    return request.post('/upms/api/system/menu', data)
  },
  edit(data: any) {
    return request.put('/upms/api/system/menu', data)
  },
  remove(menuId: number) {
    return request.delete(`/upms/api/system/menu/${menuId}`)
  },
  treeselect() {
    return request.get('/upms/api/system/menu/treeselect')
  },
  getUserRoutes() {
    return request.get('/upms/api/system/menu/user/routes')
  },
}
