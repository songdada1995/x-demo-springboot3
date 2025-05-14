import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

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
  // 状态
  const token = ref<string | null>(localStorage.getItem('token'))
  const userInfo = ref<UserInfo | null>(null)
  
  // 计算属性
  const isLoggedIn = computed(() => !!token.value)
  
  // 方法
  const login = async (payload: LoginPayload) => {
    // 模拟API请求
    const mockToken = 'mock-jwt-token-' + Date.now()
    const mockUserInfo: UserInfo = {
      username: payload.username,
      name: '管理员',
      avatar: '',
      roles: ['admin'],
      permissions: ['*']
    }
    
    // 保存到状态
    token.value = mockToken
    userInfo.value = mockUserInfo
    
    // 保存到本地存储
    localStorage.setItem('token', mockToken)
    localStorage.setItem('userInfo', JSON.stringify(mockUserInfo))
    
    return { token: mockToken, userInfo: mockUserInfo }
  }
  
  const logout = () => {
    // 清除状态
    token.value = null
    userInfo.value = null
    
    // 清除本地存储
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }
  
  const loadUserInfo = () => {
    const storedUserInfo = localStorage.getItem('userInfo')
    if (storedUserInfo) {
      userInfo.value = JSON.parse(storedUserInfo)
    }
    return userInfo.value
  }
  
  // 初始化用户信息
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