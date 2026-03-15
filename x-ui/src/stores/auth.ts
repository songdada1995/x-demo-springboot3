import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { request } from '../utils/request'
import { usePermissionStore } from './permission'

interface LoginPayload {
  username: string
  password: string
}

interface UserInfo {
  username: string
  name?: string
  avatar?: string
  roles?: string[]
  permissions?: string[]
}

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(localStorage.getItem('token'))
  const userInfo = ref<UserInfo | null>(null)

  const isLoggedIn = computed(() => !!token.value)

  const login = async (payload: LoginPayload) => {
    const res = await request.post<any>('/auth/login', payload)
    const data = (res as any).data

    token.value = data.token
    localStorage.setItem('token', data.token)

    const permissionStore = usePermissionStore()
    await permissionStore.loadPermissions()

    const user: UserInfo = {
      username: payload.username,
      permissions: permissionStore.permissions,
    }
    userInfo.value = user
    localStorage.setItem('userInfo', JSON.stringify(user))

    return data
  }

  const logout = async () => {
    try {
      await request.post('/auth/logout')
    } catch {
      // 即使后端登出失败也清除本地状态
    }
    token.value = null
    userInfo.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')

    const permissionStore = usePermissionStore()
    permissionStore.reset()
  }

  const loadUserInfo = () => {
    const storedUserInfo = localStorage.getItem('userInfo')
    if (storedUserInfo) {
      userInfo.value = JSON.parse(storedUserInfo)
    }
    return userInfo.value
  }

  if (token.value) {
    loadUserInfo()
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    login,
    logout,
    loadUserInfo
  }
}) 