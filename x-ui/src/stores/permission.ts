import { defineStore } from 'pinia'
import { ref } from 'vue'
import { menuApi } from '../api/menu'
import { userApi } from '../api/user'

export interface MenuData {
  menuId: number
  menuName: string
  parentId: number
  path: string
  icon: string
  menuType: string
  perms: string
  orderNum: number
  children?: MenuData[]
}

export const usePermissionStore = defineStore('permission', () => {
  const menus = ref<MenuData[]>([])
  const permissions = ref<string[]>([])
  const currentUser = ref<any>(null)
  const loaded = ref(false)

  const fetchUserInfo = async () => {
    try {
      const res: any = await userApi.getCurrentUser()
      currentUser.value = res.data.user
      permissions.value = Array.isArray(res.data.permissions)
        ? res.data.permissions
        : Array.from(res.data.permissions || [])
    } catch {
      permissions.value = []
    }
  }

  const fetchMenus = async () => {
    try {
      const res: any = await menuApi.getUserRoutes()
      menus.value = res.data || []
    } catch {
      menus.value = []
    }
  }

  const loadPermissions = async () => {
    await Promise.all([fetchUserInfo(), fetchMenus()])
    loaded.value = true
  }

  const hasPermission = (perm: string): boolean => {
    if (permissions.value.includes('*:*:*')) return true
    return permissions.value.includes(perm)
  }

  const hasMenuAccess = (path: string): boolean => {
    if (permissions.value.includes('*:*:*')) return true
    if (path === '/' || path === '') return true
    if (menus.value.length === 0) return path === '/dashboard' // 无菜单时允许仪表盘兜底
    return checkPathInMenus(menus.value, path)
  }

  /** 获取用户第一个可访问的菜单路径（用于登录后/无权限时重定向） */
  const getFirstMenuPath = (): string => {
    const path = findFirstPath(menus.value)
    return path || '/dashboard'
  }

  const findFirstPath = (items: MenuData[]): string | null => {
    for (const item of items) {
      if (item.menuType === 'C' && item.path) return item.path
      if (item.children) {
        const found = findFirstPath(item.children)
        if (found) return found
      }
    }
    return null
  }

  const checkPathInMenus = (items: MenuData[], path: string): boolean => {
    for (const item of items) {
      if (item.path === path) return true
      if (item.children && checkPathInMenus(item.children, path)) return true
    }
    return false
  }

  const reset = () => {
    menus.value = []
    permissions.value = []
    currentUser.value = null
    loaded.value = false
  }

  return {
    menus,
    permissions,
    currentUser,
    loaded,
    loadPermissions,
    hasPermission,
    hasMenuAccess,
    getFirstMenuPath,
    reset,
  }
})
