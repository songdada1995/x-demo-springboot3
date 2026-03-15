import { request } from '../utils/request'

export const costApi = {
  list(params: any) {
    return request.get('/upms/api/biz/cost/list', params)
  },
  overview() {
    return request.get('/upms/api/biz/cost/overview')
  },
  getInfo(id: number) {
    return request.get(`/upms/api/biz/cost/${id}`)
  },
  add(data: any) {
    return request.post('/upms/api/biz/cost', data)
  },
  edit(data: any) {
    return request.put('/upms/api/biz/cost', data)
  },
  remove(ids: string) {
    return request.delete(`/upms/api/biz/cost/${ids}`)
  },
}
