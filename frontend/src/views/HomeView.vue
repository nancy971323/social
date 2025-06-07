<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../store/auth'
import PostsView from './PostsView.vue'

const authStore = useAuthStore()
const router = useRouter()

// Computed properties
const isAuthenticated = computed(() => authStore.isAuthenticated)

// Methods
const goToLogin = () => {
  router.push('/login')
}

const goToRegister = () => {
  router.push('/register')
}
</script>

<template>
  <!-- 如果已登入，顯示動態消息頁面 -->
  <PostsView v-if="isAuthenticated" />
  
  <!-- 如果未登入，顯示歡迎/登入頁面 -->
  <div v-else class="welcome-container">
    <div class="welcome-content">
      <div class="hero-section">
        <h1 class="logo">Social</h1>
        
        <div class="auth-buttons">
          <button @click="goToRegister" class="create-account-btn">
            Create account
          </button>
          
          <button @click="goToLogin" class="sign-in-btn">
            Sign in
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.welcome-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  position: relative;
  overflow: hidden;
}

.welcome-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(circle at 30% 20%, rgba(255, 255, 255, 0.1) 0%, transparent 50%),
              radial-gradient(circle at 70% 80%, rgba(255, 255, 255, 0.1) 0%, transparent 50%);
  pointer-events: none;
}

.welcome-content {
  position: relative;
  z-index: 1;
  text-align: center;
  max-width: 500px;
  width: 100%;
}

.hero-section {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 30px;
  padding: 4rem 3rem;
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.15);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.logo {
  font-size: 5rem;
  font-weight: 800;
  margin-bottom: 3rem;
  background: linear-gradient(135deg, #1da1f2, #667eea);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  text-shadow: none;
  letter-spacing: -2px;
}

.auth-buttons {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  max-width: 300px;
  margin: 0 auto;
}

.create-account-btn {
  background: linear-gradient(135deg, #1da1f2, #667eea);
  color: white;
  border: none;
  padding: 1.2rem 2.5rem;
  border-radius: 60px;
  font-size: 1.1rem;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.4s ease;
  box-shadow: 0 8px 25px rgba(29, 161, 242, 0.3);
  text-transform: uppercase;
  letter-spacing: 1px;
}

.create-account-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 15px 35px rgba(29, 161, 242, 0.4);
  background: linear-gradient(135deg, #667eea, #1da1f2);
}

.sign-in-btn {
  background: transparent;
  color: #1da1f2;
  border: 2px solid #1da1f2;
  padding: 1.2rem 2.5rem;
  border-radius: 60px;
  font-size: 1.1rem;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.4s ease;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.sign-in-btn:hover {
  background: #1da1f2;
  color: white;
  transform: translateY(-3px);
  box-shadow: 0 15px 35px rgba(29, 161, 242, 0.3);
}

/* 響應式設計 */
@media (max-width: 768px) {
  .welcome-container {
    padding: 1rem;
  }
  
  .hero-section {
    padding: 3rem 2rem;
    border-radius: 25px;
  }
  
  .logo {
    font-size: 4rem;
    margin-bottom: 2.5rem;
  }
  
  .create-account-btn,
  .sign-in-btn {
    padding: 1rem 2rem;
    font-size: 1rem;
  }
}

@media (max-width: 480px) {
  .hero-section {
    padding: 2.5rem 1.5rem;
  }
  
  .logo {
    font-size: 3.5rem;
  }
  
  .auth-buttons {
    gap: 1.2rem;
  }
}

/* 動畫效果 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.hero-section {
  animation: fadeInUp 0.8s ease-out;
}

.logo {
  animation: fadeInUp 0.8s ease-out 0.2s both;
}

.auth-buttons {
  animation: fadeInUp 0.8s ease-out 0.4s both;
}
</style> 