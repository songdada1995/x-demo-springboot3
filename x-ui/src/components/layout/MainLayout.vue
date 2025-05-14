<template>
  <a-layout class="main-layout">
    <!-- 顶部区域 -->
    <a-layout-header class="header">
      <div class="header-left">
        <div class="logo-area" :class="{ 'logo-collapsed': collapsed }">
          <span class="logo">X-UI</span>
        </div>
        <a-menu
          v-model:selectedKeys="selectedTopKeys"
          mode="horizontal"
          theme="dark"
          class="top-menu"
          @select="handleTopMenuSelect"
        >
          <a-menu-item key="system" :class="['top-menu-item', {'highlighted-menu-item': selectedTopKeys[0] === 'system'}]">系统管理</a-menu-item>
          <a-menu-item key="business" :class="['top-menu-item', {'highlighted-menu-item': selectedTopKeys[0] === 'business'}]">业务平台</a-menu-item>
        </a-menu>
      </div>
      <div class="header-right">
        <a-dropdown>
          <span class="username" @mouseenter="arrowUp = true" @mouseleave="arrowUp = false">
            {{ userInfo?.name || userInfo?.username || 'admin' }}
            <up-outlined v-if="arrowUp" />
            <down-outlined v-else />
          </span>
          <template #overlay>
            <a-menu @click="handleUserMenuClick" class="user-dropdown-menu">
              <a-menu-item key="profile">
                <user-outlined />
                个人信息
              </a-menu-item>
              <a-menu-item key="password">
                <lock-outlined />
                修改密码
              </a-menu-item>
              <a-menu-divider />
              <a-menu-item key="logout">
                <logout-outlined />
                退出登录
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </div>
    </a-layout-header>

    <!-- 主体布局 -->
    <a-layout class="main-content-layout">
      <!-- 左侧区域 -->
      <a-layout-sider
        v-model:collapsed="collapsed"
        :trigger="null"
        collapsible
        class="sider"
      >
        <a-menu
          v-model:selectedKeys="selectedKeys"
          v-model:openKeys="openKeys"
          mode="inline"
          theme="dark"
          @select="handleMenuSelect"
          @openChange="handleOpenChange"
          :selectable="true"
          :multiple="false"
        >
          <template v-for="item in currentMenuItems" :key="item.key">
            <a-sub-menu v-if="item.children" :key="item.key">
              <template #title>
                <span>
                  <component :is="item.icon" />
                  <span>{{ item.title }}</span>
                </span>
              </template>
              <template v-for="child in item.children" :key="child.key">
                <a-sub-menu v-if="child.children" :key="`sub-${child.key}`">
                  <template #title>
                    <span>
                      <component :is="child.icon" v-if="child.icon" />
                      <span>{{ child.title }}</span>
                    </span>
                  </template>
                  <a-menu-item v-for="grandChild in child.children" :key="grandChild.key">
                    {{ grandChild.title }}
                  </a-menu-item>
                </a-sub-menu>
                <a-menu-item v-else :key="`item-${child.key}`">
                  <span>
                    <component :is="child.icon" v-if="child.icon" />
                    <span>{{ child.title }}</span>
                  </span>
                </a-menu-item>
              </template>
            </a-sub-menu>
            <a-menu-item v-else :key="`item-${item.key}`">
              <span>
                <component :is="item.icon" />
                <span>{{ item.title }}</span>
              </span>
            </a-menu-item>
          </template>
        </a-menu>
      </a-layout-sider>

      <!-- 内容区域 -->
      <a-layout-content class="content">
        <!-- 工具栏 -->
        <div class="toolbar">
          <div class="sider-trigger" @click="toggleCollapsed">
            <menu-unfold-outlined v-if="collapsed" />
            <menu-fold-outlined v-else />
          </div>
        </div>
        <div class="content-wrapper">
          <router-view></router-view>
        </div>
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted, watch, nextTick, onBeforeUnmount } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  UserOutlined,
  SettingOutlined,
  DashboardOutlined,
  TeamOutlined,
  AppstoreOutlined,
  DownOutlined,
  UpOutlined,
  LockOutlined,
  LogoutOutlined,
  MenuUnfoldOutlined,
  MenuFoldOutlined,
  SafetyOutlined,
  AuditOutlined,
  CalculatorOutlined,
  AccountBookOutlined,
} from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { useAuthStore } from '../../stores/auth'

interface MenuItem {
  key: string
  title: string
  icon?: any
  path?: string
  children?: MenuItem[]
}

const authStore = useAuthStore()
const userInfo = computed(() => authStore.userInfo)

const router = useRouter()
const route = useRoute()
const collapsed = ref(false)
const selectedKeys = ref<string[]>(['item-dashboard'])
const openKeys = ref<string[]>([])
const selectedTopKeys = ref<string[]>(['system'])
const arrowUp = ref(false)

