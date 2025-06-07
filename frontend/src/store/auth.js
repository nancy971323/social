import { defineStore } from 'pinia'
import axios from 'axios'
import router from '../router'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: JSON.parse(localStorage.getItem('user')) || null,
    isAuthenticated: !!localStorage.getItem('user')
  }),
  
  actions: {
    async register(phoneNumber, username, password, email, biography = '', coverImage = null) {
      try {
        const response = await axios.post('/api/user/register', {
          phoneNumber,
          userName: username,
          password,
          email,
          biography,
          coverImage
        })
        
        const apiResponse = response.data
        if (apiResponse.success) {
          const userData = apiResponse.data
          // 確保保存完整的用戶資料，包括 token
          const userToStore = {
            ...userData,
            token: userData.token // 確保 token 被保存
          }
          this.user = userToStore
          this.isAuthenticated = true
          localStorage.setItem('user', JSON.stringify(userToStore))
          
          return { success: true, data: userToStore }
        } else {
          return { success: false, error: apiResponse.message }
        }
      } catch (error) {
        return { success: false, error: error.response?.data?.message || 'Registration failed' }
      }
    },
    
    async login(phoneNumber, password) {
      try {
        const response = await axios.post('/api/user/login', {
          phoneNumber,
          password
        })
        
        const apiResponse = response.data
        if (apiResponse.success) {
          const userData = apiResponse.data
          // 確保保存完整的用戶資料，包括 token
          const userToStore = {
            ...userData,
            token: userData.token // 確保 token 被保存
          }
          this.user = userToStore
          this.isAuthenticated = true
          localStorage.setItem('user', JSON.stringify(userToStore))
          
          return { success: true, data: userToStore }
        } else {
          return { success: false, error: apiResponse.message }
        }
      } catch (error) {
        return { success: false, error: error.response?.data?.message || 'Login failed' }
      }
    },
    
    async updateUser(userData) {
      try {
        const token = this.user?.token
        if (!token) {
          throw new Error('No authentication token')
        }

        const response = await axios.put('/api/user/update', {
          biography: userData.biography
        }, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        })
        
        const apiResponse = response.data
        if (apiResponse.success) {
          // 更新本地用戶資料
          const updatedUser = {
            ...this.user,
            biography: userData.biography
          }
          this.user = updatedUser
          localStorage.setItem('user', JSON.stringify(updatedUser))
          
          return { success: true }
        } else {
          return { success: false, error: apiResponse.message }
        }
      } catch (error) {
        return { success: false, error: error.response?.data?.message || 'Update failed' }
      }
    },

    logout() {
      this.user = null
      this.isAuthenticated = false
      localStorage.removeItem('user')
      router.push('/')
    }
  }
}) 