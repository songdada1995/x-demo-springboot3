<template>
  <div class="login-container">
    <!-- 添加浮动粒子 -->
    <div class="particles">
      <div v-for="i in 15" :key="i" class="particle"></div>
    </div>
    <div class="login-form-container">
      <div class="login-logo">
        <h1>X-UI</h1>
      </div>
      <a-form
        :model="loginForm"
        name="login"
        class="login-form"
        @finish="handleSubmit"
      >
        <a-form-item
          name="username"
          :rules="[{ required: true, message: '请输入用户名!' }]"
        >
          <a-input
            v-model:value="loginForm.username"
            size="large"
            placeholder="用户名"
            class="custom-input"
          >
            <template #prefix>
              <user-outlined class="site-form-item-icon" />
            </template>
          </a-input>
        </a-form-item>
        <a-form-item
          name="password"
          :rules="[{ required: true, message: '请输入密码!' }]"
        >
          <a-input-password
            v-model:value="loginForm.password"
            size="large"
            placeholder="密码"
            class="custom-input"
          >
            <template #prefix>
              <lock-outlined class="site-form-item-icon" />
            </template>
          </a-input-password>
        </a-form-item>
        <a-form-item>
          <a-checkbox v-model:checked="loginForm.remember">
            记住我
          </a-checkbox>
          <a class="login-form-forgot" href="">
            忘记密码
          </a>
        </a-form-item>

        <a-form-item>
          <a-button
            type="primary"
            html-type="submit"
            class="login-form-button"
            size="large"
            :loading="loading"
          >
            登录
          </a-button>
        </a-form-item>
        
        <!-- 添加登录提示 -->
        <div class="login-tips">
          <p>默认用户名: admin</p>
          <p>默认密码: admin123</p>
        </div>
      </a-form>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref } from 'vue'
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../../stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: '',
  remember: true,
})

