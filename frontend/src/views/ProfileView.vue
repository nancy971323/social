<script setup>
import { ref, computed, onMounted } from 'vue'
import { useAuthStore } from '../store/auth'
import { usePostsStore } from '../store/posts'

const authStore = useAuthStore()
const postsStore = usePostsStore()

// User data
const user = computed(() => authStore.user)
const userPosts = computed(() => {
  return postsStore.posts.filter(post => post.userId === user.value?.userId)
})

// Edit profile
const isEditingProfile = ref(false)
const username = ref('')
const email = ref('')
const biography = ref('')
const profileErrorMessage = ref('')
const isSubmittingProfile = ref(false)

const startEditingProfile = () => {
  username.value = user.value?.userName || ''
  email.value = user.value?.email || ''
  biography.value = user.value?.biography || ''
  isEditingProfile.value = true
}

const cancelEditingProfile = () => {
  isEditingProfile.value = false
  profileErrorMessage.value = ''
}



const updateProfile = async () => {
  // 只需要驗證 biography 的長度（如果有要求的話）
  if (biography.value && biography.value.length > 500) {
    profileErrorMessage.value = 'Biography is too long (max 500 characters)'
    return
  }
  
  try {
    isSubmittingProfile.value = true
    profileErrorMessage.value = ''
    
    const result = await authStore.updateUser({
      biography: biography.value
    })
    
    if (result.success) {
      isEditingProfile.value = false
      // 顯示成功訊息
      alert('Biography updated successfully!')
    } else {
      profileErrorMessage.value = result.error || 'Failed to update biography'
    }
  } catch (error) {
    profileErrorMessage.value = error.message || 'Failed to update biography'
  } finally {
    isSubmittingProfile.value = false
  }
}

// Load posts when component is mounted
onMounted(async () => {
  try {
    await postsStore.fetchPosts()
  } catch (error) {
    console.error('Error fetching posts:', error)
  }
})
</script>

<template>
  <div class="profile-wrapper">
    <!-- 導航欄 -->
    <header class="profile-header-nav">
      <div class="profile-nav">
        <router-link to="/" class="logo">Social</router-link>
        <nav>
          <router-link to="/posts" class="nav-link" active-class="active">Posts</router-link>
          <router-link to="/profile" class="nav-link" active-class="active">Profile</router-link>
          <a href="#" @click.prevent="authStore.logout" class="nav-link">Logout</a>
        </nav>
      </div>
    </header>
    
    <div class="profile-container">
    <div class="profile-header">
      <div class="profile-info">
        <h1>{{ user?.userName || 'User' }}</h1>
        <p class="profile-email">{{ user?.email || 'No email provided' }}</p>
        
        <!-- Biography Section -->
        <div class="biography-section">
          <div v-if="!isEditingProfile" class="biography-display">
            <p class="profile-bio">{{ user?.biography || 'No biography provided' }}</p>
            <button @click="startEditingProfile" class="edit-profile-button">
              Edit Biography
            </button>
          </div>
          
          <div v-else class="biography-edit">
            <div v-if="profileErrorMessage" class="error-message">
              {{ profileErrorMessage }}
            </div>
            
            <form @submit.prevent="updateProfile" class="inline-edit-form">
              <div class="form-group">
                <label for="biography">Biography</label>
                <textarea 
                  id="biography" 
                  v-model="biography" 
                  rows="4"
                  placeholder="Tell us about yourself..."
                  class="biography-textarea"
                ></textarea>
              </div>
              
              <div class="form-actions">
                <button type="submit" :disabled="isSubmittingProfile" class="save-button">
                  {{ isSubmittingProfile ? 'Saving...' : 'Save' }}
                </button>
                
                <button 
                  type="button" 
                  @click="cancelEditingProfile"
                  class="cancel-button"
                >
                  Cancel
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
    
    <!-- User Posts -->
    <div class="user-posts">
      <h2>Your Posts</h2>
      
      <div v-if="userPosts.length === 0" class="no-posts">
        <p>You haven't created any posts yet.</p>
      </div>
      
      <div v-else class="post-cards">
        <div v-for="post in userPosts" :key="post.postId" class="post-card">
          <div class="post-header">
            <div class="post-meta">
              <span class="post-date">{{ new Date(post.createdAt).toLocaleString() }}</span>
            </div>
          </div>
          
          <div class="post-content">
            <p>{{ post.content }}</p>
            <img v-if="post.image" :src="post.image" class="post-image" alt="Post image" />
          </div>
          
          <div class="post-stats">
            <span>{{ post.comments?.length || 0 }} comments</span>
          </div>
        </div>
      </div>
    </div>
  </div>
  </div>
</template>

