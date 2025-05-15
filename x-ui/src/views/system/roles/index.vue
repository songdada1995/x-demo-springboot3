<template>
  <div class="roles-container">
    <a-card :bordered="false">
      <!-- 搜索区域 -->
      <div class="search-area">
        <a-row :gutter="[16, 16]">
          <a-col :span="6">
            <a-form-item label="角色名称">
              <a-input v-model:value="searchForm.name" placeholder="请输入角色名称" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="角色编码">
              <a-input v-model:value="searchForm.code" placeholder="请输入角色编码" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="状态">
              <a-select v-model:value="searchForm.status" placeholder="请选择状态">
                <a-select-option value="">全部</a-select-option>
                <a-select-option value="1">启用</a-select-option>
                <a-select-option value="0">禁用</a-select-option>
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
                :show-time="false"
                format="YYYY-MM-DD"
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
        row-key="id"
        @change="handleTableChange"
      >
        <!-- 状态列 -->
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <a-tag :color="record.status === 1 ? 'green' : 'red'">
              {{ record.status === 1 ? '启用' : '禁用' }}
            </a-tag>
          </template>
          <!-- 操作列 -->
          <template v-if="column.key === 'action'">
            <div class="action-column">
              <a class="action-link" @click="handleEdit(record)">编辑</a>
              <a-divider type="vertical" />
              <a class="action-link" @click="handlePermission(record)">权限设置</a>
              <a-divider type="vertical" />
              <a-popconfirm
                title="确定要删除这个角色吗？"
                @confirm="handleDelete(record)"
                okText="确定"
                cancelText="取消"
                :okButtonProps="{ type: 'primary', danger: true }"
                :cancelButtonProps="{ type: 'default' }"
              >
                <a class="action-danger">删除</a>
              </a-popconfirm>
            </div>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 角色表单对话框 -->
    <a-modal
      v-model:open="modalVisible"
      :title="modalTitle"
      @ok="handleModalOk"
      @cancel="handleModalCancel"
      width="600px"
      :maskClosable="false"
      :destroyOnClose="true"
      okText="确定"
      cancelText="取消"
    >
      <a-form
        ref="formRef"
        :model="formState"
        :rules="rules"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
      >
        <a-form-item label="角色名称" name="name">
          <a-input v-model:value="formState.name" placeholder="请输入角色名称" />
        </a-form-item>
        <a-form-item label="角色编码" name="code">
          <a-input v-model:value="formState.code" placeholder="请输入角色编码" />
        </a-form-item>
        <a-form-item label="状态" name="status">
          <a-radio-group v-model:value="formState.status">
            <a-radio :value="1">启用</a-radio>
            <a-radio :value="0">禁用</a-radio>
          </a-radio-group>
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
import zhCN from 'ant-design-vue/es/locale/zh_CN'
import type { Locale } from 'ant-design-vue/es/locale-provider'
import { createDefaultPagination } from '../../../utils/pagination'

// 搜索表单数据
const searchForm = reactive({
  name: '',
  code: '',
  status: '',
  createTime: [],
})

// 日期选择器中文配置
const locale = zhCN

// 表格列定义
const columns = [
  {
    title: '角色名称',
    dataIndex: 'name',
    key: 'name',
    width: '20%',
  },
  {
    title: '角色编码',
    dataIndex: 'code',
    key: 'code',
    width: '20%',
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    width: '10%',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime',
    width: '20%',
  },
  {
    title: '操作',
    key: 'action',
    width: '30%',
  },
]

// 表格数据
const dataSource = ref([
  {
    id: 1,
    name: '超级管理员',
    code: 'admin',
    status: 1,
    createTime: '2024-01-01 12:00:00',
    description: '系统超级管理员',
  },
  {
    id: 2,
    name: '普通用户',
    code: 'user',
    status: 1,
    createTime: '2024-01-01 12:00:00',
    description: '普通用户',
  },
  {
    id: 3,
    name: '访客',
    code: 'guest',
    status: 0,
    createTime: '2024-01-01 12:00:00',
    description: '访客用户',
  },
])

// 加载状态
const loading = ref(false)

// 分页配置
const pagination = reactive<TablePaginationConfig>(createDefaultPagination())

// 选中的行
const selectedRowKeys = ref<number[]>([])

// 表单相关
const modalVisible = ref(false)
const modalTitle = ref('新增角色')
const formRef = ref()
const formState = reactive({
  id: undefined,
  name: '',
  code: '',
  status: 1,
  description: '',
})

// 表单验证规则
const rules = {
  name: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  code: [{ required: true, message: '请输入角色编码', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }],
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

// 表格选择变化
const onSelectChange = (keys: number[]) => {
  selectedRowKeys.value = keys
}

// 新增角色
const handleAdd = () => {
  modalTitle.value = '新增角色'
  formState.id = undefined
  formState.name = ''
  formState.code = ''
  formState.status = 1
  formState.description = ''
  modalVisible.value = true
}

// 编辑角色
const handleEdit = (record: any) => {
  modalTitle.value = '编辑角色'
  Object.assign(formState, record)
  modalVisible.value = true
}

// 设置权限
const handlePermission = (record: any) => {
  message.success(`设置角色 ${record.name} 的权限成功`)
}

// 删除角色
const handleDelete = (record: any) => {
  message.success(`删除角色 ${record.name} 成功`)
  fetchData()
}

// 重置搜索
const handleReset = () => {
  searchForm.name = ''
  searchForm.code = ''
  searchForm.status = ''
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
.roles-container {
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