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
          <a-menu-item key="system">系统管理</a-menu-item>
          <a-menu-item key="business">业务平台</a-menu-item>
        </a-menu>
      </div>
      <div class="header-right">
        <a-dropdown>
          <span class="username">
            admin
            <down-outlined />
          </span>
          <template #overlay>
            <a-menu>
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
        >
          <template v-for="item in currentMenuItems" :key="item.key">
            <a-sub-menu v-if="item.children" :key="item.key" :selectable="false" @click="handleSubMenuClick(item)">
              <template #title>
                <span>
                  <component :is="item.icon" />
                  <span>{{ item.title }}</span>
                </span>
              </template>
              <template v-for="child in item.children" :key="child.key">
                <a-sub-menu v-if="child.children" :key="`sub-${child.key}`" :selectable="false" @click="handleSubMenuClick(child)">
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
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import {
  UserOutlined,
  SettingOutlined,
  DashboardOutlined,
  TeamOutlined,
  AppstoreOutlined,
  DownOutlined,
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

interface MenuItem {
  key: string
  title: string
  icon?: any
  path?: string
  children?: MenuItem[]
}

const router = useRouter()
const collapsed = ref(false)
const selectedKeys = ref(['1'])
const openKeys = ref(['systemManagement'])
const selectedTopKeys = ref(['system'])

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

// 处理菜单选择
const handleMenuSelect = ({ key }: { key: string }) => {
  // 移除 key 前缀以获取实际的菜单 key
  const actualKey = key.replace(/^(item-|sub-)/, '')
  const selectedItem = findMenuItem(currentMenuItems.value, actualKey)
  
  if (selectedItem && !selectedItem.children) {
    // 只有没有子菜单的项才能被选中
    selectedKeys.value = [key]
    if (selectedItem.path) {
      router.push(selectedItem.path)
    }
  } else {
    // 如果有子菜单，则清除选中状态
    selectedKeys.value = []
  }
}

// 处理菜单展开/收起
const handleOpenChange = (keys: string[]) => {
  const lastKey = keys[keys.length - 1]
  const menuItem = findMenuItem(currentMenuItems.value, lastKey)
  
  if (menuItem && menuItem.children) {
    // 只处理有子菜单的菜单项
    openKeys.value = keys
    // 清除选中状态
    selectedKeys.value = []
  }
}

// 处理子菜单点击
const handleSubMenuClick = (item: MenuItem) => {
  // 如果点击的是有子菜单的项，清除选中状态
  if (item.children) {
    selectedKeys.value = []
  }
}

// 处理顶部菜单选择
const handleTopMenuSelect = ({ key }: { key: string }) => {
  // 重置左侧菜单选中状态
  selectedKeys.value = []
  openKeys.value = []
  
  // 获取新菜单的第一个可点击项
  const firstMenuItem = findFirstMenuItem(currentMenuItems.value)
  if (firstMenuItem) {
    // 设置选中状态
    selectedKeys.value = [`item-${firstMenuItem.key}`]
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
    if (parentKey) {
      openKeys.value = [parentKey]
    } else {
      // 如果没有父菜单，则清空展开的菜单
      openKeys.value = []
    }
  }
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

// 组件挂载时初始化
const initFirstMenuItem = () => {
  // 默认选中仪表盘
  selectedKeys.value = ['item-dashboard']
  // 如果有路径，进行路由跳转
  router.push('/dashboard')
}

// 组件挂载时初始化
initFirstMenuItem()

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
      // 这里添加退出登录的逻辑
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

// 组件挂载后修复样式
onMounted(() => {
  fixMenuStyles()
})

// 监听折叠状态变化，重新应用样式
watch(collapsed, () => {
  fixMenuStyles()
})
</script>

<style scoped>
.main-layout {
  min-height: 100vh;
  width: 100%;
  overflow-x: hidden;
}

/* Override any inline styles for second-level menu items */
:deep(.ant-menu-dark .ant-menu-submenu .ant-menu-item),
:deep(.ant-menu-dark .ant-menu-submenu li[role="menuitem"]) {
  padding-left: 43px !important;
}

/* 顶部区域样式 */
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0;
  background: #001529;
  height: 48px;
  min-height: 48px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
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
}

.logo-area {
  background: #102a40;
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
  border-right: 1px solid #1e3c54;
  box-shadow: inset 0 -1px 0 0 #1e3c54;
}

.logo-collapsed {
  width: 80px !important;
  min-width: 80px !important;
  max-width: 80px !important;
}

.logo {
  color: #27c2ad;
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
  background: #001529;
  color: #fff;
  min-width: 0;
  margin-left: 0;
  padding-left: 0;
}

.header-right {
  height: 48px;
  display: flex;
  align-items: center;
  padding: 0 24px;
  color: #fff;
  border-radius: 0;
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
  background: #001529;
  overflow-y: auto;
  overflow-x: hidden;
  height: 100%;
  position: relative;
  transition: all 0.2s;
  z-index: 990;
}

:deep(.ant-layout-sider) {
  background: #001529 !important;
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
  color: #27c2ad;
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
  color: #27c2ad;
  opacity: 0.8;
}

/* 顶部菜单样式 */
:deep(.ant-menu-horizontal) {
  background: #001529;
  color: #fff;
  border-bottom: none;
  line-height: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  max-width: 400px;
}

:deep(.ant-menu-horizontal > .ant-menu-item) {
  color: #fff;
  margin: 0;
  padding: 0 40px;
  height: 48px;
  line-height: 48px;
  text-align: center;
  font-size: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
}

:deep(.ant-menu-horizontal > .ant-menu-item-selected) {
  background-color: #27c2ad !important;
  color: #fff !important;
}

:deep(.ant-menu-horizontal > .ant-menu-item:hover) {
  background-color: #27c2ad !important;
  color: #fff !important;
  opacity: 0.8;
}

/* 左侧菜单样式 */
:deep(.ant-menu-dark) {
  background: #001529;
  margin-top: 0 !important;
}

:deep(.ant-menu-dark > .ant-menu-item),
:deep(.ant-menu-dark > .ant-menu-submenu > .ant-menu-submenu-title) {
  padding-left: 15px !important;
  height: 48px !important;
  line-height: 48px !important;
  text-align: left !important;
  margin: 4px 0 !important;
}

:deep(.ant-menu-dark > .ant-menu-item .anticon),
:deep(.ant-menu-dark > .ant-menu-submenu > .ant-menu-submenu-title .anticon) {
  margin-right: 8px !important;
  font-size: 16px !important;
  min-width: 16px !important;
}

:deep(.ant-menu-dark > .ant-menu-item .ant-menu-title-content),
:deep(.ant-menu-dark > .ant-menu-submenu > .ant-menu-submenu-title .ant-menu-title-content) {
  display: inline-block !important;
  text-align: left !important;
  padding-left: 0 !important;
  margin-left: 0 !important;
}

:deep(.ant-menu-dark .ant-menu-submenu > .ant-menu-item) {
  padding-left: 43px !important;
  margin: 4px 0 !important;
  text-align: left !important;
  height: 32px !important;
  line-height: 32px !important;
}

:deep(.ant-menu-dark .ant-menu-submenu > .ant-menu-item .anticon) {
  margin-right: 8px !important;
  font-size: 14px !important;
  min-width: 14px !important;
}

:deep(.ant-menu-dark .ant-menu-submenu > .ant-menu-item .ant-menu-title-content) {
  display: block !important;
  text-align: left !important;
  padding-left: 0 !important;
  margin-left: 0 !important;
  width: 100% !important;
}

:deep(.ant-menu-dark .ant-menu-submenu .ant-menu-submenu > .ant-menu-item) {
  padding-left: 63px !important;
  margin: 4px 0 !important;
  text-align: left !important;
  height: 32px !important;
  line-height: 32px !important;
}

:deep(.ant-menu-dark .ant-menu-submenu .ant-menu-submenu > .ant-menu-item .ant-menu-title-content) {
  display: block !important;
  text-align: left !important;
  padding-left: 0 !important;
  margin-left: 0 !important;
  width: 100% !important;
}

/* 确保菜单项内容左对齐 */
:deep(.ant-menu-item),
:deep(.ant-menu-submenu-title) {
  text-align: left !important;
  justify-content: flex-start !important;
}

/* 移除菜单项的居中对齐 */
:deep(.ant-menu-item-selected),
:deep(.ant-menu-submenu-selected) {
  text-align: left !important;
  justify-content: flex-start !important;
}

/* 移除所有菜单的蓝色过渡效果 */
:deep(.ant-menu-item::after),
:deep(.ant-menu-submenu-title::after) {
  display: none !important;
}

:deep(.ant-menu-item-selected::after),
:deep(.ant-menu-submenu-selected::after) {
  display: none !important;
}

/* 移除所有默认的过渡动画 */
:deep(.ant-menu-item),
:deep(.ant-menu-submenu),
:deep(.ant-menu-submenu-title) {
  transition: none !important;
  border-radius: 0 !important;
}

/* 移除所有hover和active状态的默认样式 */
:deep(.ant-menu-item:hover),
:deep(.ant-menu-item:active),
:deep(.ant-menu-submenu-title:hover),
:deep(.ant-menu-submenu-title:active) {
  background-color: #27c2ad !important;
  color: #fff !important;
  opacity: 0.8;
  border-radius: 0 !important;
}

/* 移除所有focus状态的默认样式 */
:deep(.ant-menu-item:focus),
:deep(.ant-menu-submenu-title:focus) {
  background-color: #27c2ad !important;
  color: #fff !important;
  border-radius: 0 !important;
}

/* 移除所有默认的边框和轮廓 */
:deep(.ant-menu-item),
:deep(.ant-menu-submenu-title) {
  outline: none !important;
  border: none !important;
  box-shadow: none !important;
  border-radius: 0 !important;
}

/* 移除子菜单的圆角 */
:deep(.ant-menu-submenu-popup) {
  border-radius: 0 !important;
}

:deep(.ant-menu-submenu-popup .ant-menu-item) {
  border-radius: 0 !important;
}

/* 移除所有菜单项的圆角 */
:deep(.ant-menu-item),
:deep(.ant-menu-submenu-title),
:deep(.ant-menu-submenu),
:deep(.ant-menu-submenu-popup),
:deep(.ant-menu-submenu-popup .ant-menu-item) {
  border-radius: 0 !important;
}

/* 控制父级菜单的选中状态 */
:deep(.ant-menu-submenu-selected) {
  background-color: transparent !important;
  color: #fff !important;
}

:deep(.ant-menu-submenu-selected > .ant-menu-submenu-title) {
  background-color: transparent !important;
  color: #fff !important;
}
</style> 