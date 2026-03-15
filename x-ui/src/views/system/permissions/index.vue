<template>
  <div class="menu-container">
    <a-card :bordered="false">
      <!-- 搜索区域 -->
      <div class="search-area">
        <a-row :gutter="[16, 16]">
          <a-col :xs="24" :sm="12" :md="8" :lg="6">
            <a-form-item label="菜单名称">
              <a-input v-model:value="searchForm.menuName" placeholder="请输入菜单名称" allow-clear />
            </a-form-item>
          </a-col>
          <a-col :xs="24" :sm="12" :md="8" :lg="6">
            <a-form-item label="状态">
              <a-select v-model:value="searchForm.status" placeholder="请选择状态" allow-clear>
                <a-select-option value="">全部</a-select-option>
                <a-select-option value="0">正常</a-select-option>
                <a-select-option value="1">停用</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-space>
              <a-button type="primary" class="theme-button" @click="handleSearch">
                <template #icon><search-outlined /></template>
                搜索
              </a-button>
              <a-button @click="handleReset">
                <template #icon><reload-outlined /></template>
                重置
              </a-button>
            </a-space>
          </a-col>
        </a-row>
      </div>

      <!-- 操作按钮 -->
      <div class="table-operations">
        <a-space wrap>
          <a-button type="primary" class="theme-button" @click="handleAdd(0)">
            <template #icon><plus-outlined /></template>
            新增菜单
          </a-button>
          <a-button @click="toggleExpandAll">
            <template #icon><menu-unfold-outlined v-if="!isExpandAll" /><menu-fold-outlined v-else /></template>
            {{ isExpandAll ? '全部折叠' : '全部展开' }}
          </a-button>
        </a-space>
      </div>

      <!-- 树形表格 -->
      <a-table
        :columns="columns"
        :data-source="treeData"
        :loading="loading"
        :pagination="false"
        v-model:expandedRowKeys="expandedKeys"
        row-key="menuId"
      >
        <template #bodyCell="{ column, record }">
          <!-- 菜单名称列：带图标 -->
          <template v-if="column.key === 'menuName'">
            <span>
              <folder-outlined v-if="record.menuType === 'M'" style="color: #faad14; margin-right: 6px;" />
              <file-outlined v-else-if="record.menuType === 'C'" style="color: #1890ff; margin-right: 6px;" />
              <api-outlined v-else style="color: #52c41a; margin-right: 6px;" />
              {{ record.menuName }}
            </span>
          </template>
          <!-- 类型 -->
          <template v-if="column.key === 'menuType'">
            <a-tag :color="typeColorMap[record.menuType]">{{ typeTextMap[record.menuType] }}</a-tag>
          </template>
          <!-- 图标 -->
          <template v-if="column.key === 'icon'">
            <span v-if="record.icon && record.icon !== '#'">{{ record.icon }}</span>
            <span v-else style="color: #ccc;">-</span>
          </template>
          <!-- 权限标识 -->
          <template v-if="column.key === 'perms'">
            <span v-if="record.perms">{{ record.perms }}</span>
            <span v-else style="color: #ccc;">-</span>
          </template>
          <!-- 状态 -->
          <template v-if="column.key === 'status'">
            <a-tag :color="record.status === '0' ? 'green' : 'red'">{{ record.status === '0' ? '正常' : '停用' }}</a-tag>
          </template>
          <!-- 排序 -->
          <template v-if="column.key === 'orderNum'">
            {{ record.orderNum }}
          </template>
          <!-- 操作 -->
          <template v-if="column.key === 'action'">
            <div class="action-column">
              <a class="action-link" v-if="record.menuType !== 'F'" @click="handleAdd(record.menuId)">新增</a>
              <a-divider type="vertical" v-if="record.menuType !== 'F'" />
              <a class="action-link" @click="handleEdit(record)">编辑</a>
              <a-divider type="vertical" />
              <a-popconfirm title="确定要删除此菜单吗？" @confirm="handleDelete(record)" okText="确定" cancelText="取消">
                <a class="action-danger">删除</a>
              </a-popconfirm>
            </div>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 菜单表单对话框 -->
    <a-modal
      v-model:open="modalVisible"
      :title="modalTitle"
      @ok="handleModalOk"
      @cancel="modalVisible = false"
      width="650px"
      :maskClosable="false"
      :destroyOnClose="true"
      okText="确定"
      cancelText="取消"
    >
      <a-form ref="formRef" :model="formState" :rules="rules" :label-col="{ span: 5 }" :wrapper-col="{ span: 17 }">
        <a-form-item label="上级菜单" name="parentId">
          <a-tree-select
            v-model:value="formState.parentId"
            :tree-data="parentTreeData"
            placeholder="选择上级菜单（不选则为顶级）"
            allow-clear
            :field-names="{ label: 'menuName', value: 'menuId', children: 'children' }"
            tree-default-expand-all
          />
        </a-form-item>
        <a-form-item label="菜单类型" name="menuType">
          <a-radio-group v-model:value="formState.menuType">
            <a-radio value="M">目录</a-radio>
            <a-radio value="C">菜单</a-radio>
            <a-radio value="F">按钮</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="菜单名称" name="menuName">
          <a-input v-model:value="formState.menuName" placeholder="请输入菜单名称" />
        </a-form-item>
        <a-form-item label="排序" name="orderNum">
          <a-input-number v-model:value="formState.orderNum" :min="0" :max="999" style="width: 100%;" />
        </a-form-item>
        <a-form-item label="路由地址" name="path" v-if="formState.menuType !== 'F'">
          <a-input v-model:value="formState.path" placeholder="请输入路由地址，如 users" />
        </a-form-item>
        <a-form-item label="组件路径" name="component" v-if="formState.menuType === 'C'">
          <a-input v-model:value="formState.component" placeholder="请输入组件路径，如 system/users/index" />
        </a-form-item>
        <a-form-item label="权限标识" name="perms" v-if="formState.menuType !== 'M'">
          <a-input v-model:value="formState.perms" placeholder="请输入权限标识，如 system:user:list" />
        </a-form-item>
        <a-form-item label="菜单图标" name="icon" v-if="formState.menuType !== 'F'">
          <a-input v-model:value="formState.icon" placeholder="请输入图标名称，如 setting、user" />
        </a-form-item>
        <a-form-item label="显示状态" name="visible" v-if="formState.menuType !== 'F'">
          <a-radio-group v-model:value="formState.visible">
            <a-radio value="0">显示</a-radio>
            <a-radio value="1">隐藏</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="菜单状态" name="status">
          <a-radio-group v-model:value="formState.status">
            <a-radio value="0">正常</a-radio>
            <a-radio value="1">停用</a-radio>
          </a-radio-group>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import {
  PlusOutlined,
  SearchOutlined,
  ReloadOutlined,
  FolderOutlined,
  FileOutlined,
  ApiOutlined,
  MenuUnfoldOutlined,
  MenuFoldOutlined,
} from '@ant-design/icons-vue'
import { menuApi } from '../../../api/menu'

