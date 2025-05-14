<template>
  <div class="permissions-container">
    <a-card :bordered="false">
      <!-- 搜索区域 -->
      <div class="search-area">
        <a-row :gutter="[16, 16]">
          <a-col :span="6">
            <a-form-item label="权限名称">
              <a-input v-model:value="searchForm.name" placeholder="请输入权限名称" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="权限编码">
              <a-input v-model:value="searchForm.code" placeholder="请输入权限编码" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="权限类型">
              <a-select v-model:value="searchForm.type" placeholder="请选择权限类型">
                <a-select-option value="">全部</a-select-option>
                <a-select-option value="menu">菜单</a-select-option>
                <a-select-option value="button">按钮</a-select-option>
                <a-select-option value="api">接口</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="创建时间">
              <a-range-picker
                v-model:value="searchForm.createTime"
                style="width: 240px"
                :locale="locale"
                :placeholder="['开始时间', '结束时间']"
                show-time
              />
            </a-form-item>
          </a-col>
        </a-row>
      </div>

      <!-- 操作按钮区域 -->
      <div class="table-operations">
        <a-space>
          <a-button type="primary" class="theme-button" @click="handleSearch">
            <template #icon><search-outlined /></template>
            搜索
          </a-button>
          <a-button type="primary" class="theme-button" @click="handleExport">
            <template #icon><export-outlined /></template>
            导出
          </a-button>
          <a-button type="primary" class="theme-button" @click="handleAdd">
            <template #icon><plus-outlined /></template>
            新增
          </a-button>
          <a-button type="primary" class="theme-button" @click="handleBatchEdit">
            <template #icon><edit-outlined /></template>
            修改
          </a-button>
          <a-button danger @click="handleBatchDelete">
            <template #icon><delete-outlined /></template>
            删除
          </a-button>
          <a-button @click="handleReset">
            <template #icon><reload-outlined /></template>
            重置
          </a-button>
        </a-space>
      </div>

      <!-- 表格区域 -->
      <a-table
        :columns="columns"
        :data-source="dataSource"
        :loading="loading"
        :pagination="pagination"
        :row-selection="{ selectedRowKeys, onChange: onSelectChange }"
        @change="handleTableChange"
      >
        <!-- 权限类型列 -->
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'type'">
            <a-tag :color="getTypeColor(record.type)">
              {{ getTypeText(record.type) }}
            </a-tag>
          </template>
          <!-- 操作列 -->
          <template v-if="column.key === 'action'">
            <a-space size="middle">
              <a @click="handleEdit(record)">编辑</a>
              <a-divider type="vertical" />
              <a-popconfirm
                title="确定要删除这个权限吗？"
                @confirm="handleDelete(record)"
              >
                <a class="danger">删除</a>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 权限表单对话框 -->
    <a-modal
      v-model:open="modalVisible"
      :title="modalTitle"
      @ok="handleModalOk"
      @cancel="handleModalCancel"
    >
      <a-form
        ref="formRef"
        :model="formState"
        :rules="rules"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
      >
        <a-form-item label="权限名称" name="name">
          <a-input v-model:value="formState.name" placeholder="请输入权限名称" />
        </a-form-item>
        <a-form-item label="权限编码" name="code">
          <a-input v-model:value="formState.code" placeholder="请输入权限编码" />
        </a-form-item>
        <a-form-item label="权限类型" name="type">
          <a-select v-model:value="formState.type" placeholder="请选择权限类型">
            <a-select-option value="menu">菜单</a-select-option>
            <a-select-option value="button">按钮</a-select-option>
            <a-select-option value="api">接口</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="父级权限" name="parentId">
          <a-tree-select
            v-model:value="formState.parentId"
            :tree-data="permissionTree"
            placeholder="请选择父级权限"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="权限路径" name="path">
          <a-input v-model:value="formState.path" placeholder="请输入权限路径" />
        </a-form-item>
        <a-form-item label="权限图标" name="icon">
          <a-input v-model:value="formState.icon" placeholder="请输入权限图标" />
        </a-form-item>
        <a-form-item label="排序" name="sort">
          <a-input-number v-model:value="formState.sort" :min="0" :max="999" />
        </a-form-item>
        <a-form-item label="备注" name="description">
          <a-textarea
            v-model:value="formState.description"
            placeholder="请输入备注"
            :rows="4"
          />
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
  ExportOutlined,
  EditOutlined,
  DeleteOutlined,
} from '@ant-design/icons-vue'
import type { TablePaginationConfig } from 'ant-design-vue'
import { createDefaultPagination } from '../../../utils/pagination'

// 搜索表单数据
const searchForm = reactive({
  name: '',
  code: '',
  type: '',
  createTime: [],
})

