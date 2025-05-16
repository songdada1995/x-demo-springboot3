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
          :theme="null"
          class="top-menu"
          @select="handleTopMenuSelect"
        >
          <a-menu-item key="system">
            <template #icon><setting-outlined /></template>
            系统管理
          </a-menu-item>
          <a-menu-item key="business">
            <template #icon><appstore-outlined /></template>
            业务平台
          </a-menu-item>
        </a-menu>
      </div>
      <!-- 添加用户信息区域 -->
      <div class="header-right">
        <div class="username" @mouseenter="dropdownVisible = true" @mouseleave="dropdownVisible = false">
          <user-outlined />
          <span>{{ userInfo?.username || '管理员' }}</span>
          <down-outlined :style="{ transform: dropdownVisible ? 'rotate(180deg)' : 'rotate(0deg)', transition: 'transform 0.3s' }" />
        </div>
        <div class="custom-dropdown" v-show="dropdownVisible" @mouseenter="dropdownVisible = true" @mouseleave="dropdownVisible = false">
          <div class="dropdown-menu">
            <div class="dropdown-item" @click="handleUserMenuClick({ key: 'profile' })">
              <user-outlined />
              <span>个人信息</span>
            </div>
            <div class="dropdown-item" @click="handleUserMenuClick({ key: 'password' })">
              <lock-outlined />
              <span>修改密码</span>
            </div>
            <div class="dropdown-divider"></div>
            <div class="dropdown-item" @click="handleUserMenuClick({ key: 'logout' })">
              <logout-outlined />
              <span>退出登录</span>
            </div>
          </div>
        </div>
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
          theme="light"
          @select="handleMenuSelect"
          @openChange="handleOpenChange"
          :selectable="true"
          :multiple="false"
        >
          <template v-for="item in currentMenuItems" :key="item.key">
            <a-sub-menu v-if="item.children" :key="item.key">
              <template #icon>
                  <component :is="item.icon" />
              </template>
              <template #title>{{ item.title }}</template>
              <template v-for="child in item.children" :key="child.key">
                <a-sub-menu v-if="child.children" :key="`sub-${child.key}`">
                  <template #icon>
                      <component :is="child.icon" v-if="child.icon" />
                  </template>
                  <template #title>{{ child.title }}</template>
                  <a-menu-item v-for="grandChild in child.children" :key="grandChild.key">
                    {{ grandChild.title }}
                  </a-menu-item>
                </a-sub-menu>
                <a-menu-item v-else :key="`item-${child.key}`">
                  <template #icon>
                    <component :is="child.icon" v-if="child.icon" />
                  </template>
                  {{ child.title }}
                </a-menu-item>
              </template>
            </a-sub-menu>
            <a-menu-item v-else :key="`item-${item.key}`">
              <template #icon>
                <component :is="item.icon" />
              </template>
              {{ item.title }}
            </a-menu-item>
          </template>
        </a-menu>
      </a-layout-sider>

      <!-- 内容区域 -->
      <a-layout-content class="content">
        <!-- 工具栏 -->
        <div class="toolbar">
          <div class="toolbar-left">
            <div class="sider-trigger" @click="toggleCollapsed">
              <menu-unfold-outlined v-if="collapsed" />
              <menu-fold-outlined v-else />
            </div>
            <a-tabs
              v-model:activeKey="activeTabKey"
              type="editable-card"
              @edit="onTabEdit"
              @change="onTabChange"
              hide-add
              class="page-tabs"
            >
              <a-tab-pane
                v-for="tab in pageTabs"
                :key="tab.key"
                :closable="pageTabs.length > 1"
                :title="tab.title"
              >
                <template #tab>
                  <span>{{ tab.title }}</span>
                </template>
              </a-tab-pane>
            </a-tabs>
          </div>
          <div class="toolbar-right">
            <div class="tab-actions">
              <a-dropdown>
                <template #overlay>
                  <a-menu>
                    <a-menu-item key="closeOthers" @click="closeOtherTabs">
                      <template #icon><close-circle-outlined /></template>
                      关闭其他标签页
                    </a-menu-item>
                    <a-menu-item key="closeCurrent" @click="closeCurrentTab">
                      <template #icon><close-outlined /></template>
                      关闭当前标签页
                    </a-menu-item>
                    <a-menu-item key="closeAll" @click="closeAllTabs">
                      <template #icon><delete-outlined /></template>
                      关闭全部标签页
                    </a-menu-item>
                    <a-menu-divider />
                    <a-menu-item key="refreshCurrent" @click="refreshCurrentTab">
                      <template #icon><reload-outlined /></template>
                      刷新当前标签页
                    </a-menu-item>
                    <a-menu-item key="refreshAll" @click="refreshAllTabs">
                      <template #icon><sync-outlined /></template>
                      刷新全部标签页
                    </a-menu-item>
                  </a-menu>
                </template>
                <a-button type="text" class="action-button">
                  <template #icon><more-outlined /></template>
                </a-button>
              </a-dropdown>
            </div>
          </div>
        </div>

        <div class="content-wrapper">
          <router-view :key="refreshKey"></router-view>
        </div>
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted, watch, nextTick, onBeforeUnmount, provide } from 'vue'
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
  WalletOutlined,
  BankOutlined,
  MoreOutlined,
  CloseCircleOutlined,
  CloseOutlined,
  DeleteOutlined,
  ReloadOutlined,
  SyncOutlined,
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
const dropdownVisible = ref(false)

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
    key: 'financeManagement',
    title: '财务管理',
    icon: BankOutlined,
    path: '/business/finance-management'
  },
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
      {
        key: 'account',
        title: '账号维护',
        icon: UserOutlined,
        path: '/business/account'
      },
      {
        key: 'warehouse',
        title: '仓库设置',
        icon: AppstoreOutlined,
        path: '/business/warehouse'
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

// 标签页相关
interface PageTab {
  key: string
  title: string
  path: string
  icon?: any
}

const pageTabs = ref<PageTab[]>([
  {
    key: 'dashboard',
    title: '仪表盘',
    path: '/dashboard',
    icon: DashboardOutlined
  }
])
const activeTabKey = ref('dashboard')

// 处理标签页编辑（关闭）
const onTabEdit = (targetKey: string, action: 'add' | 'remove') => {
  if (action === 'remove') {
    // 如果只有一个标签页，不允许关闭
    if (pageTabs.value.length <= 1) {
      return
    }

    const tabs = pageTabs.value
    let activeKey = activeTabKey.value
    let lastIndex = -1

    tabs.forEach((tab, i) => {
      if (tab.key === targetKey) {
        lastIndex = i - 1
      }
    })

    const newTabs = tabs.filter(tab => tab.key !== targetKey)
    if (newTabs.length && activeKey === targetKey) {
      if (lastIndex >= 0) {
        activeKey = newTabs[lastIndex].key
      } else {
        activeKey = newTabs[0].key
      }
    }

    pageTabs.value = newTabs
    activeTabKey.value = activeKey
    
    // 确保路由跳转到激活的标签页
    const activeTab = newTabs.find(tab => tab.key === activeKey)
    if (activeTab) {
      router.push(activeTab.path)
    }
  }
}

// 处理标签页切换
const onTabChange = (key: string) => {
  const tab = pageTabs.value.find(tab => tab.key === key)
  if (tab) {
    // 更新路由
    router.push(tab.path)
    
    // 更新菜单选中状态
    updateMenuSelection(tab.path)
  }
}

// 更新菜单选中状态
const updateMenuSelection = (path: string) => {
  // 根据路径判断应该选中哪个顶部菜单
  const isSystemPath = path.startsWith('/system') || path === '/dashboard'
  selectedTopKeys.value = [isSystemPath ? 'system' : 'business']

  // 查找对应的菜单项
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

  const currentItems = selectedTopKeys.value[0] === 'system' ? systemMenuItems : businessMenuItems
  const menuInfo = findMenuItemByPath(currentItems, path)

  if (menuInfo) {
    // 更新左侧菜单选中状态
    selectedKeys.value = [menuInfo.key]
    lastSelectedKey.value = menuInfo.key
    localStorage.setItem('lastSelectedMenuKey', menuInfo.key)

    // 展开父级菜单
    openKeys.value = menuInfo.parentKeys
  }
}

// 添加新标签页
const addTab = (menuItem: MenuItem) => {
  if (!menuItem.path) return

  const existingTab = pageTabs.value.find(tab => tab.path === menuItem.path)
  if (!existingTab) {
    const newTab: PageTab = {
      key: menuItem.key,
      title: menuItem.title,
      path: menuItem.path,
      icon: menuItem.icon
    }
    pageTabs.value.push(newTab)
  }
  activeTabKey.value = menuItem.key
}

// 修改菜单选择处理函数
const handleMenuSelect = ({ key }: { key: string }) => {
  // 移除 key 前缀以获取实际的菜单 key
  const actualKey = key.replace(/^(item-|sub-)/, '')
  const selectedItem = findMenuItem(currentMenuItems.value, actualKey)
  
  // 如果是有子菜单的项，只处理展开/收起
  if (selectedItem && selectedItem.children) {
    const isOpen = openKeys.value.includes(selectedItem.key)
    if (isOpen) {
      openKeys.value = openKeys.value.filter(k => k !== selectedItem.key)
    } else {
      openKeys.value = [...openKeys.value, selectedItem.key]
    }
    return
  }
  
  // 只有没有子菜单的项才能被选中
  if (selectedItem && !selectedItem.children) {
    selectedKeys.value = [key]
    lastSelectedKey.value = key
    
    // 确保父菜单保持展开状态
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
                  if (!openKeys.value.includes(`sub-${child.key}`)) {
                    openKeys.value.push(`sub-${child.key}`)
                  }
                  return item.key
                }
              }
            }
          }
        }
      }
      return null
    }
    
    const parentKey = findParentMenu(currentMenuItems.value, actualKey)
    if (parentKey && !openKeys.value.includes(parentKey)) {
      openKeys.value = [...openKeys.value, parentKey]
    }
    
    // 添加标签页并路由跳转
    if (selectedItem.path) {
      addTab(selectedItem)
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

// 修改初始化函数
const initTopMenu = () => {
  // 根据路由路径判断应该选中哪个顶部菜单
  const path = route.path
  const isSystemPath = path.startsWith('/system') || path === '/dashboard'
  selectedTopKeys.value = [isSystemPath ? 'system' : 'business']
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
    const findMenuItemByPath = (items: MenuItem[], path: string): { key: string, parentKeys: string[], menuItem: MenuItem } | null => {
      for (const item of items) {
        if (item.path === path) {
          return { key: `item-${item.key}`, parentKeys: [], menuItem: item }
        }
        if (item.children) {
          for (const child of item.children) {
            if (child.path === path) {
              return { key: `item-${child.key}`, parentKeys: [item.key], menuItem: child }
            }
            if (child.children) {
              for (const grandChild of child.children) {
                if (grandChild.path === path) {
                  return { key: grandChild.key, parentKeys: [item.key, `sub-${child.key}`], menuItem: grandChild }
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

      // 更新标签页
      if (menuInfo && menuInfo.menuItem.path && menuInfo.menuItem.key) {
        pageTabs.value = [{
          key: menuInfo.menuItem.key,
          title: menuInfo.menuItem.title,
          path: menuInfo.menuItem.path,
          icon: menuInfo.menuItem.icon
        }]
        activeTabKey.value = menuInfo.menuItem.key
      }
      return
    }
  } else {
    // 如果是首次加载或是仪表盘路径，只选中仪表盘，不展开任何菜单
    selectedKeys.value = ['item-dashboard']
    lastSelectedKey.value = 'item-dashboard'
    localStorage.setItem('lastSelectedMenuKey', 'item-dashboard')
    
    // 确保不会有任何菜单展开
    openKeys.value = []

    // 更新标签页为仪表盘
    pageTabs.value = [{
      key: 'dashboard',
      title: '仪表盘',
      path: '/dashboard',
      icon: DashboardOutlined
    }]
    activeTabKey.value = 'dashboard'
  }
}

// 在组件挂载后添加事件监听
onMounted(() => {
  nextTick(() => {
    initTopMenu()
    initFirstMenuItem()
    fixMenuStyles()
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
watch(() => route.path, () => {
  initTopMenu()
})

// 监听路由变化，自动添加标签页
watch(() => route.path, (newPath) => {
  const findMenuItemByPath = (items: MenuItem[], path: string): MenuItem | null => {
    for (const item of items) {
      if (item.path === path) {
        return item
      }
      if (item.children) {
        for (const child of item.children) {
          if (child.path === path) {
            return child
          }
          if (child.children) {
            for (const grandChild of child.children) {
              if (grandChild.path === path) {
                return grandChild
              }
            }
          }
        }
      }
    }
    return null
  }

  const menuItem = findMenuItemByPath(currentMenuItems.value, newPath)
  if (menuItem) {
    addTab(menuItem)
    // 更新菜单选中状态
    updateMenuSelection(newPath)
  }
}, { immediate: true })

// 关闭其他标签页
const closeOtherTabs = () => {
  // 如果只有一个标签页，不需要操作
  if (pageTabs.value.length <= 1) {
    return
  }

  const currentTab = pageTabs.value.find(tab => tab.key === activeTabKey.value)
  if (currentTab) {
    pageTabs.value = [currentTab]
    // 确保路由跳转到当前标签页
    router.push(currentTab.path)
  }
}

// 关闭当前标签页
const closeCurrentTab = () => {
  // 如果只有一个标签页，不允许关闭
  if (pageTabs.value.length <= 1) {
    return
  }
  
  if (activeTabKey.value !== 'dashboard') {
    const tabs = pageTabs.value
    const currentIndex = tabs.findIndex(tab => tab.key === activeTabKey.value)
    const nextTab = tabs[currentIndex + 1] || tabs[currentIndex - 1]
    
    // 直接关闭当前标签页
    onTabEdit(activeTabKey.value, 'remove')
    
    // 如果有关闭后的下一个标签页，切换到它
    if (nextTab) {
      activeTabKey.value = nextTab.key
      router.push(nextTab.path)
    }
  }
}

// 关闭全部标签页
const closeAllTabs = () => {
  const dashboardTab = pageTabs.value.find(tab => tab.key === 'dashboard')
  if (dashboardTab) {
    pageTabs.value = [dashboardTab]
    activeTabKey.value = 'dashboard'
    router.push('/dashboard')
  }
}

// 提供刷新方法给子组件
const refreshKey = ref(0)
provide('refreshKey', refreshKey)

// 刷新当前标签页
const refreshCurrentTab = () => {
  // 通过改变 key 来触发组件重新渲染
  refreshKey.value++
}

// 刷新全部标签页
const refreshAllTabs = () => {
  // 通过改变 key 来触发组件重新渲染
  refreshKey.value++
}
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
  padding: 0;
  background: linear-gradient(135deg, #27c2ad 0%, #1fa195 100%) !important;
  height: 48px;
  min-height: 48px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
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
  gap: 0;
  flex: 1;
}

.logo-area {
  background: linear-gradient(135deg, #27c2ad 0%, #1fa195 100%) !important;
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
  border-right: 1px solid rgba(255, 255, 255, 0.1);
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
  position: relative;
  z-index: 1001;
}

.logo-collapsed {
  width: 80px !important;
  min-width: 80px !important;
  max-width: 80px !important;
}

.logo {
  color: #ffffff;
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
  background: transparent !important;
  border: none !important;
  margin: 0 !important;
  padding: 0 !important;
  position: relative;
  z-index: 1000;
}

/* 顶部菜单样式 */
:deep(.ant-menu.top-menu) {
  background: transparent !important;
  border: none !important;
  line-height: 48px !important;
  height: 48px !important;
}

:deep(.ant-menu.top-menu.ant-menu-horizontal) {
  line-height: 48px !important;
  height: 48px !important;
  border: none !important;
}

:deep(.ant-menu.top-menu.ant-menu-horizontal > .ant-menu-item) {
  height: 48px !important;
  line-height: 48px !important;
  margin: 0 !important;
  padding: 0 24px !important;
  color: #ffffff !important;
  background: transparent !important;
  border: none !important;
  min-width: 180px !important;
  transition: all 0.3s !important;
  border-radius: 0 !important;
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
  gap: 8px !important;
}

:deep(.ant-menu.top-menu.ant-menu-horizontal > .ant-menu-item:hover) {
  color: #333333 !important;
  background-color: #f0f0f0 !important;
  border-radius: 0 !important;
}

:deep(.ant-menu.top-menu.ant-menu-horizontal > .ant-menu-item-selected),
:deep(.ant-menu.top-menu.ant-menu-horizontal > .ant-menu-item.ant-menu-item-selected) {
  color: #333333 !important;
  background-color: #f0f0f0 !important;
  border-radius: 0 !important;
}

:deep(.ant-menu.top-menu.ant-menu-horizontal > .ant-menu-item .anticon) {
  font-size: 16px !important;
  margin-right: 4px !important;
  color: inherit !important;
}

:deep(.ant-menu.top-menu.ant-menu-horizontal > .ant-menu-item:hover .anticon),
:deep(.ant-menu.top-menu.ant-menu-horizontal > .ant-menu-item-selected .anticon),
:deep(.ant-menu.top-menu.ant-menu-horizontal > .ant-menu-item.ant-menu-item-selected .anticon) {
  color: #333333 !important;
}

:deep(.ant-menu.top-menu.ant-menu-horizontal > .ant-menu-item::after),
:deep(.ant-menu.top-menu.ant-menu-horizontal > .ant-menu-item::before) {
  display: none !important;
}

:deep(.ant-menu.top-menu.ant-menu-horizontal > .ant-menu-item .ant-menu-title-content) {
  transition: color 0.3s !important;
  text-align: center !important;
}

/* 确保内容居中且无圆角 */
:deep(.ant-menu.top-menu.ant-menu-horizontal > .ant-menu-item) {
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
  border-radius: 0 !important;
}

/* 移除所有可能的边框和圆角 */
:deep(.ant-menu.top-menu.ant-menu-horizontal:not(.ant-menu-dark) > .ant-menu-item),
:deep(.ant-menu.top-menu.ant-menu-horizontal:not(.ant-menu-dark) > .ant-menu-item:hover),
:deep(.ant-menu.top-menu.ant-menu-horizontal:not(.ant-menu-dark) > .ant-menu-item-selected) {
  border: none !important;
  border-radius: 0 !important;
}

/* 覆盖任何可能的主题样式 */
:deep(.ant-menu.top-menu.ant-menu-horizontal.ant-menu-light > .ant-menu-item-selected),
:deep(.ant-menu.top-menu.ant-menu-horizontal.ant-menu-light > .ant-menu-item.ant-menu-item-selected) {
  color: #333333 !important;
  background-color: #f0f0f0 !important;
  border-radius: 0 !important;
}

.header-right {
  display: flex;
  align-items: center;
  padding: 0;
  margin-left: auto;
  height: 48px;
  width: 120px;
  min-width: 120px;
  max-width: 120px;
  position: relative;
  
  .username {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    padding: 0 12px;
    height: 48px;
    cursor: pointer;
    border-radius: 0;
    transition: all 0.3s;
    color: #ffffff;
    width: 100%;
    min-width: 120px;
    margin: 0;
    
    &:hover {
      background-color: rgba(255, 255, 255, 0.1);
      color: #ffffff;

      .anticon {
        color: #ffffff;
      }
    }
    
    .anticon {
      font-size: 14px;
      color: #ffffff;
      transition: all 0.3s;
      display: flex;
      align-items: center;
    }

    span {
      margin-left: 4px;
      font-size: 14px;
      line-height: 1;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    .anticon-down {
      margin-left: 4px;
    }
  }

  .custom-dropdown {
    position: absolute;
    top: 48px;
    right: 0;
    z-index: 1000;
    width: 120px;
    background: #ffffff;
    border-radius: 4px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
    padding: 4px 0;

    .dropdown-menu {
      width: 100%;
    }

    .dropdown-item {
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 8px 12px;
      cursor: pointer;
      transition: all 0.3s;
      color: #333333;
      height: 32px;
      line-height: 16px;

      .anticon {
        font-size: 14px;
        margin: 0;
        display: flex;
        align-items: center;
      }

      span {
        margin-left: 4px;
        font-size: 14px;
        line-height: 1;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }

      &:hover {
        background-color: #f0f0f0;
        color: #333333;
      }
    }

    .dropdown-divider {
      height: 1px;
      background-color: #e6e6e6;
      margin: 4px 0;
    }
  }
}

/* 主体内容布局 */
.main-content-layout {
  margin-top: 48px;
  height: calc(100vh - 48px);
  display: flex;
  flex-direction: row;
  background: #f5f7fa !important;
}

/* 左侧区域样式 */
.sider {
  background: #ffffff !important;
  border-right: 1px solid #e8e8e8;
  overflow-y: auto;
  overflow-x: hidden;
  height: 100%;
  position: relative;
  transition: all 0.2s;
  z-index: 990;
  width: 200px !important;
  min-width: 200px !important;
  max-width: 200px !important;
  flex: 0 0 200px !important;
  box-sizing: border-box !important;
}

:deep(.ant-layout-sider) {
  background: #ffffff !important;
  min-width: 200px !important;
  max-width: 200px !important;
  width: 200px !important;
  flex: 0 0 200px !important;
  box-sizing: border-box !important;
}

:deep(.ant-layout-sider-children) {
  width: 200px !important;
  min-width: 200px !important;
  max-width: 200px !important;
  box-sizing: border-box !important;
}

:deep(.ant-layout-sider-collapsed) {
  min-width: 80px !important;
  max-width: 80px !important;
  width: 80px !important;
  flex: 0 0 80px !important;
}

:deep(.ant-layout-sider-collapsed .ant-layout-sider-children) {
  width: 80px !important;
  min-width: 80px !important;
  max-width: 80px !important;
}

/* 左侧菜单样式 */
:deep(.sider .ant-menu) {
  background: #ffffff !important;
  border-right: none !important;

  /* 所有左侧菜单项基础样式 */
  .ant-menu-item,
  .ant-menu-submenu-title {
    color: #333333 !important;
    background: #ffffff !important;
    margin: 0 !important;
    padding: 0 24px !important;
    height: 40px !important;
    line-height: 40px !important;
    width: 100% !important;
    border-radius: 0 !important;

    &:hover {
      color: #333333 !important;
      background-color: #f0f0f0 !important;
    }

    .anticon {
      color: #333333 !important;
    }
  }

  /* 有子级的菜单样式 */
  .ant-menu-submenu {
    .ant-menu-submenu-title {
      color: #333333 !important;
      background: #ffffff !important;
      margin: 0 !important;
      width: 100% !important;
      border-radius: 0 !important;

      &:hover {
        color: #333333 !important;
        background: #ffffff !important;
      }

      .ant-menu-submenu-arrow {
        color: #333333 !important;
      }
    }

    /* 展开时的样式 */
    &.ant-menu-submenu-open {
      > .ant-menu-submenu-title {
        color: #333333 !important;
        background: #ffffff !important;

        .ant-menu-submenu-arrow {
          color: #333333 !important;
        }
      }
    }
  }

  /* 没有子级的菜单选中样式 */
  .ant-menu-item-selected {
    color: #333333 !important;
    background-color: #f0f0f0 !important;
    margin: 0 !important;
    width: 100% !important;
    border-radius: 0 !important;

    &:hover {
      color: #333333 !important;
      background-color: #f0f0f0 !important;
    }

    .anticon {
      color: #333333 !important;
    }

    &::after {
      display: none !important;
    }
  }

  /* 子菜单内容区域 */
  .ant-menu-sub {
    background: #ffffff !important;

    .ant-menu-item {
      padding-left: 48px !important;
      color: #333333 !important;
      margin: 0 !important;
      width: 100% !important;
      border-radius: 0 !important;

      &:hover {
        color: #333333 !important;
        background-color: #f0f0f0 !important;
      }

      &.ant-menu-item-selected {
        color: #333333 !important;
        background-color: #f0f0f0 !important;
        
        &::after {
          display: none !important;
        }
      }
    }
  }
}

/* 折叠状态的左侧菜单 */
:deep(.ant-menu-inline-collapsed) {
  width: 80px !important;
  min-width: 80px !important;

  .ant-menu-item,
  .ant-menu-submenu .ant-menu-submenu-title {
    padding: 0 !important;
    text-align: center !important;
    margin: 0 !important;
    width: 80px !important;
    border-radius: 0 !important;
    display: flex !important;
    align-items: center !important;
    justify-content: center !important;

    .anticon {
      margin: 0 !important;
      font-size: 16px !important;
      line-height: 40px !important;
      color: #333333 !important;
    }

    .ant-menu-title-content {
      display: none !important;
    }

    .ant-menu-submenu-arrow {
      display: none !important;
    }
  }
}

/* 内容区域样式 */
.content {
  background: #f5f7fa !important;
  position: relative;
  padding: 0;
  transition: all 0.2s;
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  min-width: 0; /* 确保flex布局下内容可以被压缩 */
}

/* 工具栏样式 */
.toolbar {
  background: #ffffff;
  border-bottom: 1px solid #f0f0f0;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

.toolbar-left {
  display: flex;
  align-items: center;
  flex: 1;
  height: 100%;
  overflow: hidden;
  gap: 8px; /* 添加间距 */
}

.toolbar-right {
  display: flex;
  align-items: center;
  padding-right: 16px;
  height: 100%;
}

.sider-trigger {
  width: 40px;
  height: 40px;
  color: #1fa195 !important;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s;
  background: #f9f9f9;
  border-right: 1px solid #f0f0f0;
  padding: 0;
  font-size: 18px;
  flex-shrink: 0;
}

.sider-trigger:hover {
  color: #52c4b7 !important;
  background-color: #f0f7f7 !important;
}

/* 标签页样式 */
:deep(.page-tabs) {
  flex: 1;
  height: 100%;
  margin-left: 0;
  overflow: hidden;

  .ant-tabs-nav {
    margin: 0;
    background: transparent;
    height: 100%;
  }

  .ant-tabs-nav-wrap {
    height: 100%;
  }

  .ant-tabs-nav-list {
    height: 100%;
    display: flex;
    align-items: center;
    padding: 0 2px; /* 减小内边距 */
  }

  .ant-tabs-tab {
    padding: 0 12px;
    height: 32px;
    line-height: 32px;
    background: transparent;
    border: none;
    margin: 0 1px;
    border-radius: 0;
    transition: all 0.3s;
    display: flex;
    align-items: center;
    position: relative;

    &:hover {
      color: #333333;
      background-color: #f0f0f0;
    }

    .ant-tabs-tab-btn {
      font-size: 14px;
    }

    /* 添加底部边框 */
    &::after {
      content: '';
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      height: 2px;
      background-color: transparent;
      transition: all 0.3s;
    }
  }

  .ant-tabs-tab-active {
    background: #f0f0f0;

    .ant-tabs-tab-btn {
      color: #333333;
    }

    /* 激活状态显示底部边框 */
    &::after {
      background-color: #27c2ad;
    }
  }

  .ant-tabs-tab-remove {
    color: #999;
    margin-left: 4px;
    transition: all 0.3s;
    font-size: 12px;
    opacity: 1;
    width: 16px;
    height: 16px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 50%;

    &:hover {
      color: #ff4d4f;
      background-color: rgba(255, 77, 79, 0.1);
    }
  }

  .ant-tabs-tab:hover .ant-tabs-tab-remove {
    opacity: 1;
  }

  .ant-tabs-tab-active .ant-tabs-tab-remove {
    color: #999;
    opacity: 1;

    &:hover {
      color: #ff4d4f;
      background-color: rgba(255, 77, 79, 0.1);
    }
  }

  .ant-tabs-ink-bar {
    display: none;
  }
}

/* 调整内容区域样式 */
.content-wrapper {
  margin: 8px 24px 24px 24px;
  padding: 24px;
  background: #ffffff;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
  flex: 1;
  overflow: auto;
}

.tab-actions {
  display: flex;
  align-items: center;
  height: 100%;
  border-left: 1px solid #f0f0f0;
  padding-left: 16px;
  margin-left: 16px;

  .action-button {
    width: 32px;
    height: 32px;
    padding: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #666;
    border-radius: 4px;
    transition: all 0.3s;

    &:hover {
      color: #27c2ad;
      background: rgba(39, 194, 173, 0.1);
    }

    .anticon {
      font-size: 16px;
    }
  }
}

:deep(.ant-dropdown-menu) {
  padding: 4px 0;
  min-width: 160px;

  .ant-dropdown-menu-item {
    padding: 8px 16px;
    display: flex;
    align-items: center;
    gap: 8px;
    transition: all 0.3s;

    .anticon {
      font-size: 14px;
    }

    &:hover {
      background: #f5f5f5;
    }
  }

  .ant-dropdown-menu-item-divider {
    margin: 4px 0;
  }
}
</style> 