<template>
  <div class="finance-management-container">
    <div class="system-grid">
      <div 
        v-for="system in systems" 
        :key="system.id" 
        class="system-card"
        @click="navigateToSystem(system)"
        @mouseenter="system.isHovered = true"
        @mouseleave="system.isHovered = false"
      >
        <div class="system-icon-wrapper" :class="{ 'hovered': system.isHovered }">
          <component :is="system.icon" class="system-icon" />
        </div>
        <div class="system-title">{{ system.title }}</div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
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
  StockOutlined
} from '@ant-design/icons-vue'

const router = useRouter()

// 业务子系统数据类型
interface SystemItem {
  id: number
  title: string
  description: string
  icon: any
  url: string
  isHovered: boolean
}

// 业务子系统数据
const systems = reactive<SystemItem[]>([
  {
    id: 1,
    title: '财务报表',
    description: '查看和管理企业财务报表',
    icon: AccountBookOutlined,
    url: '/business/finance-reports',
    isHovered: false
  },
  {
    id: 2,
    title: '资金管理',
    description: '企业资金流向管理',
    icon: BankOutlined,
    url: '/business/fund-management',
    isHovered: false
  },
  {
    id: 3,
    title: '收支明细',
    description: '日常收支明细记录',
    icon: TransactionOutlined,
    url: '/business/transactions',
    isHovered: false
  },
  {
    id: 4,
    title: '发票管理',
    description: '发票开具与管理',
    icon: CreditCardOutlined,
    url: '/business/invoice',
    isHovered: false
  },
  {
    id: 5,
    title: '工资管理',
    description: '员工工资发放与管理',
    icon: DollarOutlined,
    url: '/business/salary',
    isHovered: false
  },
  {
    id: 6,
    title: '电子钱包',
    description: '企业电子钱包管理',
    icon: WalletOutlined,
    url: '/business/e-wallet',
    isHovered: false
  },
  {
    id: 7,
    title: '财务审计',
    description: '企业财务审计系统',
    icon: AuditOutlined,
    url: '/business/audit',
    isHovered: false
  },
  {
    id: 8,
    title: '柱状图表',
    description: '财务数据柱状图分析',
    icon: BarChartOutlined,
    url: '/business/bar-charts',
    isHovered: false
  },
  {
    id: 9,
    title: '折线图表',
    description: '财务数据趋势分析',
    icon: LineChartOutlined,
    url: '/business/line-charts',
    isHovered: false
  },
  {
    id: 10,
    title: '饼图分析',
    description: '财务数据占比分析',
    icon: PieChartOutlined,
    url: '/business/pie-charts',
    isHovered: false
  },
  {
    id: 11,
    title: '资金流向',
    description: '企业资金流向图',
    icon: FundOutlined,
    url: '/business/fund-flow',
    isHovered: false
  },
  {
    id: 12,
    title: '股票投资',
    description: '企业股票投资管理',
    icon: StockOutlined,
    url: '/business/stock',
    isHovered: false
  }
])

// 跳转到对应子系统
const navigateToSystem = (system: SystemItem) => {
  console.log(`跳转到: ${system.url}`)
  router.push(system.url)
}
</script>

<style scoped>
.finance-management-container {
  padding: 16px;
  max-width: 1200px;
  margin: 0 auto;
}

.system-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 12px;
}

.system-card {
  background-color: #fff;
  border-radius: 6px;
  box-shadow: 0 1px 6px rgba(0, 0, 0, 0.05);
  padding: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  border: 1px solid #f0f0f0;
}

.system-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(39, 194, 173, 0.15);
  border-color: rgba(39, 194, 173, 0.3);
}

.system-icon-wrapper {
  width: 46px;
  height: 46px;
  background-color: #f5f5f5;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 6px;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.system-icon-wrapper::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(39, 194, 173, 0.1), rgba(39, 194, 173, 0.3));
  opacity: 0;
  transition: opacity 0.3s ease;
}

.system-icon-wrapper.hovered {
  background-color: #e6f7f5;
  transform: scale(1.1) rotate(5deg);
}

.system-icon-wrapper.hovered::before {
  opacity: 1;
}

.system-icon {
  font-size: 28px;
  color: #27c2ad;
  transition: all 0.3s ease;
}

.system-icon-wrapper.hovered .system-icon {
  transform: scale(1.2);
  color: #1fb19d;
}

.system-title {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  line-height: 1.4;
  margin-top: 4px;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .system-grid {
    grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
    gap: 12px;
  }
  
  .system-card {
    padding: 10px;
  }
  
  .system-icon-wrapper {
    width: 40px;
    height: 40px;
  }
  
  .system-icon {
    font-size: 22px;
  }
  
  .system-title {
    font-size: 12px;
  }
}
</style> 