const handleSubmit = async () => {
  loading.value = true
  try {
    // 假数据登录验证
    if (loginForm.username === 'admin' && loginForm.password === 'admin123') {
      // 登录成功
      await authStore.login({
        username: loginForm.username,
        password: loginForm.password,
      })
      message.success('登录成功')
      router.push('/dashboard')
    } else {
      message.error('用户名或密码错误')
    }
  } catch (error) {
    message.error('登录失败，请重试')
    console.error('登录失败:', error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  width: 100vw;
  background: linear-gradient(135deg, #001529 0%, #003a70 100%);
  position: relative;
  overflow: hidden;
}

/* 添加几何图案背景 */
.login-container {
  background-image: 
    linear-gradient(135deg, #001529 0%, #003a70 100%),
    repeating-linear-gradient(45deg, rgba(39, 194, 173, 0.05) 0px, rgba(39, 194, 173, 0.05) 2px, transparent 2px, transparent 10px),
    repeating-linear-gradient(135deg, rgba(39, 194, 173, 0.05) 0px, rgba(39, 194, 173, 0.05) 2px, transparent 2px, transparent 10px);
  background-blend-mode: normal, normal, screen;
}

/* 添加背景图案 */
.login-container::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: 
    radial-gradient(circle at 10% 20%, rgba(39, 194, 173, 0.1) 0%, transparent 20%),
    radial-gradient(circle at 90% 30%, rgba(39, 194, 173, 0.15) 0%, transparent 25%),
    radial-gradient(circle at 30% 70%, rgba(39, 194, 173, 0.1) 0%, transparent 15%),
    radial-gradient(circle at 80% 80%, rgba(39, 194, 173, 0.15) 0%, transparent 20%);
  background-size: 100% 100%;
  z-index: 1;
}

/* 添加动态波浪效果 */
.login-container::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 200px;
  background: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320"><path fill="%2327c2ad" fill-opacity="0.15" d="M0,192L48,186.7C96,181,192,171,288,181.3C384,192,480,224,576,229.3C672,235,768,213,864,186.7C960,160,1056,128,1152,138.7C1248,149,1344,203,1392,229.3L1440,256L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z"></path></svg>') no-repeat bottom;
  background-size: 100% 100%;
  z-index: 1;
}

/* 浮动粒子样式 */
.particles {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
  pointer-events: none;
}

.particle {
  position: absolute;
  border-radius: 50%;
  background: rgba(39, 194, 173, 0.2);
  box-shadow: 0 0 10px 2px rgba(39, 194, 173, 0.1);
}

.particle:nth-child(1) { width: 15px; height: 15px; top: 10%; left: 20%; animation: float 20s infinite ease-in-out; }
.particle:nth-child(2) { width: 12px; height: 12px; top: 30%; left: 85%; animation: float 15s infinite ease-in-out 1s; }
.particle:nth-child(3) { width: 8px; height: 8px; top: 60%; left: 10%; animation: float 18s infinite ease-in-out 2s; }
.particle:nth-child(4) { width: 20px; height: 20px; top: 80%; left: 70%; animation: float 22s infinite ease-in-out 3s; }
.particle:nth-child(5) { width: 10px; height: 10px; top: 15%; left: 60%; animation: float 19s infinite ease-in-out 4s; }
.particle:nth-child(6) { width: 18px; height: 18px; top: 40%; left: 30%; animation: float 21s infinite ease-in-out 5s; }
.particle:nth-child(7) { width: 7px; height: 7px; top: 75%; left: 40%; animation: float 16s infinite ease-in-out 6s; }
.particle:nth-child(8) { width: 14px; height: 14px; top: 25%; left: 75%; animation: float 23s infinite ease-in-out 7s; }
.particle:nth-child(9) { width: 9px; height: 9px; top: 50%; left: 90%; animation: float 17s infinite ease-in-out 8s; }
.particle:nth-child(10) { width: 16px; height: 16px; top: 85%; left: 15%; animation: float 24s infinite ease-in-out 9s; }
.particle:nth-child(11) { width: 11px; height: 11px; top: 5%; left: 45%; animation: float 20s infinite ease-in-out 10s; }
.particle:nth-child(12) { width: 13px; height: 13px; top: 35%; left: 5%; animation: float 19s infinite ease-in-out 11s; }
.particle:nth-child(13) { width: 6px; height: 6px; top: 65%; left: 55%; animation: float 22s infinite ease-in-out 12s; }
.particle:nth-child(14) { width: 17px; height: 17px; top: 20%; left: 95%; animation: float 18s infinite ease-in-out 13s; }
.particle:nth-child(15) { width: 10px; height: 10px; top: 90%; left: 25%; animation: float 21s infinite ease-in-out 14s; }

@keyframes float {
  0%, 100% {
    transform: translateY(0) translateX(0);
  }
  25% {
    transform: translateY(-30px) translateX(15px);
  }
  50% {
    transform: translateY(-15px) translateX(30px);
  }
  75% {
    transform: translateY(-40px) translateX(-15px);
  }
}

.login-form-container {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  position: relative;
  z-index: 2;
  backdrop-filter: blur(5px);
  background-color: rgba(255, 255, 255, 0.95);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.login-logo {
  text-align: center;
  margin-bottom: 40px;
  position: relative;
}

.login-logo::after {
  content: "";
  position: absolute;
  bottom: -15px;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 3px;
  background: linear-gradient(90deg, transparent, #27c2ad, transparent);
}

.login-logo h1 {
  color: #27c2ad;
  font-size: 36px;
  font-weight: bold;
  letter-spacing: 2px;
  margin: 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.login-form {
  max-width: 100%;
}

.login-form-forgot {
  float: right;
  color: #27c2ad;
}

.login-form-button {
  width: 100%;
  background-color: #27c2ad;
  border-color: #27c2ad;
  height: 42px;
  font-size: 16px;
  box-shadow: 0 2px 6px rgba(39, 194, 173, 0.4);
  transition: all 0.3s ease;
}

.login-form-button:hover {
  background-color: #2ed3bd;
  border-color: #2ed3bd;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(39, 194, 173, 0.6);
}

.ant-form-item {
  margin-bottom: 24px;
}

.site-form-item-icon {
  color: rgba(0, 0, 0, 0.25);
}

/* 登录提示 */
.login-tips {
  text-align: center;
  margin-top: 16px;
  padding: 8px;
  background-color: rgba(39, 194, 173, 0.05);
  border-radius: 4px;
  border-left: 3px solid #27c2ad;
}

.login-tips p {
  margin: 4px 0;
  font-size: 13px;
  color: #666;
}

/* 完全重写输入框样式 */
:deep(.custom-input.ant-input),
:deep(.custom-input.ant-input-affix-wrapper) {
  height: 42px;
  border-radius: 4px;
  border: 1px solid #d9d9d9;
  transition: all 0.3s;
  box-shadow: none;
  background-color: #fff;
}

:deep(.custom-input.ant-input:hover),
:deep(.custom-input.ant-input-affix-wrapper:hover) {
  border-color: #27c2ad;
}

:deep(.custom-input.ant-input:focus),
:deep(.custom-input.ant-input-affix-wrapper-focused) {
  border-color: #27c2ad;
  box-shadow: 0 0 0 2px rgba(39, 194, 173, 0.2);
  outline: 0;
}

:deep(.custom-input.ant-input-affix-wrapper) {
  padding: 0 11px;
  display: flex;
  align-items: center;
}

:deep(.custom-input.ant-input-affix-wrapper .ant-input) {
  background-color: transparent;
  border: none;
  box-shadow: none;
  padding: 0;
  height: 24px;
}

:deep(.custom-input.ant-input-affix-wrapper .ant-input-prefix) {
  margin-right: 12px;
}

:deep(.custom-input.ant-input-affix-wrapper .ant-input-suffix) {
  margin-left: auto;
}

:deep(.custom-input .ant-input-password-icon) {
  color: rgba(0, 0, 0, 0.45);
  cursor: pointer;
  transition: all 0.3s;
}

:deep(.custom-input .ant-input-password-icon:hover) {
  color: #27c2ad;
}

/* 处理自动填充样式 */
:deep(input:-webkit-autofill),
:deep(input:-webkit-autofill:hover),
:deep(input:-webkit-autofill:focus) {
  -webkit-box-shadow: 0 0 0 1000px white inset !important;
  -webkit-text-fill-color: rgba(0, 0, 0, 0.85) !important;
  transition: background-color 5000s ease-in-out 0s;
}

/* 恢复复选框样式 */
:deep(.ant-checkbox-checked .ant-checkbox-inner) {
  background-color: #27c2ad;
  border-color: #27c2ad;
}

:deep(.ant-form-item-explain-error) {
  margin-top: 4px;
}

/* 添加动画效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.login-form-container {
  animation: fadeIn 0.8s ease-out forwards;
}

/* 确保输入框内容正确对齐 */
:deep(.ant-input),
:deep(.ant-input-affix-wrapper .ant-input) {
  line-height: 1.5;
  vertical-align: middle;
}

/* 修复输入框内容的字体和颜色 */
:deep(.ant-input),
:deep(.ant-input-affix-wrapper .ant-input) {
  font-size: 14px;
  color: rgba(0, 0, 0, 0.85);
  caret-color: #27c2ad;
}

/* 移除输入框内阴影 */
:deep(.ant-input),
:deep(.ant-input-affix-wrapper),
:deep(.ant-input-affix-wrapper .ant-input) {
  background-image: none !important;
  background-color: white !important;
}

:deep(.ant-form-item-has-feedback .ant-input-affix-wrapper .ant-input-suffix) {
  padding-right: 0;
}
</style> 