const typeTextMap: Record<string, string> = { M: '目录', C: '菜单', F: '按钮' }
const typeColorMap: Record<string, string> = { M: 'orange', C: 'blue', F: 'green' }

const searchForm = reactive({ menuName: '', status: '' })

const columns = [
  { title: '菜单名称', dataIndex: 'menuName', key: 'menuName', width: 220 },
  { title: '类型', dataIndex: 'menuType', key: 'menuType', width: 80, align: 'center' as const },
  { title: '图标', dataIndex: 'icon', key: 'icon', width: 80, align: 'center' as const },
  { title: '排序', dataIndex: 'orderNum', key: 'orderNum', width: 60, align: 'center' as const },
  { title: '权限标识', dataIndex: 'perms', key: 'perms', width: 180 },
  { title: '路由地址', dataIndex: 'path', key: 'path', width: 120 },
  { title: '组件路径', dataIndex: 'component', key: 'component', width: 180 },
  { title: '状态', dataIndex: 'status', key: 'status', width: 80, align: 'center' as const },
  { title: '操作', key: 'action', width: 180, fixed: 'right' as const },
]

const treeData = ref<any[]>([])
const allMenus = ref<any[]>([])
const parentTreeData = ref<any[]>([])
const loading = ref(false)
const expandedKeys = ref<number[]>([])
const isExpandAll = ref(true)

const modalVisible = ref(false)
const modalTitle = ref('新增菜单')
const formRef = ref()
const formState = reactive({
  menuId: undefined as number | undefined,
  parentId: 0 as number | null,
  menuType: 'M' as string,
  menuName: '',
  orderNum: 0,
  path: '',
  component: '',
  perms: '',
  icon: '',
  visible: '0',
  status: '0',
})

const rules = {
  menuName: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
  menuType: [{ required: true, message: '请选择菜单类型', trigger: 'change' }],
  orderNum: [{ required: true, message: '请输入排序', trigger: 'blur' }],
}

const buildTree = (menus: any[], parentId: number = 0): any[] => {
  return menus
    .filter(m => m.parentId === parentId)
    .sort((a, b) => (a.orderNum || 0) - (b.orderNum || 0))
    .map(m => {
      const children = buildTree(menus, m.menuId)
      return children.length > 0 ? { ...m, children } : { ...m }
    })
}

