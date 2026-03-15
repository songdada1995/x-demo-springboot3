import { request } from '../utils/request'

export const accountApi = {
  list(params: any) {
    return request.get('/upms/api/biz/account/list', params)
  },
  getInfo(id: number) {
    return request.get(`/upms/api/biz/account/${id}`)
  },
  add(data: any) {
    return request.post('/upms/api/biz/account', data)
  },
  edit(data: any) {
    return request.put('/upms/api/biz/account', data)
  },
  remove(ids: string) {
    return request.delete(`/upms/api/biz/account/${ids}`)
  },
}
