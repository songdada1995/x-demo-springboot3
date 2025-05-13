import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../components/layout/MainLayout.vue'
import Dashboard from '../components/pages/Dashboard.vue'
import ListPage from '../components/pages/ListPage.vue'
import SystemIcons from '../components/pages/SystemIcons.vue'
import CostAccounting from '../components/pages/CostAccounting.vue'
import FinancialReport from '../components/pages/FinancialReport.vue'

const routes = [
  {
    path: '/',
    component: MainLayout,
    children: [
      {
        path: '',
        redirect: '/system'
      },
      {
        path: '/system',
        name: 'System',
        component: Dashboard
      },
      {
        path: '/system/users',
        name: 'UserList',
        component: ListPage
      },
      {
        path: '/system/roles',
        name: 'RoleList',
        component: ListPage
      },
      {
        path: '/system/permissions',
        name: 'PermissionList',
        component: ListPage
      },
      {
        path: '/business',
        name: 'Business',
        component: SystemIcons
      },
      {
        path: '/business/cost',
        name: 'CostAccounting',
        component: CostAccounting
      },
      {
        path: '/business/finance',
        name: 'FinancialReport',
        component: FinancialReport
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router 