<style scoped>
.profile-wrapper {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

.profile-wrapper::before {
  content: '';
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(circle at 30% 20%, rgba(255, 255, 255, 0.1) 0%, transparent 50%),
              radial-gradient(circle at 70% 80%, rgba(255, 255, 255, 0.1) 0%, transparent 50%);
  pointer-events: none;
  z-index: 0;
}

.profile-header-nav {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
  position: sticky;
  top: 0;
  z-index: 100;
}

.profile-nav {
  max-width: 1200px;
  margin: 0 auto;
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  font-size: 2rem;
  font-weight: 800;
  background: linear-gradient(135deg, #1da1f2, #667eea);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  text-decoration: none;
  letter-spacing: -1px;
  transition: all 0.3s ease;
}

.logo:hover {
  transform: scale(1.05);
}

nav {
  display: flex;
  gap: 2rem;
  align-items: center;
}

.nav-link {
  color: #657786;
  text-decoration: none;
  font-weight: 500;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  transition: all 0.3s ease;
}

.nav-link:hover, .nav-link.active {
  color: #1da1f2;
  background-color: rgba(29, 161, 242, 0.1);
}

.profile-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem 1rem;
  position: relative;
  z-index: 1;
}

.profile-header {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.3);
  overflow: hidden;
  margin-bottom: 2rem;
  animation: fadeInUp 0.6s ease-out;
}



.profile-info {
  padding: 1.5rem;
}

.profile-info h1 {
  margin-bottom: 0.5rem;
}

.profile-email {
  color: #666;
  margin-bottom: 1rem;
}

.biography-section {
  margin-top: 1rem;
}

.biography-display .profile-bio {
  margin-bottom: 1.5rem;
  line-height: 1.5;
  padding: 1rem;
  background: rgba(248, 249, 250, 0.5);
  border-radius: 12px;
  border: 1px dashed rgba(108, 117, 125, 0.3);
  min-height: 60px;
}

.biography-edit {
  margin-top: 1rem;
}

.inline-edit-form {
  width: 100%;
}

.biography-textarea {
  width: 100%;
  min-height: 100px;
  resize: vertical;
}

.form-actions {
  display: flex;
  gap: 1rem;
  margin-top: 1rem;
  justify-content: flex-start;
}





.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 1rem;
  border: 2px solid #e1e8ed;
  border-radius: 15px;
  font-size: 1rem;
  font-family: inherit;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.8);
  resize: vertical;
}

.form-group input:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #1da1f2;
  box-shadow: 0 0 0 3px rgba(29, 161, 242, 0.1);
  background: white;
}



/* 統一按鈕樣式 */
button, .cancel-button, .edit-profile-button {
  padding: 0.8rem 1.8rem;
  border: none;
  border-radius: 50px;
  font-size: 0.9rem;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.4s ease;
  background: linear-gradient(135deg, #1da1f2, #667eea);
  color: white;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

button:hover:not(:disabled), .edit-profile-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 15px 35px rgba(29, 161, 242, 0.4);
  background: linear-gradient(135deg, #667eea, #1da1f2);
}

button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.cancel-button {
  background: linear-gradient(135deg, #6c757d, #495057);
}

.cancel-button:hover {
  background: linear-gradient(135deg, #495057, #6c757d);
  transform: translateY(-2px);
  box-shadow: 0 15px 35px rgba(108, 117, 125, 0.4);
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

.user-posts {
  margin-top: 2rem;
}

.user-posts h2 {
  margin-bottom: 1rem;
}

.post-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.3);
  padding: 2rem;
  margin-bottom: 2rem;
  animation: fadeInUp 0.6s ease-out;
}

.post-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 1rem;
}

.post-meta {
  font-size: 0.9rem;
  color: #666;
}

.post-content {
  margin-bottom: 1rem;
}

.post-image {
  max-width: 100%;
  border-radius: 4px;
  margin-top: 1rem;
}

.post-stats {
  font-size: 0.9rem;
  color: #666;
  padding-top: 1rem;
  border-top: 1px solid #eee;
}

.no-posts {
  text-align: center;
  padding: 2rem;
  color: #666;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.3);
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
  .profile-nav {
    padding: 1rem;
  }
  
  .profile-container {
    padding: 1rem;
  }
  
  .profile-header,
  .post-card {
    padding: 1.5rem;
    border-radius: 15px;
  }
  
  .logo {
    font-size: 1.5rem;
  }
  
  nav {
    gap: 1rem;
  }
  
  .nav-link {
    padding: 0.4rem 0.8rem;
    font-size: 0.9rem;
  }
  
  .form-actions {
    flex-direction: column;
    gap: 0.8rem;
  }
}
</style> 