<template>
  <div class="login-container">
    <!-- 添加浮动粒子 -->
    <div class="particles">
      <div v-for="i in 15" :key="i" class="particle"></div>
    </div>
    
    <!-- 左侧品牌区域 -->
    <div class="brand-container">
      <!-- 添加几何装饰元素 -->
      <div class="geometric-shapes">
        <div class="shape circle-1"></div>
        <div class="shape circle-2"></div>
        <div class="shape square"></div>
        <div class="shape triangle"></div>
        <div class="shape rectangle"></div>
      </div>
      
      <!-- 添加波浪装饰 -->
      <div class="wave-decoration top-wave"></div>
      <div class="wave-decoration bottom-wave"></div>
      
      <div class="brand-content">
        <div class="brand-logo">
          <svg viewBox="0 0 100 100" width="80" height="80">
            <circle cx="50" cy="50" r="40" fill="white" />
            <path d="M30,35 L70,35 L70,65 L30,65 Z" fill="#27c2ad" />
            <path d="M40,45 L60,45 L60,55 L40,55 Z" fill="white" />
          </svg>
        </div>
        <h1 class="brand-title">X-UI</h1>
        <p class="brand-slogan">高效、简洁、现代的管理系统</p>
        <div class="brand-features">
          <div class="feature-item">
            <check-circle-outlined />
            <span>简洁直观的操作界面</span>
          </div>
          <div class="feature-item">
            <check-circle-outlined />
            <span>强大的数据分析能力</span>
          </div>
          <div class="feature-item">
            <check-circle-outlined />
            <span>安全可靠的系统架构</span>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 右侧登录表单 -->
    <div class="login-form-container">
      <div class="login-logo">
        <h1>欢迎登录</h1>
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
import { UserOutlined, LockOutlined, CheckCircleOutlined } from '@ant-design/icons-vue'
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
  justify-content: flex-start;
  align-items: center;
  height: 100vh;
  width: 100vw;
  background: linear-gradient(135deg, #f5f5f5 0%, #ffffff 100%);
  position: relative;
  overflow: hidden;
}

