<template>
  <div class="users-container">
    <a-card :bordered="false">
      <!-- 搜索区域 -->
      <div class="search-area">
        <a-row :gutter="[16, 16]">
          <a-col :span="6">
            <a-form-item label="用户名">
              <a-input v-model:value="searchForm.username" placeholder="请输入用户名" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="姓名">
              <a-input v-model:value="searchForm.name" placeholder="请输入姓名" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="手机号">
              <a-input v-model:value="searchForm.phone" placeholder="请输入手机号" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="邮箱">
              <a-input v-model:value="searchForm.email" placeholder="请输入邮箱" />
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
                :picker-locale="{
                  lang: {
                    ...locale.lang,
                    monthFormat: 'M月',
                    yearFormat: 'YYYY年',
                    dateFormat: 'YYYY年M月D日',
                    dayFormat: 'D日',
                    dateTimeFormat: 'YYYY年M月D日',
                    previousMonth: '上个月',
                    nextMonth: '下个月',
                    previousYear: '上一年',
                    nextYear: '下一年',
                    today: '今天',
                    now: '此刻',
                    ok: '确定'
                  }
                }"
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
        :row-selection="{
          selectedRowKeys,
          onChange: handleSelectionChange
        }"
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
              <a class="action-link" @click="handleResetPassword(record)">重置密码</a>
              <a-divider type="vertical" />
              <a-popconfirm
                title="确定要删除这个用户吗？"
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

    <!-- 用户表单对话框 -->
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
        <a-form-item label="用户名" name="username">
          <a-input v-model:value="formState.username" placeholder="请输入用户名" />
        </a-form-item>
        <a-form-item label="姓名" name="name">
          <a-input v-model:value="formState.name" placeholder="请输入姓名" />
        </a-form-item>
        <a-form-item label="手机号" name="phone">
          <a-input v-model:value="formState.phone" placeholder="请输入手机号" />
        </a-form-item>
        <a-form-item label="邮箱" name="email">
          <a-input v-model:value="formState.email" placeholder="请输入邮箱" />
        </a-form-item>
        <a-form-item label="角色" name="roles">
          <a-select
            v-model:value="formState.roles"
            mode="multiple"
            placeholder="请选择角色"
          >
            <a-select-option value="admin">超级管理员</a-select-option>
            <a-select-option value="user">普通用户</a-select-option>
            <a-select-option value="guest">访客</a-select-option>
          </a-select>
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
import zhCN from 'ant-design-vue/es/date-picker/locale/zh_CN'
import { createDefaultPagination } from '../../../utils/pagination'

// 日期选择器中文配置
const locale = zhCN

// 分页本地化配置
const paginationLocale = {
  items_per_page: '条/页',
  jump_to: '跳至',
  jump_to_confirm: '确定',
  page: '页',
  prev_page: '上一页',
  next_page: '下一页',
  prev_5: '向前 5 页',
  next_5: '向后 5 页',
  prev_3: '向前 3 页',
  next_3: '向后 3 页',
}

// 搜索表单数据
const searchForm = reactive({
  username: '',
  name: '',
  phone: '',
  email: '',
  status: '',
  createTime: [],
})

// 选中的行
const selectedRowKeys = ref<number[]>([])

// 表格列定义
const columns = [
  {
    title: '用户名',
    dataIndex: 'username',
    key: 'username',
    width: 120,
  },
  {
    title: '姓名',
    dataIndex: 'name',
    key: 'name',
    width: 100,
  },
  {
    title: '手机号',
    dataIndex: 'phone',
    key: 'phone',
    width: 120,
  },
  {
    title: '邮箱',
    dataIndex: 'email',
    key: 'email',
    width: 180,
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    width: 80,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime',
    width: 160,
  },
  {
    title: '操作',
    key: 'action',
    fixed: 'right',
    width: 240,
  },
]