// 系统管理菜单
const systemMenuItems: MenuItem[] = [
  {
    key: 'dashboard',
    title: '仪表盘',
    icon: DashboardOutlined,
    path: '/dashboard'
  },
  {
    key: 'systemManagement',
    title: '系统管理',
    icon: SettingOutlined,
    children: [
      {
        key: 'users',
        title: '用户管理',
        icon: UserOutlined,
        path: '/system/users'
      },
      {
        key: 'roles',
        title: '角色管理',
        icon: TeamOutlined,
        path: '/system/roles'
      },
      {
        key: 'permissions',
        title: '权限管理',
        icon: SafetyOutlined,
        path: '/system/permissions'
      },
    ],
  },
]

// 业务平台菜单
const businessMenuItems: MenuItem[] = [
  {
    key: 'businessPlatform',
    title: '业务管理',
    icon: AppstoreOutlined,
    children: [
      {
        key: '4',
        title: '成本核算',
        icon: CalculatorOutlined,
        path: '/business/cost'
      },
      {
        key: '5',
        title: '财务报表',
        icon: AccountBookOutlined,
        path: '/business/finance'
      },
    ],
  },
]

// 根据顶部菜单选择显示对应的左侧菜单
const currentMenuItems = computed(() => {
  return selectedTopKeys.value[0] === 'system' ? systemMenuItems : businessMenuItems
})

// 查找菜单项
const findMenuItem = (items: MenuItem[], key: string): MenuItem | null => {
  for (const item of items) {
    if (item.key === key) {
      return item
    }
    if (item.children) {
      const found = findMenuItem(item.children, key)
      if (found) return found
    }
  }
  return null
}

// 记住最后选中的菜单项
const lastSelectedKey = ref('item-dashboard')

// 处理菜单选择
const handleMenuSelect = ({ key }: { key: string }) => {
  console.log('Menu selected:', key, 'Current openKeys:', openKeys.value)
  
  // 移除 key 前缀以获取实际的菜单 key
  const actualKey = key.replace(/^(item-|sub-)/, '')
  const selectedItem = findMenuItem(currentMenuItems.value, actualKey)
  
  // 如果是有子菜单的项，不进行高亮，只处理展开/收起
  if (selectedItem && selectedItem.children) {
    // 展开/收起子菜单
    const isOpen = openKeys.value.includes(selectedItem.key)
    if (isOpen) {
      openKeys.value = openKeys.value.filter(k => k !== selectedItem.key)
    } else {
      // 当点击一级菜单时，添加到展开列表，但不要移除其他已展开的菜单
      openKeys.value = [...openKeys.value, selectedItem.key]
    }
    
    // 恢复上一次的选中状态
    nextTick(() => {
      selectedKeys.value = [lastSelectedKey.value]
    })
    return
  }
  
  if (selectedItem && !selectedItem.children) {
    // 只有没有子菜单的项才能被选中
    selectedKeys.value = [key]
    lastSelectedKey.value = key
    localStorage.setItem('lastSelectedMenuKey', key) // 保存到localStorage
    
    // 如果选中的菜单项在子菜单中，确保其父菜单保持展开状态
    const findParentMenu = (items: MenuItem[], targetKey: string): string | null => {
      for (const item of items) {
        if (item.children) {
          for (const child of item.children) {
            if (child.key === targetKey) {
              return item.key
            }
            if (child.children) {
              for (const grandChild of child.children) {
                if (grandChild.key === targetKey) {
                  // 如果是在二级子菜单中，展开两级菜单
                  if (!openKeys.value.includes(`sub-${child.key}`)) {
                    openKeys.value.push(`sub-${child.key}`)
                  }
                  return item.key
                }
              }
            }
          }
          const found = findParentMenu(item.children, targetKey)
          if (found) return found
        }
      }
      return null
    }
    
    const parentKey = findParentMenu(currentMenuItems.value, actualKey)
    if (parentKey && !openKeys.value.includes(parentKey)) {
      // 添加新的父菜单到展开列表，但保留其他已展开的菜单
      openKeys.value = [...openKeys.value, parentKey]
    }
    
    // 路由跳转
    if (selectedItem.path) {
      router.push(selectedItem.path)
    }
  }
}

// 处理菜单展开/收起
const handleOpenChange = (keys: string[]) => {
  console.log('Menu openChange called with:', keys, 'Current:', openKeys.value)
  
  // 获取本次操作中被添加和移除的key
  const addedKeys = keys.filter(key => !openKeys.value.includes(key))
  const removedKeys = openKeys.value.filter(key => !keys.includes(key))
  
  console.log('Added keys:', addedKeys, 'Removed keys:', removedKeys)
  
  // 仅当用户明确点击折叠某个菜单时才进行折叠
  // 如果是添加新菜单，则保留已有的打开菜单
  if (addedKeys.length > 0) {
    // 添加新的菜单到已打开的菜单列表中
    openKeys.value = [...openKeys.value, ...addedKeys]
  } else if (removedKeys.length > 0) {
    // 用户明确点击关闭某个菜单，才进行折叠
    openKeys.value = openKeys.value.filter(key => !removedKeys.includes(key))
  }
  // 如果既没有添加也没有移除，保持不变
  
  console.log('After update, openKeys:', openKeys.value)
}

