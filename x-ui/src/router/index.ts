import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../components/layout/MainLayout.vue'
import Dashboard from '../components/pages/Dashboard.vue'
import SystemIcons from '../components/pages/SystemIcons.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/login/index.vue'),
    meta: {
      title: '登录',
      requiresAuth: false
    }
  },
  {
    path: '/',
    component: MainLayout,
    meta: {
      requiresAuth: true
    },
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
          requiresAuth: true
        },
      },
      {
        path: '/system',
        component: () => import('@/views/system/index.vue'),
        meta: {
          requiresAuth: true
        },
        children: [
          {
            path: 'users',
            name: 'SystemUsers',
            component: () => import('@/views/system/users/index.vue'),
            meta: {
              title: '用户管理',
              icon: 'user',
              requiresAuth: true
            },
          },
          {
            path: 'roles',
            name: 'SystemRoles',
            component: () => import('@/views/system/roles/index.vue'),
            meta: {
              title: '角色管理',
              icon: 'team',
              requiresAuth: true
            },
          },
          {
            path: 'permissions',
            name: 'SystemPermissions',
            component: () => import('@/views/system/permissions/index.vue'),
            meta: {
              title: '权限管理',
              icon: 'safety',
              requiresAuth: true
            },
          },
        ]
      },
      {
        path: '/business',
        name: 'Business',
        component: SystemIcons,
        meta: {
          requiresAuth: true
        }
      },
      {
        path: '/business/cost',
        name: 'CostAccounting',
        component: () => import('@/views/finance/cost/index.vue'),
        meta: {
          requiresAuth: true
        }
      },
      {
        path: '/business/cost/detail',
        name: 'CostDetail',
        component: () => import('@/views/finance/cost/detail.vue'),
        meta: {
          requiresAuth: true
        }
      },
      {
        path: '/business/finance',
        name: 'FinancialReport',
        component: () => import('@/views/finance/report/index.vue'),
        meta: {
          requiresAuth: true
        }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth !== false)
  
  // 如果需要认证且没有token，则重定向到登录页
  if (requiresAuth && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    // 如果已登录且访问登录页，则重定向到首页
    next('/dashboard')
  } else {
    next()
  }
})

export default router 