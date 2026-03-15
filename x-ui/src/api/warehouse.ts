import { request } from '../utils/request'

export const warehouseApi = {
  list(params: any) {
    return request.get('/upms/api/biz/warehouse/list', params)
  },
  getInfo(id: number) {
    return request.get(`/upms/api/biz/warehouse/${id}`)
  },
  add(data: any) {
    return request.post('/upms/api/biz/warehouse', data)
  },
  edit(data: any) {
    return request.put('/upms/api/biz/warehouse', data)
  },
  remove(ids: string) {
    return request.delete(`/upms/api/biz/warehouse/${ids}`)
  },
}