// 处理子菜单点击
const handleSubMenuClick = (item: MenuItem) => {
  console.log('SubMenu clicked:', item.key)
  // 如果点击的是有子菜单的项，只处理展开/收起
  if (item.children) {
    // 检查当前是否已经展开
    const isOpen = openKeys.value.includes(item.key)
    if (isOpen) {
      // 如果已展开，则关闭
      openKeys.value = openKeys.value.filter(k => k !== item.key)
    } else {
      // 如果未展开，则打开
      openKeys.value = [...openKeys.value, item.key]
    }
    console.log('After click, openKeys:', openKeys.value)
    
    // 防止菜单被选中，如果当前菜单有子项，则保持上一次选中的状态
    // 阻止事件冒泡，防止被选中
    selectedKeys.value = [lastSelectedKey.value]
    
    // 如果路由没有改变，确保不触发菜单项的选中状态改变
    nextTick(() => {
      if (selectedKeys.value.length === 0) {
        selectedKeys.value = [lastSelectedKey.value]
      }
    })
  }
}

// 处理顶部菜单选择
const handleTopMenuSelect = ({ key }: { key: string }) => {
  // 保存当前所有打开的菜单状态
  const currentOpenKeys = [...openKeys.value]
  
  // 确保顶部菜单的选中状态正确
  selectedTopKeys.value = [key]
  console.log('Top menu selected:', key, 'Current openKeys:', currentOpenKeys)
  
  // 重置左侧菜单选中状态，但不清空打开的菜单
  selectedKeys.value = []
  
  // 获取新菜单的第一个可点击项
  const firstMenuItem = findFirstMenuItem(currentMenuItems.value)
  if (firstMenuItem) {
    // 设置选中状态
    const newKey = `item-${firstMenuItem.key}`
    selectedKeys.value = [newKey]
    lastSelectedKey.value = newKey
    localStorage.setItem('lastSelectedMenuKey', newKey)
    
    // 如果有路径，进行路由跳转
    if (firstMenuItem.path) {
      router.push(firstMenuItem.path)
    }
    
    // 如果第一个可点击项在子菜单中，需要展开其父菜单
    const findParentMenu = (items: MenuItem[], targetKey: string): string | null => {
      for (const item of items) {
        if (item.children) {
          if (item.children.some(child => child.key === targetKey)) {
            return item.key
          }
          const found = findParentMenu(item.children, targetKey)
          if (found) return found
        }
      }
      return null
    }
    
    const parentKey = findParentMenu(currentMenuItems.value, firstMenuItem.key)
    if (parentKey && !currentOpenKeys.includes(parentKey)) {
      // 保留当前所有打开的菜单，并添加新的父菜单
      openKeys.value = [...currentOpenKeys, parentKey]
    }
  }
  
  // 强制应用顶部菜单的选中样式
  nextTick(() => {
    // 手动给选中的菜单项添加样式
    const selectedMenuItem = document.querySelector(`.ant-menu-horizontal .ant-menu-item[data-menu-id="${key}"]`) as HTMLElement
    if (selectedMenuItem) {
      selectedMenuItem.classList.add('ant-menu-item-selected')
      selectedMenuItem.style.backgroundColor = '#27c2ad'
      selectedMenuItem.style.color = 'white'
    }
  })
}

// 查找第一个可点击的菜单项（没有子菜单的项）
const findFirstMenuItem = (items: MenuItem[]): MenuItem | null => {
  for (const item of items) {
    if (!item.children) {
      // 如果当前项没有子菜单，直接返回
      return item
    }
    // 如果有子菜单，递归查找子菜单中的第一个可点击项
    const found = findFirstMenuItem(item.children)
    if (found) return found
  }
  return null
}

// 处理用户菜单点击
const handleUserMenuClick = ({ key }: { key: string }) => {
  switch (key) {
    case 'profile':
      router.push('/system/profile')
      break
    case 'password':
      router.push('/system/change-password')
      break
    case 'logout':
      // 退出登录
      authStore.logout()
      message.success('退出成功')
      router.push('/login')
      break
  }
}

// 切换菜单折叠状态
const toggleCollapsed = () => {
  collapsed.value = !collapsed.value
}

// 修复菜单样式
const fixMenuStyles = () => {
  setTimeout(() => {
    // 获取所有二级菜单项
    const secondLevelMenuItems = document.querySelectorAll('.ant-menu-dark .ant-menu-submenu .ant-menu-item')
    // 修改它们的内联样式
    secondLevelMenuItems.forEach(item => {
      (item as HTMLElement).style.paddingLeft = '43px'
    })
  }, 100)
}

