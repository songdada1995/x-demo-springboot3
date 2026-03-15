import { request } from '../utils/request'

export const dashboardApi = {
  stats() {
    return request.get('/upms/api/dashboard/stats')
  },
  notices() {
    return request.get('/upms/api/dashboard/notices')
  },
  recentActivity() {
    return request.get('/upms/api/dashboard/recentActivity')
  },
}
