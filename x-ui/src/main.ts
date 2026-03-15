import { createApp } from 'vue'
import Antd from 'ant-design-vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import { hasPermDirective } from './directives/permission'
import 'ant-design-vue/dist/reset.css'
import './styles/global.less'
import './style.css'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(Antd, {
  theme: {
    token: {
      colorPrimary: '#27c2ad',
      colorPrimaryHover: '#2ed3bd',
      colorPrimaryActive: '#1fb19d',
    }
  }
})
app.use(router)

app.directive('has-perm', hasPermDirective)

app.mount('#app')