// 表格列定义
const columns = [
  {
    title: '权限名称',
    dataIndex: 'name',
    key: 'name',
    width: '20%',
  },
  {
    title: '权限编码',
    dataIndex: 'code',
    key: 'code',
    width: '20%',
  },
  {
    title: '权限类型',
    dataIndex: 'type',
    key: 'type',
    width: '10%',
  },
  {
    title: '权限路径',
    dataIndex: 'path',
    key: 'path',
    width: '20%',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime',
    width: '10%',
  },
  {
    title: '操作',
    key: 'action',
    width: '20%',
  },
]

// 表格数据
const dataSource = ref([
  {
    id: 1,
    name: '系统管理',
    code: 'system',
    type: 'menu',
    path: '/system',
    icon: 'setting',
    sort: 1,
    parentId: null,
    createTime: '2024-01-01 12:00:00',
    description: '系统管理模块',
  },
  {
    id: 2,
    name: '用户管理',
    code: 'system:user',
    type: 'menu',
    path: '/system/user',
    icon: 'user',
    sort: 1,
    parentId: 1,
    createTime: '2024-01-01 12:00:00',
    description: '用户管理模块',
  },
  {
    id: 3,
    name: '新增用户',
    code: 'system:user:add',
    type: 'button',
    path: null,
    icon: null,
    sort: 1,
    parentId: 2,
    createTime: '2024-01-01 12:00:00',
    description: '新增用户按钮',
  },
])

// 权限树数据
const permissionTree = ref([
  {
    title: '系统管理',
    value: 1,
    children: [
      {
        title: '用户管理',
        value: 2,
      },
    ],
  },
])

// 加载状态
const loading = ref(false)

// 分页配置
const pagination = reactive<TablePaginationConfig>(createDefaultPagination())

// 选中的行
const selectedRowKeys = ref<string[]>([])

// 表单相关
const modalVisible = ref(false)
const modalTitle = ref('新增权限')
const formRef = ref()
const formState = reactive({
  id: undefined,
  name: '',
  code: '',
  type: 'menu',
  path: '',
  icon: '',
  sort: 0,
  parentId: null,
  description: '',
})

// 表单验证规则
const rules = {
  name: [{ required: true, message: '请输入权限名称', trigger: 'blur' }],
  code: [{ required: true, message: '请输入权限编码', trigger: 'blur' }],
  type: [{ required: true, message: '请选择权限类型', trigger: 'change' }],
}

// 获取权限类型颜色
const getTypeColor = (type: string) => {
  const colors = {
    menu: 'blue',
    button: 'green',
    api: 'purple',
  }
  return colors[type] || 'default'
}

// 获取权限类型文本
const getTypeText = (type: string) => {
  const texts = {
    menu: '菜单',
    button: '按钮',
    api: '接口',
  }
  return texts[type] || type
}

// 处理表格变化
const handleTableChange = (pag: TablePaginationConfig) => {
  pagination.current = pag.current || 1
  pagination.pageSize = pag.pageSize || 10
  fetchData()
}

// 获取数据
const fetchData = () => {
  loading.value = true
  // 这里模拟异步请求
  setTimeout(() => {
    loading.value = false
    pagination.total = dataSource.value.length
  }, 500)
}

// 处理搜索
const handleSearch = () => {
  pagination.current = 1
  fetchData()
}

// 处理导出
const handleExport = () => {
  message.success('导出成功')
}

// 处理批量编辑
const handleBatchEdit = () => {
  if (selectedRowKeys.value.length !== 1) {
    message.warning('请选择一条记录进行编辑')
    return
  }
  const record = dataSource.value.find(item => item.id === selectedRowKeys.value[0])
  if (record) {
    handleEdit(record)
  }
}

// 处理批量删除
const handleBatchDelete = () => {
  if (selectedRowKeys.value.length === 0) {
    message.warning('请选择要删除的记录')
    return
  }
  message.success(`删除选中的 ${selectedRowKeys.value.length} 条记录成功`)
  fetchData()
}

// 处理选择变化
const onSelectChange = (keys: string[]) => {
  selectedRowKeys.value = keys
}

// 新增权限
const handleAdd = () => {
  modalTitle.value = '新增权限'
  formState.id = undefined
  formState.name = ''
  formState.code = ''
  formState.type = 'menu'
  formState.path = ''
  formState.icon = ''
  formState.sort = 0
  formState.parentId = null
  formState.description = ''
  modalVisible.value = true
}

// 编辑权限
const handleEdit = (record: any) => {
  modalTitle.value = '编辑权限'
  Object.assign(formState, record)
  modalVisible.value = true
}

// 删除权限
const handleDelete = (record: any) => {
  message.success(`删除权限 ${record.name} 成功`)
  fetchData()
}

// 重置搜索
const handleReset = () => {
  searchForm.name = ''
  searchForm.code = ''
  searchForm.type = ''
  searchForm.createTime = []
  handleSearch()
}