// 表格数据
const dataSource = ref([
  {
    id: 1,
    username: 'admin',
    name: '管理员',
    phone: '13800138000',
    email: 'admin@example.com',
    roles: ['admin'],
    status: 1,
    createTime: '2024-01-01 12:00:00',
    description: '系统管理员',
  },
  {
    id: 2,
    username: 'user1',
    name: '张三',
    phone: '13800138001',
    email: 'user1@example.com',
    roles: ['user'],
    status: 1,
    createTime: '2024-01-01 12:00:00',
    description: '普通用户',
  },
  {
    id: 3,
    username: 'guest1',
    name: '李四',
    phone: '13800138002',
    email: 'guest1@example.com',
    roles: ['guest'],
    status: 0,
    createTime: '2024-01-01 12:00:00',
    description: '访客用户',
  },
])

// 加载状态
const loading = ref(false)

// 分页配置
const pagination = reactive<TablePaginationConfig>(createDefaultPagination())

// 表单相关
const modalVisible = ref(false)
const modalTitle = ref('新增用户')
const formRef = ref()
const formState = reactive({
  id: undefined,
  username: '',
  name: '',
  phone: '',
  email: '',
  roles: [],
  status: 1,
  description: '',
})

// 表单验证规则
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }],
  roles: [{ required: true, message: '请选择角色', trigger: 'change' }],
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
  const id = Number(selectedRowKeys.value[0])
  const record = dataSource.value.find(item => item.id === id)
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

// 新增用户
const handleAdd = () => {
  modalTitle.value = '新增用户'
  formState.id = undefined
  formState.username = ''
  formState.name = ''
  formState.phone = ''
  formState.email = ''
  formState.roles = []
  formState.status = 1
  formState.description = ''
  modalVisible.value = true
}

// 编辑用户
const handleEdit = (record: any) => {
  modalTitle.value = '编辑用户'
  Object.assign(formState, record)
  modalVisible.value = true
}

// 重置密码
const handleResetPassword = (record: any) => {
  message.success(`重置用户 ${record.username} 的密码成功`)
}

// 删除用户
const handleDelete = (record: any) => {
  message.success(`删除用户 ${record.username} 成功`)
  fetchData()
}

// 重置搜索
const handleReset = () => {
  searchForm.username = ''
  searchForm.name = ''
  searchForm.phone = ''
  searchForm.email = ''
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

// 处理选择变化
const handleSelectionChange = (keys: number[]) => {
  selectedRowKeys.value = keys
}

// 初始化
onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.users-container {
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

/* 使用全局样式 */
:deep(.action-column) {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 8px;
}

:deep(.action-link) {
  color: #27c2ad;
  transition: all 0.3s;
}

:deep(.action-link:hover) {
  color: #2ed3bd;
}

:deep(.action-danger) {
  color: #ff4d4f;
}

:deep(.action-danger:hover) {
  color: #ff7875;
}

:deep(.ant-modal-content) {
  border-radius: 8px;
  overflow: hidden;
}

:deep(.ant-modal-header) {
  padding: 16px 24px;
  border-bottom: 1px solid #f0f0f0;
}

:deep(.ant-modal-title) {
  font-weight: 600;
  font-size: 16px;
}

:deep(.ant-modal-body) {
  padding: 24px;
}

:deep(.ant-modal-footer) {
  padding: 10px 16px;
  border-top: 1px solid #f0f0f0;
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

.action-column {
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
  flex-wrap: nowrap;
  white-space: nowrap;
}

.action-column a {
  margin: 0 4px;
}

/* 分页组件样式优化 */
:deep(.ant-pagination) {
  margin-top: 16px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

:deep(.ant-pagination-options) {
  margin-left: 16px;
}

:deep(.ant-pagination-options-size-changer) {
  margin-right: 8px;
  width: 110px;
}

:deep(.ant-pagination-options-size-changer .ant-select-selector) {
  padding-right: 8px !important;
}

:deep(.ant-pagination-options-quick-jumper) {
  margin-right: 0;
}

:deep(.ant-pagination-options-quick-jumper input) {
  width: 50px;
  margin: 0 8px;
}

:deep(.ant-select-dropdown) {
  min-width: 100px !important;
  width: auto !important;
}

:deep(.ant-select-item-option-content) {
  white-space: nowrap;
}

:deep(.ant-select-dropdown .ant-select-item) {
  padding: 5px 12px;
  min-height: 32px;
}

/* 确保下拉菜单不会太宽 */
:deep(.ant-select-dropdown) {
  max-width: 120px;
}
</style> 