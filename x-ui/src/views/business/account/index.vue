<template>
  <div class="account-container">
    <a-card :bordered="false">
      <!-- 搜索区域 -->
      <div class="search-area">
        <a-row :gutter="[16, 16]">
          <a-col :span="6">
            <a-form-item label="账号名称">
              <a-input v-model:value="searchForm.accountName" placeholder="请输入账号名称" style="max-width: 200px;" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="账号类型">
              <a-select v-model:value="searchForm.accountType" placeholder="请选择账号类型" class="custom-select">
                <a-select-option value="">全部</a-select-option>
                <a-select-option value="1">个人账号</a-select-option>
                <a-select-option value="2">企业账号</a-select-option>
                <a-select-option value="3">系统账号</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="状态">
              <a-select v-model:value="searchForm.status" placeholder="请选择状态" class="custom-select">
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
        <!-- 状态列 -->
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <a-tag :color="record.status === 1 ? 'green' : 'red'">
              {{ record.status === 1 ? '启用' : '禁用' }}
            </a-tag>
          </template>
          <template v-if="column.key === 'accountType'">
            <a-tag :color="getAccountTypeColor(record.accountType)">
              {{ getAccountTypeText(record.accountType) }}
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
                title="确定要删除这个账号吗？"
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

    <!-- 账号表单对话框 -->
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
        <a-form-item label="账号名称" name="accountName">
          <a-input v-model:value="formState.accountName" placeholder="请输入账号名称" />
        </a-form-item>
        <a-form-item label="账号类型" name="accountType">
          <a-select v-model:value="formState.accountType" placeholder="请选择账号类型">
            <a-select-option value="1">个人账号</a-select-option>
            <a-select-option value="2">企业账号</a-select-option>
            <a-select-option value="3">系统账号</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="联系人" name="contactPerson">
          <a-input v-model:value="formState.contactPerson" placeholder="请输入联系人" />
        </a-form-item>
        <a-form-item label="联系电话" name="contactPhone">
          <a-input v-model:value="formState.contactPhone" placeholder="请输入联系电话" />
        </a-form-item>
        <a-form-item label="电子邮箱" name="email">
          <a-input v-model:value="formState.email" placeholder="请输入电子邮箱" />
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

// 搜索表单数据
const searchForm = reactive({
  accountName: '',
  accountType: '',
  status: '',
  createTime: [],
})

// 表格列定义
const columns = [
  {
    title: '账号名称',
    dataIndex: 'accountName',
    key: 'accountName',
    width: 150,
  },
  {
    title: '账号类型',
    dataIndex: 'accountType',
    key: 'accountType',
    width: 100,
  },
  {
    title: '联系人',
    dataIndex: 'contactPerson',
    key: 'contactPerson',
    width: 100,
  },
  {
    title: '联系电话',
    dataIndex: 'contactPhone',
    key: 'contactPhone',
    width: 120,
  },
  {
    title: '电子邮箱',
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
    accountName: 'company001',
    accountType: '2',
    contactPerson: '张三',
    contactPhone: '13800138000',
    email: 'company001@example.com',
    status: 1,
    createTime: '2024-01-01 12:00:00',
    description: '企业账号',
  },
  {
    id: 2,
    accountName: 'person001',
    accountType: '1',
    contactPerson: '李四',
    contactPhone: '13800138001',
    email: 'person001@example.com',
    status: 1,
    createTime: '2024-01-01 12:00:00',
    description: '个人账号',
  },
  {
    id: 3,
    accountName: 'system001',
    accountType: '3',
    contactPerson: '系统管理员',
    contactPhone: '13800138002',
    email: 'system001@example.com',
    status: 0,
    createTime: '2024-01-01 12:00:00',
    description: '系统账号',
  },
])

// 账号类型颜色
const getAccountTypeColor = (type: string) => {
  const colors: Record<string, string> = {
    '1': 'blue',
    '2': 'green',
    '3': 'purple'
  }
  return colors[type] || 'default'
}

// 账号类型文本
const getAccountTypeText = (type: string) => {
  const texts: Record<string, string> = {
    '1': '个人账号',
    '2': '企业账号',
    '3': '系统账号'
  }
  return texts[type] || '未知类型'
}

// 加载状态
const loading = ref(false)

// 分页配置
const pagination = reactive<TablePaginationConfig>({
  current: 1,
  pageSize: 10,
  total: 3,
  showSizeChanger: true,
  showQuickJumper: true,
  showTotal: (total) => `共 ${total} 条`,
  pageSizeOptions: ['10', '20', '50', '100'],
})

// 选中的行
const selectedRowKeys = ref<string[]>([])

// 表单相关
const modalVisible = ref(false)
const modalTitle = ref('新增账号')
const formRef = ref()
const formState = reactive({
  id: undefined,
  accountName: '',
  accountType: '1',
  contactPerson: '',
  contactPhone: '',
  email: '',
  status: 1,
  description: '',
})

// 表单验证规则
const rules = {
  accountName: [{ required: true, message: '请输入账号名称', trigger: 'blur' }],
  accountType: [{ required: true, message: '请选择账号类型', trigger: 'change' }],
  contactPerson: [{ required: true, message: '请输入联系人', trigger: 'blur' }],
  contactPhone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
  email: [{ required: true, message: '请输入电子邮箱', trigger: 'blur' }],
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

// 处理选择变化
const onSelectChange = (keys: string[] | number[]) => {
  selectedRowKeys.value = keys.map(key => key.toString())
}

// 新增账号
const handleAdd = () => {
  modalTitle.value = '新增账号'
  formState.id = undefined
  formState.accountName = ''
  formState.accountType = '1'
  formState.contactPerson = ''
  formState.contactPhone = ''
  formState.email = ''
  formState.status = 1
  formState.description = ''
  modalVisible.value = true
}

// 编辑账号
const handleEdit = (record: any) => {
  modalTitle.value = '编辑账号'
  Object.assign(formState, record)
  modalVisible.value = true
}

// 重置密码
const handleResetPassword = (record: any) => {
  message.success(`重置账号 ${record.accountName} 的密码成功`)
}

// 删除账号
const handleDelete = (record: any) => {
  message.success(`删除账号 ${record.accountName} 成功`)
  fetchData()
}

// 重置搜索
const handleReset = () => {
  searchForm.accountName = ''
  searchForm.accountType = ''
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
.account-container {
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

/* 自定义下拉框样式 */
:deep(.custom-select .ant-select-dropdown) {
  background-color: #fff !important;
}

:deep(.custom-select .ant-select-item) {
  color: rgba(0, 0, 0, 0.85) !important;
}

:deep(.custom-select .ant-select-item-option-selected) {
  background-color: #f5f5f5 !important;
  color: rgba(0, 0, 0, 0.85) !important;
}

:deep(.custom-select .ant-select-item-option-active) {
  background-color: #e6f7f5 !important;
}
</style> 