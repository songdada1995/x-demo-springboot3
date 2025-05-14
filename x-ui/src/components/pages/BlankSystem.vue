<template>
  <div class="blank-system">
    <div class="system-content">
      <div class="system-icon">
        <component :is="icon" class="icon" />
      </div>
      <h2 class="system-title">{{ title }}</h2>
      <p class="system-description">该子系统正在开发中，敬请期待...</p>
      <a-button type="primary" @click="goBack">返回</a-button>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  BankOutlined,
  AccountBookOutlined,
  DollarOutlined,
  TransactionOutlined,
  CreditCardOutlined,
  WalletOutlined,
  AuditOutlined,
  BarChartOutlined,
  LineChartOutlined,
  PieChartOutlined,
  FundOutlined,
  StockOutlined,
  AppstoreOutlined
} from '@ant-design/icons-vue'

const router = useRouter()
const route = useRoute()

// 定义图标映射类型
interface IconMap {
  [key: string]: any;
}

// 定义标题映射类型
interface TitleMap {
  [key: string]: string;
}

// 根据路由路径获取对应的图标和标题
const icon = computed(() => {
  const path = route.path
  const iconMap: IconMap = {
    '/business/finance-reports': AccountBookOutlined,
    '/business/fund-management': BankOutlined,
    '/business/transactions': TransactionOutlined,
    '/business/invoice': CreditCardOutlined,
    '/business/salary': DollarOutlined,
    '/business/e-wallet': WalletOutlined,
    '/business/audit': AuditOutlined,
    '/business/bar-charts': BarChartOutlined,
    '/business/line-charts': LineChartOutlined,
    '/business/pie-charts': PieChartOutlined,
    '/business/fund-flow': FundOutlined,
    '/business/stock': StockOutlined
  }
  return iconMap[path] || AppstoreOutlined
})

const title = computed(() => {
  const path = route.path
  const titleMap: TitleMap = {
    '/business/finance-reports': '财务报表',
    '/business/fund-management': '资金管理',
    '/business/transactions': '收支明细',
    '/business/invoice': '发票管理',
    '/business/salary': '工资管理',
    '/business/e-wallet': '电子钱包',
    '/business/audit': '财务审计',
    '/business/bar-charts': '柱状图表',
    '/business/line-charts': '折线图表',
    '/business/pie-charts': '饼图分析',
    '/business/fund-flow': '资金流向',
    '/business/stock': '股票投资'
  }
  return titleMap[path] || '业务子系统'
})

// 返回上一页
const goBack = () => {
  router.push('/business/finance-management')
}
</script>

<style scoped>
.blank-system {
  display: flex;
  justify-content: center;
  align-items: center;
  height: calc(100vh - 180px);
  width: 100%;
}

.system-content {
  text-align: center;
  padding: 40px;
  max-width: 500px;
}

.system-icon {
  margin-bottom: 24px;
}

.icon {
  font-size: 80px;
  color: #27c2ad;
}

.system-title {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 16px;
  color: #333;
}

.system-description {
  font-size: 16px;
  color: #666;
  margin-bottom: 32px;
}
</style> 