// 添加全局点击事件监听器
const addGlobalClickListener = () => {
  // 使用MutationObserver监听菜单项的class变化
  const menuObserver = new MutationObserver((mutations) => {
    mutations.forEach((mutation) => {
      if (mutation.type === 'attributes' && mutation.attributeName === 'class') {
        const target = mutation.target as HTMLElement
        // 如果是菜单项且有上次选中的菜单项
        if (target.classList.contains('ant-menu-item') && lastSelectedKey.value) {
          // 检查是否是我们应该选中的菜单项
          const key = target.getAttribute('data-menu-id') || ''
          if (key.includes(lastSelectedKey.value.replace(/^(item-|sub-)/, ''))) {
            // 如果这个菜单项应该被选中但没有选中类，则添加选中类
            if (!target.classList.contains('ant-menu-item-selected')) {
              nextTick(() => {
                selectedKeys.value = [lastSelectedKey.value]
              })
            }
          }
        }
      }
    })
  })
  
  // 监听所有菜单项
  setTimeout(() => {
    const menuItems = document.querySelectorAll('.ant-menu-item')
    menuItems.forEach(item => {
      menuObserver.observe(item, { attributes: true })
    })
  }, 500)
}

// 组件挂载时初始化
const initFirstMenuItem = () => {
  // 确保 openKeys 初始为空，避免闪烁
  openKeys.value = []
  
  // 检查URL路径，如果不是仪表盘路径，则尝试根据路径找到对应的菜单项
  if (route.path !== '/dashboard') {
    // 查找菜单项所属的顶级菜单
    const findTopMenuByPath = (path: string): 'system' | 'business' => {
      // 检查是否在系统菜单中
      const isInSystemMenu = systemMenuItems.some(item => 
        item.path === path || 
        (item.children && item.children.some(child => 
          child.path === path || 
          (child.children && child.children.some(grandChild => grandChild.path === path))
        ))
      )
      return isInSystemMenu ? 'system' : 'business'
    }
    
    const topMenu = findTopMenuByPath(route.path)
    selectedTopKeys.value = [topMenu]
    
    // 根据路径查找对应的菜单项
    const findMenuItemByPath = (items: MenuItem[], path: string): { key: string, parentKeys: string[] } | null => {
      for (const item of items) {
        if (item.path === path) {
          return { key: `item-${item.key}`, parentKeys: [] }
        }
        if (item.children) {
          for (const child of item.children) {
            if (child.path === path) {
              return { key: `item-${child.key}`, parentKeys: [item.key] }
            }
            if (child.children) {
              for (const grandChild of child.children) {
                if (grandChild.path === path) {
                  return { key: grandChild.key, parentKeys: [item.key, `sub-${child.key}`] }
                }
              }
            }
          }
        }
      }
      return null
    }
    
    const currentItems = topMenu === 'system' ? systemMenuItems : businessMenuItems
    const menuInfo = findMenuItemByPath(currentItems, route.path)
    
    if (menuInfo) {
      selectedKeys.value = [menuInfo.key]
      lastSelectedKey.value = menuInfo.key
      localStorage.setItem('lastSelectedMenuKey', menuInfo.key)
      
      // 只展开当前菜单所在的父菜单
      openKeys.value = menuInfo.parentKeys
      return
    }
  } else {
    // 如果是首次加载或是仪表盘路径，只选中仪表盘，不展开任何菜单
    selectedKeys.value = ['item-dashboard']
    lastSelectedKey.value = 'item-dashboard'
    localStorage.setItem('lastSelectedMenuKey', 'item-dashboard')
    
    // 确保不会有任何菜单展开
    openKeys.value = []
  }
}

// 在组件挂载后添加事件监听
onMounted(() => {
  // 确保在挂载时不会有任何菜单自动展开
  nextTick(() => {
    // 强制关闭所有菜单，解决初始渲染时的闪烁问题
    openKeys.value = []
    
    // 然后再初始化
    initFirstMenuItem()
    fixMenuStyles()
    addGlobalClickListener()
    
    // 确保顶部菜单的选中状态正确显示
    nextTick(() => {
      const topMenuKey = selectedTopKeys.value[0]
      const selectedTopMenuItem = document.querySelector(`.ant-menu-horizontal .ant-menu-item[data-menu-id="${topMenuKey}"]`) as HTMLElement
      if (selectedTopMenuItem) {
        selectedTopMenuItem.classList.add('ant-menu-item-selected')
        selectedTopMenuItem.style.backgroundColor = '#27c2ad'
        selectedTopMenuItem.style.color = 'white'
      }
      
      // 监听顶部菜单项的类变化
      const topMenuObserver = new MutationObserver((mutations) => {
        mutations.forEach((mutation) => {
          if (mutation.type === 'attributes' && mutation.attributeName === 'class') {
            const target = mutation.target as HTMLElement
            if (target.classList.contains('ant-menu-item') && 
                (target.getAttribute('data-menu-id') === 'system' || 
                 target.getAttribute('data-menu-id') === 'business')) {
              if (target.getAttribute('data-menu-id') === selectedTopKeys.value[0] && 
                  !target.classList.contains('ant-menu-item-selected')) {
                target.classList.add('ant-menu-item-selected')
                target.style.backgroundColor = '#27c2ad'
                target.style.color = 'white'
              }
            }
          }
        })
      })
      
      // 监听所有顶部菜单项
      const topMenuItems = document.querySelectorAll('.ant-menu-horizontal .ant-menu-item')
      topMenuItems.forEach(item => {
        topMenuObserver.observe(item, { attributes: true })
      })
    })
  })
})

