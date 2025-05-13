import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../components/layout/MainLayout.vue'
import Dashboard from '../components/pages/Dashboard.vue'
import SystemIcons from '../components/pages/SystemIcons.vue'

const routes = [
  {
    path: '/',
    component: MainLayout,
    children: [
      {
        path: '',
        redirect: '/dashboard'
      },
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: Dashboard,
        meta: {
          title: '仪表盘',
          icon: 'dashboard',
        },
      },
      {
        path: '/system',
        component: () => import('@/views/system/index.vue'),
        children: [
          {
            path: 'users',
            name: 'SystemUsers',
            component: () => import('@/views/system/users/index.vue'),
            meta: {
              title: '用户管理',
              icon: 'user',
            },
          },
          {
            path: 'roles',
            name: 'SystemRoles',
            component: () => import('@/views/system/roles/index.vue'),
            meta: {
              title: '角色管理',
              icon: 'team',
            },
          },
          {
            path: 'permissions',
            name: 'SystemPermissions',
            component: () => import('@/views/system/permissions/index.vue'),
            meta: {
              title: '权限管理',
              icon: 'safety',
            },
          },
        ]
      },
      {
        path: '/business',
        name: 'Business',
        component: SystemIcons
      },
      {
        path: '/business/cost',
        name: 'CostAccounting',
        component: () => import('@/views/finance/cost/index.vue')
      },
      {
        path: '/business/cost/detail',
        name: 'CostDetail',
        component: () => import('@/views/finance/cost/detail.vue')
      },
      {
        path: '/business/finance',
        name: 'FinancialReport',
        component: () => import('@/views/finance/report/index.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router 