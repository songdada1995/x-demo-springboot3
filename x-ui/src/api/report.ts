import { request } from '../utils/request'

export const reportApi = {
  list(params: any) {
    return request.get('/upms/api/biz/report/list', params)
  },
  overview() {
    return request.get('/upms/api/biz/report/overview')
  },
  getInfo(id: number) {
    return request.get(`/upms/api/biz/report/${id}`)
  },
  add(data: any) {
    return request.post('/upms/api/biz/report', data)
  },
  edit(data: any) {
    return request.put('/upms/api/biz/report', data)
  },
  remove(ids: string) {
    return request.delete(`/upms/api/biz/report/${ids}`)
  },
}