// 在组件卸载时清除事件监听器
onBeforeUnmount(() => {
  // 清理工作可以在这里
})

// 监听折叠状态变化，重新应用样式
watch(collapsed, () => {
  fixMenuStyles()
})

// 监听路由变化，保持菜单选中状态
watch(() => route.path, (newPath) => {
  console.log('Route changed:', newPath, 'Current openKeys:', openKeys.value)
  
  // 保存当前已经打开的所有菜单
  const currentOpenKeys = [...openKeys.value]
  
  // 根据路径查找对应的菜单项
  const findMenuItemByPath = (items: MenuItem[], path: string): string | null => {
    for (const item of items) {
      if (item.path === path) {
        return `item-${item.key}`
      }
      if (item.children) {
        for (const child of item.children) {
          if (child.path === path) {
            return `item-${child.key}`
          }
          if (child.children) {
            for (const grandChild of child.children) {
              if (grandChild.path === path) {
                return grandChild.key
              }
            }
          }
        }
      }
    }
    return null
  }
  
  // 查找菜单项所属的顶级菜单
  const findTopMenuByPath = (path: string): 'system' | 'business' => {
    // 检查是否在系统菜单中
    const inSystem = findMenuItemByPath(systemMenuItems, path) !== null
    return inSystem ? 'system' : 'business'
  }
  
  // 找到对应顶级菜单
  const topMenu = findTopMenuByPath(newPath)
  selectedTopKeys.value = [topMenu]
  
  // 更新当前显示的菜单项
  let currentItems = topMenu === 'system' ? systemMenuItems : businessMenuItems
  
  // 在当前菜单中查找
  let menuKey = findMenuItemByPath(currentItems, newPath)
  if (menuKey) {
    // 设置选中的菜单项
    selectedKeys.value = [menuKey]
    lastSelectedKey.value = menuKey
    
    // 仅展开当前选中菜单所在的父菜单，但不关闭其他已打开的菜单
    const findParentMenus = (items: MenuItem[], path: string): string[] => {
      const parents: string[] = []
      
      const findParent = (items: MenuItem[], path: string, level: number = 0): boolean => {
        for (const item of items) {
          if (item.path === path) {
            return true
          }
          
          if (item.children) {
            const childFound = findParent(item.children, path, level + 1)
            if (childFound) {
              if (level === 0) {
                parents.push(item.key)
              } else if (level === 1) {
                parents.push(`sub-${item.key}`)
              }
              return true
            }
          }
        }
        return false
      }
      
      findParent(items, path)
      return parents
    }
    
    const parentMenus = findParentMenus(currentItems, newPath)
    // 将新菜单的父菜单添加到现有的打开菜单中，但不关闭已打开的菜单
    if (parentMenus.length > 0) {
      const newOpenKeys = [...currentOpenKeys]
      parentMenus.forEach(key => {
        if (!newOpenKeys.includes(key)) {
          newOpenKeys.push(key)
        }
      })
      openKeys.value = newOpenKeys
    }
  }
})
</script>

<style lang="less" scoped>
.main-layout {
  height: 100vh;
  width: 100%;
}

/* 顶部区域样式 */
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0;
  background: #121212; /* 暗黑色背景 */
  height: 48px;
  min-height: 48px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.15);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  width: 100%;
}

.header-left {
  display: flex;
  align-items: center;
  height: 100%;
  gap: 15px;
}

.logo-area {
  background: #1a1a1a; /* 稍浅一点的暗黑色 */
  display: flex;
  align-items: center;
  justify-content: center;
  height: 48px;
  min-height: 48px;
  width: 200px;
  min-width: 200px;
  max-width: 200px;
  flex-shrink: 0;
  box-sizing: border-box;
  transition: all 0.2s;
  border-right: 1px solid #2a2a2a;
  box-shadow: inset 0 -1px 0 0 #2a2a2a;
}

.logo-collapsed {
  width: 80px !important;
  min-width: 80px !important;
  max-width: 80px !important;
}

.logo {
  color: #27c2ad; /* 保持主打配色 */
  font-size: 20px;
  font-weight: bold;
  letter-spacing: 2px;
  text-align: center;
  margin-left: 0;
}

.top-menu {
  flex: 1;
  line-height: 48px;
  height: 48px;
  background: #121212; /* 暗黑色背景 */
  color: #fff;
  min-width: 0;
  margin-left: 20px;
  padding-left: 10px;
}