/* 添加几何图案背景 */
.login-container {
  background-image: 
    linear-gradient(135deg, #f5f5f5 0%, #ffffff 100%),
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

/* 左侧品牌区域样式 */
.brand-container {
  width: 65%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 0 60px;
  position: relative;
  z-index: 2;
  background: linear-gradient(135deg, #27c2ad 0%, #1a8f80 100%);
  background-image: 
    linear-gradient(135deg, #27c2ad 0%, #1a8f80 100%),
    linear-gradient(rgba(255, 255, 255, 0.05) 1px, transparent 1px),
    linear-gradient(90deg, rgba(255, 255, 255, 0.05) 1px, transparent 1px);
  background-size: 100% 100%, 20px 20px, 20px 20px;
  background-position: 0 0, 0 0, 0 0;
  color: white;
  overflow: hidden;
}

/* 几何装饰元素 */
.geometric-shapes {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  z-index: -1;
  overflow: hidden;
}

.shape {
  position: absolute;
  opacity: 0.15;
  background: white;
  animation: pulse 8s infinite alternate;
}

@keyframes pulse {
  0% {
    transform: scale(1) rotate(0deg);
    opacity: 0.1;
  }
  50% {
    transform: scale(1.05) rotate(2deg);
    opacity: 0.15;
  }
  100% {
    transform: scale(1) rotate(0deg);
    opacity: 0.1;
  }
}

.circle-1 {
  width: 300px;
  height: 300px;
  border-radius: 50%;
  top: -100px;
  left: -100px;
  animation: float 20s infinite ease-in-out;
}

.circle-2 {
  width: 200px;
  height: 200px;
  border-radius: 50%;
  bottom: -50px;
  right: -50px;
  animation: float 25s infinite ease-in-out reverse;
}

.square {
  width: 100px;
  height: 100px;
  transform: rotate(45deg);
  top: 20%;
  right: 10%;
  animation: rotate 30s linear infinite;
}

@keyframes rotate {
  from {
    transform: rotate(45deg);
  }
  to {
    transform: rotate(405deg);
  }
}

.triangle {
  width: 0;
  height: 0;
  border-left: 80px solid transparent;
  border-right: 80px solid transparent;
  border-bottom: 140px solid white;
  background: transparent;
  opacity: 0.1;
  bottom: 10%;
  left: 5%;
  animation: float 18s infinite ease-in-out 2s;
}

.rectangle {
  width: 120px;
  height: 60px;
  top: 40%;
  left: -20px;
  transform: rotate(-30deg);
  animation: slide 15s infinite alternate;
}

@keyframes slide {
  0% {
    transform: translateX(-20px) rotate(-30deg);
  }
  100% {
    transform: translateX(20px) rotate(-25deg);
  }
}

/* 添加更多装饰元素 */
.brand-container::before {
  content: "";
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  background-image: 
    radial-gradient(circle at 70% 20%, rgba(255, 255, 255, 0.1) 0%, transparent 25%),
    radial-gradient(circle at 30% 80%, rgba(255, 255, 255, 0.08) 0%, transparent 20%);
  z-index: 0;
}

/* 波浪装饰 */
.wave-decoration {
  position: absolute;
  width: 100%;
  height: 100px;
  left: 0;
  background-repeat: no-repeat;
  background-size: 100% 100%;
  opacity: 0.2;
  animation: wave 10s infinite ease-in-out alternate;
}

@keyframes wave {
  0% {
    transform: translateY(0);
  }
  100% {
    transform: translateY(10px);
  }
}

.top-wave {
  top: 0;
  background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320"><path fill="%23ffffff" d="M0,192L48,197.3C96,203,192,213,288,229.3C384,245,480,267,576,250.7C672,235,768,181,864,181.3C960,181,1056,235,1152,234.7C1248,235,1344,181,1392,154.7L1440,128L1440,0L1392,0C1344,0,1248,0,1152,0C1056,0,960,0,864,0C768,0,672,0,576,0C480,0,384,0,288,0C192,0,96,0,48,0L0,0Z"></path></svg>');
}

.bottom-wave {
  bottom: 0;
  background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320"><path fill="%23ffffff" d="M0,128L48,144C96,160,192,192,288,197.3C384,203,480,181,576,154.7C672,128,768,96,864,90.7C960,85,1056,107,1152,128C1248,149,1344,171,1392,181.3L1440,192L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z"></path></svg>');
  animation-delay: 1s;
}

/* 品牌 Logo 动画 */
.brand-logo svg {
  animation: logoFloat 6s infinite ease-in-out;
}

@keyframes logoFloat {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

/* 品牌标题增强 */
.brand-title {
  font-size: 48px;
  font-weight: bold;
  margin-bottom: 20px;
  letter-spacing: 2px;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  background: linear-gradient(to right, #ffffff, #e0f5f2);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  position: relative;
  display: inline-block;
}

.brand-title::after {
  content: "";
  position: absolute;
  bottom: -5px;
  left: 0;
  width: 0;
  height: 3px;
  background: white;
  animation: titleLine 3s forwards 1s;
}

@keyframes titleLine {
  0% {
    width: 0;
  }
  100% {
    width: 100%;
  }
}

/* 特性项目动画 */
.feature-item {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  font-size: 18px;
  opacity: 0;
  transform: translateX(-20px);
  animation: featureAppear 0.5s forwards;
}

.feature-item:nth-child(1) {
  animation-delay: 1s;
}

.feature-item:nth-child(2) {
  animation-delay: 1.3s;
}

.feature-item:nth-child(3) {
  animation-delay: 1.6s;
}

@keyframes featureAppear {
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.feature-item .anticon {
  margin-right: 12px;
  font-size: 20px;
  animation: iconPulse 2s infinite alternate;
}

@keyframes iconPulse {
  from {
    transform: scale(1);
  }
  to {
    transform: scale(1.2);
  }
}

.brand-content {
  max-width: 500px;
  text-align: left;
  position: relative;
  z-index: 2;
}

.brand-logo {
  margin-bottom: 20px;
  display: flex;
  justify-content: flex-start;
}

.brand-slogan {
  font-size: 24px;
  margin-bottom: 40px;
  opacity: 0.9;
}

.brand-features {
  margin-top: 40px;
}

/* 右侧登录表单样式 */
.login-form-container {
  width: 400px;
  padding: 40px;
  margin-right: 0;
  background: white;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
  position: absolute;
  left: calc(65% + ((100% - 65%) / 2) - 200px);
  top: 30%;
  transform: translateY(-50%);
  z-index: 2;
  backdrop-filter: blur(5px);
  background-color: rgba(255, 255, 255, 0.95);
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.login-form-container:hover {
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
  transform: translateY(-52%);
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
  font-size: 28px;
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

.brand-content {
  animation: fadeIn 0.8s ease-out 0.3s forwards;
  opacity: 0;
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

/* 响应式调整 */
@media (max-width: 1400px) {
  .brand-container {
    width: 60%;
  }
  
  .login-form-container {
    left: calc(60% + ((100% - 60%) / 2) - 190px);
    width: 380px;
  }
}

@media (max-width: 1200px) {
  .brand-container {
    padding: 0 40px;
    width: 55%;
  }
  
  .login-form-container {
    left: calc(55% + ((100% - 55%) / 2) - 180px);
    width: 360px;
    padding: 35px;
  }
}

@media (max-width: 992px) {
  .login-container {
    flex-direction: column;
    justify-content: flex-start;
  }
  
  .brand-container {
    width: 100%;
    height: 55%;
    padding: 20px;
  }
  
  .brand-content {
    text-align: center;
    max-width: 600px;
  }
  
  .brand-logo {
    justify-content: center;
  }
  
  .login-form-container {
    position: relative;
    left: auto;
    right: auto;
    top: auto;
    transform: none;
    margin: -140px auto 0;
    width: 90%;
    max-width: 380px;
    padding: 35px;
  }
  
  .login-form-container:hover {
    transform: none;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
  }
  
  .feature-item {
    justify-content: center;
  }
  
  .geometric-shapes .shape {
    transform: scale(0.7);
  }
}

@media (max-width: 576px) {
  .login-form-container {
    width: 90%;
    padding: 30px 20px;
  }
  
  .brand-title {
    font-size: 36px;
  }
  
  .brand-slogan {
    font-size: 18px;
  }
  
  .feature-item {
    font-size: 16px;
  }
}
</style> 