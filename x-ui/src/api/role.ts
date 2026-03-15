import { request } from '../utils/request'

export const roleApi = {
  list(params: any) {
    return request.get('/upms/api/system/role/list', params)
  },
  getInfo(roleId: number) {
    return request.get(`/upms/api/system/role/${roleId}`)
  },
  add(data: any) {
    return request.post('/upms/api/system/role', data)
  },
  edit(data: any) {
    return request.put('/upms/api/system/role', data)
  },
  remove(roleId: number) {
    return request.delete(`/upms/api/system/role/${roleId}`)
  },
  getRoleMenuIds(roleId: number) {
    return request.get(`/upms/api/system/role/${roleId}/menus`)
  },
  saveRoleMenus(roleId: number, menuIds: number[]) {
    return request.put(`/upms/api/system/role/${roleId}/menus`, { menuIds })
  },
}
