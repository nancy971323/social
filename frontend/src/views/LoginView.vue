<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../store/auth'

const router = useRouter()
const authStore = useAuthStore()

const phoneNumber = ref('')
const password = ref('')
const errorMessage = ref('')
const isSubmitting = ref(false)

const login = async () => {
  // Reset error message
  errorMessage.value = ''
  
  // Validate form
  if (!phoneNumber.value || !password.value) {
    errorMessage.value = 'Please enter both phone number and password'
    return
  }
  
  try {
    isSubmitting.value = true
    const result = await authStore.login(phoneNumber.value, password.value)
    
    if (result.success) {
      router.push('/')
    } else {
      errorMessage.value = result.error
    }
  } catch (error) {
    errorMessage.value = error.message || 'Login failed'
  } finally {
    isSubmitting.value = false
  }
}

const goToRegister = () => {
  router.push('/register')
}
</script>

<template>
  <div class="login-container">
    <div class="auth-form">
      <router-link to="/" class="logo">Social</router-link>
      <h1>Sign in to your account</h1>
      
      <div v-if="errorMessage" class="error-message">
        {{ errorMessage }}
      </div>
      
      <form @submit.prevent="login">
        <div class="form-group">
          <label for="phoneNumber">Phone Number</label>
          <input 
            id="phoneNumber" 
            type="text" 
            v-model="phoneNumber" 
            placeholder="Enter your phone number"
            required
          />
        </div>
        
        <div class="form-group">
          <label for="password">Password</label>
          <input 
            id="password" 
            type="password" 
            v-model="password" 
            placeholder="Enter your password"
            required
          />
        </div>
        
        <button type="submit" :disabled="isSubmitting">
          {{ isSubmitting ? 'Logging in...' : 'Login' }}
        </button>
      </form>
      
      <div class="auth-alternative">
        Don't have an account? <a href="#" @click.prevent="goToRegister">Register</a>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  position: relative;
  overflow: hidden;
}

.login-container::before {
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

.auth-form {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 30px;
  padding: 3rem;
  width: 100%;
  max-width: 450px;
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.15);
  border: 1px solid rgba(255, 255, 255, 0.2);
  position: relative;
  z-index: 1;
  text-align: center;
  animation: fadeInUp 0.8s ease-out;
}

.logo {
  font-size: 3rem;
  font-weight: 800;
  margin-bottom: 1rem;
  background: linear-gradient(135deg, #1da1f2, #667eea);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  text-decoration: none;
  letter-spacing: -1px;
  display: block;
}

h1 {
  margin-bottom: 2rem;
  color: #333;
  font-size: 1.5rem;
  font-weight: 600;
}

.form-group {
  margin-bottom: 1.5rem;
  text-align: left;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: #333;
  font-weight: 600;
  font-size: 0.9rem;
}

.form-group input {
  width: 100%;
  padding: 1rem;
  border: 2px solid #e1e8ed;
  border-radius: 15px;
  font-size: 1rem;
  transition: all 0.3s ease;
  background: white;
}

.form-group input:focus {
  outline: none;
  border-color: #1da1f2;
  box-shadow: 0 0 0 3px rgba(29, 161, 242, 0.1);
}

button {
  width: 100%;
  background: linear-gradient(135deg, #1da1f2, #667eea);
  color: white;
  border: none;
  padding: 1.2rem;
  border-radius: 60px;
  font-size: 1.1rem;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.4s ease;
  text-transform: uppercase;
  letter-spacing: 1px;
  margin-top: 1rem;
}

button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 15px 35px rgba(29, 161, 242, 0.4);
  background: linear-gradient(135deg, #667eea, #1da1f2);
}

button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
}

.auth-alternative {
  margin-top: 2rem;
  font-size: 0.9rem;
  color: #666;
}

.auth-alternative a {
  color: #1da1f2;
  text-decoration: none;
  font-weight: 600;
}

.auth-alternative a:hover {
  text-decoration: underline;
}

.error-message {
  background: rgba(231, 76, 60, 0.1);
  color: #e74c3c;
  padding: 1rem;
  border-radius: 15px;
  margin-bottom: 1.5rem;
  border: 1px solid rgba(231, 76, 60, 0.2);
  font-size: 0.9rem;
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

/* 響應式設計 */
@media (max-width: 768px) {
  .login-container {
    padding: 1rem;
  }
  
  .auth-form {
    padding: 2.5rem 2rem;
    border-radius: 25px;
  }
  
  .logo {
    font-size: 2.5rem;
  }
  
  h1 {
    font-size: 1.3rem;
  }
}
</style> 