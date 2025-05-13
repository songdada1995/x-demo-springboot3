import { createApp } from 'vue'
import Antd from 'ant-design-vue'
import App from './App.vue'
import router from './router'
import 'ant-design-vue/dist/reset.css'
import './style.css'

const app = createApp(App)

app.use(Antd, {
  theme: {
    token: {
      colorPrimary: '#27c2ad'
    }
  }
})
app.use(router)

app.mount('#app')