.header-right {
  height: 48px;
  display: flex;
  align-items: center;
  padding: 0 24px;
  color: #fff;
  border-radius: 0;
  background-color: transparent !important;
}

/* 用户名样式 */
.username {
  color: #fff;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 5px;
  background-color: transparent !important;
  padding: 0 10px;
  border-radius: 4px;
  transition: all 0.3s;
}

.username:hover {
  background-color: rgba(255, 255, 255, 0.1) !important;
}

/* 主体内容布局 */
.main-content-layout {
  margin-top: 48px;
  height: calc(100vh - 48px);
  display: flex;
  flex-direction: row;
}

/* 左侧区域样式 */
.sider {
  background: #121212; /* 暗黑色背景 */
  overflow-y: auto;
  overflow-x: hidden;
  height: 100%;
  position: relative;
  transition: all 0.2s;
  z-index: 990;
}

:deep(.ant-layout-sider) {
  background: #121212 !important; /* 暗黑色背景 */
  min-width: 200px !important;
  max-width: 200px !important;
  width: 200px !important;
  flex: 0 0 200px !important;
}

:deep(.ant-layout-sider-collapsed) {
  min-width: 80px !important;
  max-width: 80px !important;
  width: 80px !important;
  flex: 0 0 80px !important;
}

/* 内容区域样式 */
.content {
  background: #f0f2f5;
  position: relative;
  padding: 0;
  transition: all 0.2s;
  flex: 1;
  display: flex;
  flex-direction: column;
}

