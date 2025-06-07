import './assets/main.css'
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import axios from 'axios'

// Set base URL for axios
axios.defaults.baseURL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

// Add authorization header to requests if user is logged in
axios.interceptors.request.use(config => {
  const user = JSON.parse(localStorage.getItem('user'))
  if (user && user.token) {
    config.headers.Authorization = `Bearer ${user.token}`
  }
  return config
})

const app = createApp(App)
app.use(createPinia())
app.use(router)
app.mount('#app')
