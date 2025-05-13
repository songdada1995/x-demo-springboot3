<template>
  <div class="list-page">
    <div class="page-header">
      <h2>用户管理</h2>
      <a-button type="primary" @click="handleAdd">
        <template #icon><plus-outlined /></template>
        新增用户
      </a-button>
    </div>

    <a-card>
      <!-- 搜索表单 -->
      <a-form layout="inline" :model="searchForm" class="search-form">
        <a-form-item label="用户名">
          <a-input v-model:value="searchForm.username" placeholder="请输入用户名" />
        </a-form-item>
        <a-form-item label="状态">
          <a-select v-model:value="searchForm.status" style="width: 120px">
            <a-select-option value="">全部</a-select-option>
            <a-select-option value="1">启用</a-select-option>
            <a-select-option value="0">禁用</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="handleSearch">
            <template #icon><search-outlined /></template>
            搜索
          </a-button>
          <a-button style="margin-left: 8px" @click="handleReset">
            <template #icon><redo-outlined /></template>
            重置
          </a-button>
        </a-form-item>
      </a-form>

      <!-- 数据表格 -->
      <a-table
        :columns="columns"
        :data-source="dataSource"
        :loading="loading"
        :pagination="pagination"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <a-tag :color="record.status === '1' ? 'green' : 'red'">
              {{ record.status === '1' ? '启用' : '禁用' }}
            </a-tag>
          </template>
          <template v-if="column.key === 'action'">
            <a-space>
              <a @click="handleEdit(record)">编辑</a>
              <a-divider type="vertical" />
              <a-popconfirm
                title="确定要删除这条记录吗？"
                @confirm="handleDelete(record)"
              >
                <a class="danger-link">删除</a>
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
  PlusOutlined,
  SearchOutlined,
  RedoOutlined,
} from '@ant-design/icons-vue'

// 搜索表单数据
const searchForm = reactive({
  username: '',
  status: '',
})

// 表格列定义
const columns = [
  {
    title: '用户名',
    dataIndex: 'username',
    key: 'username',
  },
  {
    title: '姓名',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: '邮箱',
    dataIndex: 'email',
    key: 'email',
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime',
  },
  {
    title: '操作',
    key: 'action',
    width: 150,
  },
]

// 表格数据
const dataSource = ref([
  {
    key: '1',
    username: 'admin',
    name: '管理员',
    email: 'admin@example.com',
    status: '1',
    createTime: '2024-01-01 12:00:00',
  },
  // 更多数据...
])

// 加载状态
const loading = ref(false)

// 分页配置
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 100,
  showSizeChanger: true,
  showQuickJumper: true,
})

// 处理搜索
const handleSearch = () => {
  // 实现搜索逻辑
  console.log('搜索条件：', searchForm)
}

// 处理重置
const handleReset = () => {
  searchForm.username = ''
  searchForm.status = ''
  handleSearch()
}

// 处理表格变化
const handleTableChange = (pag: any) => {
  pagination.current = pag.current
  pagination.pageSize = pag.pageSize
  // 重新加载数据
}

// 处理新增
const handleAdd = () => {
  // 实现新增逻辑
}

// 处理编辑
const handleEdit = (record: any) => {
  // 实现编辑逻辑
  console.log('编辑记录：', record)
}

// 处理删除
const handleDelete = (record: any) => {
  // 实现删除逻辑
  console.log('删除记录：', record)
}
</script>

<style scoped>
.list-page {
  padding: 24px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-header h2 {
  margin: 0;
  color: rgba(0, 0, 0, 0.85);
  font-weight: 500;
}

.search-form {
  margin-bottom: 24px;
}

.danger-link {
  color: #ff4d4f;
}

.danger-link:hover {
  color: #ff7875;
}
</style> 