const collectAllKeys = (tree: any[]): number[] => {
  const keys: number[] = []
  const walk = (nodes: any[]) => {
    for (const n of nodes) {
      if (n.children && n.children.length > 0) {
        keys.push(n.menuId)
        walk(n.children)
      }
    }
  }
  walk(tree)
  return keys
}

const fetchData = async () => {
  loading.value = true
  try {
    const res: any = await menuApi.list({
      menuName: searchForm.menuName || undefined,
      status: searchForm.status || undefined,
    })
    allMenus.value = res.data || []
    const tree = buildTree(allMenus.value)
    treeData.value = tree
    expandedKeys.value = collectAllKeys(tree)
    isExpandAll.value = true

    const rootNode = { menuId: 0, menuName: '根目录', children: [] as any[] }
    rootNode.children = buildTree(
      allMenus.value.filter(m => m.menuType === 'M' || m.menuType === 'C'),
    )
    parentTreeData.value = [rootNode]
  } catch (e: any) {
    message.error(e.message || '获取菜单列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => fetchData()
const handleReset = () => {
  searchForm.menuName = ''
  searchForm.status = ''
  fetchData()
}

const toggleExpandAll = () => {
  if (isExpandAll.value) {
    expandedKeys.value = []
    isExpandAll.value = false
  } else {
    expandedKeys.value = collectAllKeys(treeData.value)
    isExpandAll.value = true
  }
}

const resetForm = () => {
  formState.menuId = undefined
  formState.parentId = 0
  formState.menuType = 'M'
  formState.menuName = ''
  formState.orderNum = 0
  formState.path = ''
  formState.component = ''
  formState.perms = ''
  formState.icon = ''
  formState.visible = '0'
  formState.status = '0'
}

const handleAdd = (parentId: number) => {
  resetForm()
  formState.parentId = parentId || 0
  if (parentId > 0) {
    const parent = allMenus.value.find(m => m.menuId === parentId)
    if (parent?.menuType === 'M') {
      formState.menuType = 'C'
    } else if (parent?.menuType === 'C') {
      formState.menuType = 'F'
    }
  }
  modalTitle.value = '新增菜单'
  modalVisible.value = true
}

const handleEdit = (record: any) => {
  resetForm()
  formState.menuId = record.menuId
  formState.parentId = record.parentId || 0
  formState.menuType = record.menuType
  formState.menuName = record.menuName
  formState.orderNum = record.orderNum
  formState.path = record.path || ''
  formState.component = record.component || ''
  formState.perms = record.perms || ''
  formState.icon = record.icon || ''
  formState.visible = record.visible || '0'
  formState.status = record.status || '0'
  modalTitle.value = '编辑菜单'
  modalVisible.value = true
}

const handleDelete = async (record: any) => {
  try {
    await menuApi.remove(record.menuId)
    message.success(`删除菜单"${record.menuName}"成功`)
    fetchData()
  } catch (e: any) {
    message.error(e.message || '删除失败')
  }
}

const handleModalOk = () => {
  formRef.value?.validate().then(async () => {
    try {
      const data: any = {
        menuId: formState.menuId,
        parentId: formState.parentId || 0,
        menuType: formState.menuType,
        menuName: formState.menuName,
        orderNum: formState.orderNum,
        path: formState.path,
        component: formState.component || null,
        perms: formState.perms || null,
        icon: formState.icon || '#',
        visible: formState.visible,
        status: formState.status,
      }
      if (formState.menuId) {
        await menuApi.edit(data)
      } else {
        await menuApi.add(data)
      }
      message.success(`${modalTitle.value}成功`)
      modalVisible.value = false
      fetchData()
    } catch (e: any) {
      message.error(e.message || `${modalTitle.value}失败`)
    }
  })
}

onMounted(() => fetchData())
</script>

<style scoped>
.menu-container {
  padding: 0;
}

.search-area {
  margin-bottom: 16px;
}

.table-operations {
  margin-bottom: 16px;
}

:deep(.ant-form-item) {
  margin-bottom: 16px;
  display: flex;
  align-items: center;
}

:deep(.ant-form-item-label) {
  padding-bottom: 0;
}

:deep(.ant-card-body) {
  padding: 16px;
}

.action-column {
  display: flex;
  align-items: center;
  white-space: nowrap;
}

.action-link {
  color: #27c2ad;
  cursor: pointer;
}

.action-link:hover {
  color: #2ed3bd;
}

.action-danger {
  color: #ff4d4f;
  cursor: pointer;
}

.action-danger:hover {
  color: #ff7875;
}
</style>