/* 工具栏样式 */
.toolbar {
  height: 32px;
  background: #fff;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  align-items: center;
  padding-left: 16px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

.content-wrapper {
  background: #fff;
  padding: 24px;
  flex: 1;
  width: 100%;
  box-sizing: border-box;
  position: relative;
}

/* 收起按钮样式 */
.sider-trigger {
  width: 32px;
  height: 32px;
  color: #27c2ad; /* 保持主打配色 */
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s;
  background: transparent;
  border: none;
  padding: 0;
  font-size: 20px;
}

.sider-trigger:hover {
  color: #27c2ad; /* 保持主打配色 */
  opacity: 0.8;
}

/* 菜单样式 */
:deep(.ant-menu-dark) {
  background: #121212 !important; /* 暗黑色背景 */
  margin-top: 0 !important;
  color: #fff !important; /* 设置文字颜色为白色 */
}

:deep(.ant-menu-dark .ant-menu-sub) {
  background: #1a1a1a !important; /* 子菜单稍浅一点的暗黑色 */
}

:deep(.ant-menu-dark .ant-menu-item-selected) {
  background-color: #27c2ad !important; /* 保持高亮色不变 */
  width: 100% !important; /* 确保高亮占满整行 */
  margin: 0 !important; /* 移除边距 */
  padding-right: 0 !important; /* 移除右内边距 */
  color: #fff !important; /* 确保选中项文字为白色 */
}

:deep(.ant-menu-dark .ant-menu-item) {
  width: 100% !important; /* 确保菜单项占满整行 */
  margin: 0 !important; /* 移除菜单项之间的边距 */
  padding-right: 0 !important; /* 移除右内边距 */
  color: #fff !important; /* 设置文字颜色为白色 */
}

:deep(.ant-menu-dark .ant-menu-item:hover),
:deep(.ant-menu-dark .ant-menu-submenu-title:hover) {
  background-color: #2a2a2a !important; /* 悬停颜色 */
  width: 100% !important; /* 确保悬停效果占满整行 */
  margin: 0 !important; /* 移除边距 */
  padding-right: 0 !important; /* 移除右内边距 */
}

:deep(.ant-menu-dark .ant-menu-submenu-selected > .ant-menu-submenu-title) {
  color: #fff !important; /* 选中的子菜单标题颜色改为白色 */
}

:deep(.ant-menu-dark .ant-menu-item-active),
:deep(.ant-menu-dark .ant-menu-submenu-active) {
  background-color: #2a2a2a !important; /* 活动状态颜色 */
  width: 100% !important; /* 确保活动效果占满整行 */
  margin: 0 !important; /* 移除边距 */
  padding-right: 0 !important; /* 移除右内边距 */
  color: #fff !important; /* 设置文字颜色为白色 */
}

:deep(.ant-menu-dark .ant-menu-submenu-open) {
  color: #fff !important; /* 打开的子菜单颜色改为白色 */
}

:deep(.ant-menu-dark .ant-menu-submenu-title) {
  width: 100% !important; /* 确保子菜单标题占满整行 */
  margin: 0 !important; /* 移除边距 */
  padding-right: 16px !important; /* 调整右内边距，为箭头留出空间 */
  color: #fff !important; /* 设置文字颜色为白色 */
}

/* 移除菜单项的蓝色过渡效果 */
:deep(.ant-menu-item::after),
:deep(.ant-menu-submenu::after) {
  border-right: none !important;
  transition: none !important;
}

/* 调整菜单项的圆角 */
:deep(.ant-menu-item),
:deep(.ant-menu-submenu) {
  border-radius: 0 !important;
  margin: 0 !important;
}

/* 调整子菜单的缩进 */
:deep(.ant-menu-sub .ant-menu-item) {
  padding-left: 48px !important;
  width: 100% !important; /* 确保子菜单项占满整行 */
  margin: 0 !important; /* 移除边距 */
}

/* 顶部菜单样式 */
:deep(.ant-menu-horizontal) {
  background: #121212 !important; /* 暗黑色背景 */
  color: #fff;
  border-bottom: none;
  line-height: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  max-width: 400px;
}

/* 修复顶部菜单项的下拉菜单背景色 */
:deep(.ant-menu-submenu-popup) {
  background: #121212 !important; /* 暗黑色背景 */
}

:deep(.ant-menu-submenu-popup .ant-menu) {
  background: #1a1a1a !important; /* 稍浅一点的暗黑色 */
}

:deep(.ant-dropdown-menu) {
  background: #1a1a1a !important; /* 稍浅一点的暗黑色 */
  border: 1px solid #2a2a2a !important;
}

:deep(.ant-dropdown-menu-item),
:deep(.ant-dropdown-menu-submenu-title) {
  color: #fff !important;
}

:deep(.ant-dropdown-menu-item:hover),
:deep(.ant-dropdown-menu-submenu-title:hover) {
  background-color: #2a2a2a !important; /* 悬停颜色 */
}

/* 用户下拉菜单样式 */
:deep(.user-dropdown-menu.ant-menu) {
  background: #1a1a1a !important; /* 稍浅一点的暗黑色 */
  border: 1px solid #2a2a2a !important;
}

:deep(.user-dropdown-menu.ant-menu .ant-menu-item) {
  color: #fff !important;
}

:deep(.user-dropdown-menu.ant-menu .ant-menu-item:hover) {
  background-color: #2a2a2a !important; /* 悬停颜色 */
}

/* 确保菜单项内容左对齐且占满整行 */
:deep(.ant-menu-inline .ant-menu-item),
:deep(.ant-menu-inline .ant-menu-submenu-title) {
  text-align: left !important;
  justify-content: flex-start !important;
  width: 100% !important;
  margin: 0 !important;
  padding-right: 16px !important;
}

/* 修复菜单项选中时的边框和背景 */
:deep(.ant-menu-item-selected) {
  border-right: none !important;
}

/* 修复菜单折叠时的样式 */
:deep(.ant-layout-sider-collapsed .ant-menu-item),
:deep(.ant-layout-sider-collapsed .ant-menu-submenu-title) {
  padding: 0 calc(50% - 16px) !important; /* 图标居中 */
  width: 100% !important;
}

:deep(.ant-layout-sider-collapsed .ant-menu-item-selected) {
  width: 100% !important;
  padding: 0 calc(50% - 16px) !important;
}

/* 顶部菜单项专用样式类 */
:deep(.top-menu-item) {
  padding: 0 30px !important;
  margin: 0 5px !important;
  height: 48px !important;
  line-height: 48px !important;
  text-align: center !important;
  font-size: 16px !important;
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
  background-color: #121212 !important; /* 暗黑色背景 */
  border: none !important;
  box-shadow: none !important;
  width: auto !important; /* 覆盖左侧菜单的宽度设置 */
  color: #fff !important; /* 设置文字颜色为白色 */
}

:deep(.top-menu-item .ant-menu-title-content) {
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
  text-align: center !important;
  width: 100% !important;
}

/* 顶部菜单项基本样式 */
:deep(.ant-menu-horizontal > .ant-menu-item) {
  color: #fff;
  margin: 0 5px;
  padding: 0 30px;
  height: 48px;
  line-height: 48px;
  text-align: center;
  font-size: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: auto !important; /* 覆盖左侧菜单的宽度设置 */
}

/* 确保顶部菜单项文字居中对齐 */
:deep(.ant-menu-horizontal > .ant-menu-item .ant-menu-title-content) {
  text-align: center !important;
  display: flex !important;
  justify-content: center !important;
  align-items: center !important;
  width: 100% !important;
}

/* 选中和悬停状态 */
:deep(.ant-menu-horizontal > .ant-menu-item-selected) {
  background-color: #27c2ad !important; /* 保持主打配色 */
  color: #fff !important;
  width: auto !important; /* 覆盖左侧菜单的宽度设置 */
}

:deep(.ant-menu-horizontal > .ant-menu-item:hover) {
  background-color: #27c2ad !important; /* 保持主打配色 */
  color: #fff !important;
  opacity: 0.8;
  width: auto !important; /* 覆盖左侧菜单的宽度设置 */
}

/* 顶部菜单项样式 - 保持原样不填满 */
:deep(.header .ant-menu-item) {
  transition: none !important;
  animation: none !important;
  margin: 0 5px !important;
  padding: 0 30px !important;
  height: 48px !important;
  line-height: 48px !important;
  width: auto !important; /* 覆盖左侧菜单的宽度设置 */
}

:deep(.header .ant-menu-item:hover) {
  background-color: #27c2ad !important; /* 保持主打配色 */
  opacity: 0.8 !important;
  color: #fff !important;
  transition: none !important;
  animation: none !important;
  width: auto !important; /* 覆盖左侧菜单的宽度设置 */
}

:deep(.header .ant-menu-item-selected) {
  background-color: #27c2ad !important; /* 保持主打配色 */
  color: #fff !important;
  border-bottom: none !important;
  transition: none !important;
  animation: none !important;
  width: auto !important; /* 覆盖左侧菜单的宽度设置 */
}

/* 左侧菜单样式 - 确保左对齐 */
:deep(.ant-menu-inline .ant-menu-item),
:deep(.ant-menu-inline .ant-menu-submenu-title) {
  text-align: left !important;
  justify-content: flex-start !important;
}

:deep(.ant-menu-inline .ant-menu-item .ant-menu-title-content),
:deep(.ant-menu-inline .ant-menu-submenu-title .ant-menu-title-content) {
  text-align: left !important;
  justify-content: flex-start !important;
  display: inline-block !important;
}

/* 高亮菜单项 */
:deep(.highlighted-menu-item) {
  background-color: #27c2ad !important; /* 保持主打配色 */
  color: #fff !important;
  width: auto !important; /* 覆盖左侧菜单的宽度设置 */
}

/* 修复菜单折叠时的样式 */
:deep(.ant-layout-sider-collapsed) {
  background: #121212 !important; /* 暗黑色背景 */
}

:deep(.ant-layout-sider-collapsed .ant-menu-inline-collapsed) {
  background: #121212 !important; /* 暗黑色背景 */
}

:deep(.ant-menu-inline-collapsed) {
  background: #121212 !important; /* 暗黑色背景 */
}

:deep(.ant-menu-inline-collapsed > .ant-menu-item),
:deep(.ant-menu-inline-collapsed > .ant-menu-submenu > .ant-menu-submenu-title) {
  background: #121212 !important; /* 暗黑色背景 */
  color: #fff !important;
}

:deep(.ant-menu-inline-collapsed .ant-menu-item),
:deep(.ant-menu-inline-collapsed .ant-menu-submenu-title) {
  padding: 0 calc(50% - 16px) !important; /* 图标居中 */
  width: 100% !important;
  background: #121212 !important; /* 暗黑色背景 */
}

:deep(.ant-menu-inline-collapsed .ant-menu-item-selected) {
  width: 100% !important;
  padding: 0 calc(50% - 16px) !important;
  background-color: #27c2ad !important; /* 保持高亮色不变 */
}

:deep(.ant-menu-inline-collapsed .ant-menu-item:hover),
:deep(.ant-menu-inline-collapsed .ant-menu-submenu-title:hover) {
  background-color: #2a2a2a !important; /* 悬停颜色 */
}

/* 修复菜单折叠后的弹出子菜单样式 */
:deep(.ant-menu-vertical .ant-menu-item),
:deep(.ant-menu-vertical-left .ant-menu-item),
:deep(.ant-menu-vertical-right .ant-menu-item),
:deep(.ant-menu-inline .ant-menu-item),
:deep(.ant-menu-vertical .ant-menu-submenu-title),
:deep(.ant-menu-vertical-left .ant-menu-submenu-title),
:deep(.ant-menu-vertical-right .ant-menu-submenu-title),
:deep(.ant-menu-inline .ant-menu-submenu-title) {
  background: #121212 !important; /* 暗黑色背景 */
  color: #fff !important;
}

:deep(.ant-menu-vertical .ant-menu-item-selected),
:deep(.ant-menu-vertical-left .ant-menu-item-selected),
:deep(.ant-menu-vertical-right .ant-menu-item-selected),
:deep(.ant-menu-inline .ant-menu-item-selected) {
  background-color: #27c2ad !important; /* 保持高亮色不变 */
  color: #fff !important;
}

:deep(.ant-menu-vertical .ant-menu-item:hover),
:deep(.ant-menu-vertical-left .ant-menu-item:hover),
:deep(.ant-menu-vertical-right .ant-menu-item:hover),
:deep(.ant-menu-inline .ant-menu-item:hover),
:deep(.ant-menu-vertical .ant-menu-submenu-title:hover),
:deep(.ant-menu-vertical-left .ant-menu-submenu-title:hover),
:deep(.ant-menu-vertical-right .ant-menu-submenu-title:hover),
:deep(.ant-menu-inline .ant-menu-submenu-title:hover) {
  background-color: #2a2a2a !important; /* 悬停颜色 */
}

/* 修复折叠时弹出的子菜单背景色 */
:deep(.ant-menu-submenu-popup > .ant-menu) {
  background: #1a1a1a !important; /* 稍浅一点的暗黑色 */
}
</style> 