// 模态框确认
const handleModalOk = () => {
  formRef.value?.validate().then(() => {
    message.success(`${modalTitle.value}成功`)
    modalVisible.value = false
    fetchData()
  })
}

// 模态框取消
const handleModalCancel = () => {
  modalVisible.value = false
}

// 初始化
onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.permissions-container {
  padding: 0;
}

.search-area {
  margin-bottom: 16px;
}

.table-operations {
  margin-bottom: 16px;
  display: flex;
  justify-content: flex-start;
}

.danger {
  color: #ff4d4f;
}

.danger:hover {
  color: #ff7875;
}

:deep(.ant-form-item) {
  margin-bottom: 16px;
  display: flex;
  align-items: center;
}

:deep(.ant-form-item-label) {
  padding-bottom: 0;
  width: 70px;
  text-align: right;
  padding-right: 8px;
  flex: 0 0 70px !important;
  max-width: 70px !important;
}

:deep(.ant-form-item-control) {
  width: 240px;
  margin-left: 0 !important;
  flex: 0 0 240px !important;
}

:deep(.ant-card-head) {
  min-height: 48px;
  padding: 0 16px;
}

:deep(.ant-card-body) {
  padding: 16px;
}

:deep(.ant-form-item-label > label) {
  height: 32px;
  line-height: 32px;
  padding-right: 0;
}

:deep(.ant-input),
:deep(.ant-select),
:deep(.ant-picker) {
  width: 240px;
}

:deep(.ant-picker-range) {
  width: 240px;
}

/* 响应式布局调整 */
@media screen and (max-width: 1200px) {
  :deep(.ant-form-item-control) {
    width: 220px;
    flex: 0 0 220px !important;
  }
  
  :deep(.ant-input),
  :deep(.ant-select),
  :deep(.ant-picker),
  :deep(.ant-picker-range) {
    width: 220px;
  }
}

@media screen and (max-width: 992px) {
  :deep(.ant-form-item-control) {
    width: 200px;
    flex: 0 0 200px !important;
  }
  
  :deep(.ant-input),
  :deep(.ant-select),
  :deep(.ant-picker),
  :deep(.ant-picker-range) {
    width: 200px;
  }
}

/* 覆盖 ant-design-vue 的默认样式 */
:deep(.ant-form-item-control-input) {
  margin-left: 0 !important;
  padding-left: 0 !important;
}

:deep(.ant-form-item-control-input-content) {
  margin-left: 0 !important;
  padding-left: 0 !important;
}

:deep(.ant-form-item-explain) {
  margin-left: 0 !important;
  padding-left: 0 !important;
}

:deep(.ant-form-item-extra) {
  margin-left: 0 !important;
  padding-left: 0 !important;
}

:deep(.ant-form-item-label-col) {
  flex: 0 0 70px !important;
  max-width: 70px !important;
}

:deep(.ant-form-item-control-col) {
  flex: 0 0 240px !important;
  max-width: 240px !important;
}

:deep(.ant-form-item-label-col > label) {
  padding-right: 0 !important;
  margin-right: 0 !important;
}

/* 日期选择器特殊处理 */
:deep(.ant-picker) {
  margin-left: 0 !important;
}

:deep(.ant-picker-range) {
  margin-left: 0 !important;
}

:deep(.ant-picker-input) {
  margin-left: 0 !important;
}

:deep(.ant-picker-separator) {
  margin: 0 4px !important;
}

:deep(.ant-picker-suffix) {
  margin-left: 4px !important;
}

/* 统一所有表单项的间距 */
:deep(.ant-form-item-label),
:deep(.ant-form-item-control),
:deep(.ant-form-item-label-col),
:deep(.ant-form-item-control-col) {
  margin-right: 0 !important;
  margin-left: 0 !important;
  padding-right: 0 !important;
  padding-left: 0 !important;
}

:deep(.ant-form-item-label) {
  margin-right: 8px !important;
}

:deep(.ant-form-item-control) {
  margin-left: 0 !important;
}

/* 确保所有输入框组件对齐 */
:deep(.ant-input),
:deep(.ant-select),
:deep(.ant-picker),
:deep(.ant-picker-range),
:deep(.ant-select-selector),
:deep(.ant-picker-input) {
  margin-left: 0 !important;
  padding-left: 0 !important;
}

/* 调整输入框内占位文字的边距 */
:deep(.ant-input) {
  padding-left: 8px !important;
}

:deep(.ant-select-selector) {
  padding-left: 8px !important;
}

:deep(.ant-picker-input) {
  padding-left: 8px !important;
}

:deep(.ant-picker-range .ant-picker-input) {
  padding-left: 8px !important;
}

:deep(.ant-select-selection-placeholder) {
  padding-left: 0 !important;
}

:deep(.ant-picker-input > input) {
  padding-left: 0 !important;
}

/* 移除自定义按钮样式 */
.primary-button {
  display: none;
}
</style> 