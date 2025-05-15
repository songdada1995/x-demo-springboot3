<template>
  <div class="cost-detail-container">
    <a-card class="cost-detail-card">
      <div class="search-form">
        <a-form layout="inline" :model="searchForm">
          <a-form-item label="成本类型">
            <a-select
              v-model:value="searchForm.costType"
              placeholder="请选择成本类型"
              style="width: 200px"
            >
              <a-select-option value="material">材料成本</a-select-option>
              <a-select-option value="labor">人工成本</a-select-option>
              <a-select-option value="overhead">制造费用</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item label="日期范围">
            <a-range-picker
              v-model:value="searchForm.dateRange"
              style="width: 300px"
            />
          </a-form-item>
          <a-form-item>
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
          </a-form-item>
        </a-form>
      </div>

      <a-table
        :columns="columns"
        :data-source="tableData"
        :loading="loading"
        :pagination="pagination"
        :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'action'">
            <a-space>
              <a @click="handleEdit(record)">编辑</a>
              <a-divider type="vertical" />
              <a-popconfirm
                title="确定要删除这条记录吗？"
                @confirm="handleDelete(record)"
              >
                <a>删除</a>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive } from 'vue'
import {
  SearchOutlined,
  ExportOutlined,
  PlusOutlined,
  EditOutlined,
  DeleteOutlined,
  ReloadOutlined,
} from '@ant-design/icons-vue'

// 搜索表单
const searchForm = reactive({
  costType: undefined,
  dateRange: [],
})

// 表格数据
const loading = ref(false)
const tableData = ref([])
const selectedRowKeys = ref<string[]>([])

// 分页配置
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
  showSizeChanger: true,
  showQuickJumper: true,
})

// 表格列定义
const columns = [
  {
    title: '成本类型',
    dataIndex: 'costType',
    key: 'costType',
  },
  {
    title: '金额',
    dataIndex: 'amount',
    key: 'amount',
  },
  {
    title: '日期',
    dataIndex: 'date',
    key: 'date',
  },
  {
    title: '备注',
    dataIndex: 'remark',
    key: 'remark',
  },
  {
    title: '操作',
    key: 'action',
    width: 150,
  },
]

// 搜索
const handleSearch = () => {
  // TODO: 实现搜索逻辑
}

// 导出
const handleExport = () => {
  // TODO: 实现导出逻辑
}

// 新增
const handleAdd = () => {
  // TODO: 实现新增逻辑
}

// 修改
const handleBatchEdit = () => {
  // TODO: 实现批量修改逻辑
}

// 删除
const handleBatchDelete = () => {
  // TODO: 实现批量删除逻辑
}

// 重置
const handleReset = () => {
  searchForm.costType = undefined
  searchForm.dateRange = []
  handleSearch()
}

// 表格选择变化
const onSelectChange = (keys: string[]) => {
  selectedRowKeys.value = keys
}

// 表格变化
const handleTableChange = (pag: any) => {
  pagination.current = pag.current
  pagination.pageSize = pag.pageSize
  handleSearch()
}

// 编辑单条记录
const handleEdit = (record: any) => {
  // TODO: 实现编辑逻辑
}

// 删除单条记录
const handleDelete = (record: any) => {
  // TODO: 实现删除逻辑
}
</script>

<style lang="less" scoped>
.cost-detail-container {
  padding: 24px;
  min-height: 100vh;

  .cost-detail-card {
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);

    .search-form {
      margin-bottom: 24px;
    }
  }
}
</style> 