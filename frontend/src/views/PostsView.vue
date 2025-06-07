<script setup>
import { ref, onMounted, computed } from 'vue'
import { usePostsStore } from '@/store/posts'
import { useAuthStore } from '@/store/auth'

const postsStore = usePostsStore()
const authStore = useAuthStore()

const newPostContent = ref('')
const isSubmittingPost = ref(false)
const postErrorMessage = ref('')
const isLoading = ref(false)
const commentInputs = ref({})
const editingPostId = ref(null)
const editingContent = ref('')

const posts = computed(() => postsStore.posts)
const user = computed(() => authStore.user)

const fetchPosts = async () => {
  try {
    isLoading.value = true
    await postsStore.fetchPosts()
  } catch (error) {
    console.error('Error fetching posts:', error)
  } finally {
    isLoading.value = false
  }
}

const submitPost = async () => {
  if (!newPostContent.value.trim()) {
    postErrorMessage.value = 'Post content cannot be empty'
    return
  }
  
  try {
    isSubmittingPost.value = true
    await postsStore.createPost(newPostContent.value)
    newPostContent.value = ''
    postErrorMessage.value = ''
  } catch (error) {
    postErrorMessage.value = error.message || 'Failed to submit post'
  } finally {
    isSubmittingPost.value = false
  }
}

const startEditingPost = (post) => {
  editingPostId.value = post.postId
  editingContent.value = post.content
}

const cancelEditingPost = () => {
  editingPostId.value = null
  editingContent.value = ''
}

const updatePost = async (postId) => {
  if (!editingContent.value.trim()) {
    return
  }
  
  try {
    await postsStore.updatePost(postId, editingContent.value)
    editingPostId.value = null
    editingContent.value = ''
  } catch (error) {
    console.error('Error updating post:', error)
  }
}

const deletePost = async (postId) => {
  if (confirm('Are you sure you want to delete this post?')) {
    try {
      await postsStore.deletePost(postId)
    } catch (error) {
      console.error('Error deleting post:', error)
    }
  }
}

const submitComment = async (postId) => {
  if (!commentInputs.value[postId] || !commentInputs.value[postId].trim()) {
    return
  }
  
  try {
    await postsStore.addComment(postId, commentInputs.value[postId])
    commentInputs.value[postId] = ''
  } catch (error) {
    console.error('Error submitting comment:', error)
  }
}

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleString()
}

onMounted(fetchPosts)
</script>

<template>
  <div class="posts-wrapper">
    <!-- 導航欄 -->
    <header class="posts-header">
      <div class="posts-nav">
        <router-link to="/" class="logo">Social</router-link>
        <nav>
          <router-link to="/posts" class="nav-link" active-class="active">Posts</router-link>
          <router-link to="/profile" class="nav-link" active-class="active">Profile</router-link>
          <a href="#" @click.prevent="authStore.logout" class="nav-link">Logout</a>
        </nav>
      </div>
    </header>
    
    <div class="posts-container">
    <!-- New Post Form -->
    <div class="post-form-card">
      <div v-if="postErrorMessage" class="error-message">
        {{ postErrorMessage }}
      </div>
      
      <form @submit.prevent="submitPost">
        <div class="form-group">
          <textarea 
            v-model="newPostContent" 
            placeholder="What's on your mind?" 
            rows="3"
            required
          ></textarea>
        </div>
        
        <div class="form-actions">
          <button type="submit" :disabled="isSubmittingPost">
            {{ isSubmittingPost ? 'Submitting...' : 'Post' }}
          </button>
        </div>
      </form>
    </div>
    
    <!-- Posts List -->
    <div class="posts-list">
      <div v-if="isLoading" class="loading-indicator">
        Loading posts...
      </div>
      
      <div v-else-if="posts.length === 0" class="no-posts">
        <p>No posts yet. Be the first to share something!</p>
      </div>
      
      <div v-else class="post-cards">
        <div v-for="post in posts" :key="post.postId" class="post-card">
          <div class="post-header">
            <div class="post-author">
              <strong>{{ post.userName || 'Anonymous' }}</strong>
            </div>
            <div class="post-meta">
              <span class="post-date">{{ formatDate(post.createdAt) }}</span>
              
              <div v-if="user && post.userId === user.userId" class="post-actions">
                <button 
                  v-if="editingPostId !== post.postId"
                  @click="startEditingPost(post)" 
                  class="icon-button edit-button"
                >
                  Edit
                </button>
                <button @click="deletePost(post.postId)" class="icon-button delete-button">
                  Delete
                </button>
              </div>
            </div>
          </div>
          
          <!-- Post Content - Normal View -->
          <div v-if="editingPostId !== post.postId" class="post-content">
            <p>{{ post.content }}</p>
          </div>
          
          <!-- Post Content - Edit Mode -->
          <div v-else class="post-edit-form">
            <textarea 
              v-model="editingContent"
              rows="3"
              class="edit-textarea"
              placeholder="Edit your post content..."
            ></textarea>
            <div class="edit-actions">
              <button @click="updatePost(post.postId)">
                Save
              </button>
              <button @click="cancelEditingPost" class="cancel-button">
                Cancel
              </button>
            </div>
          </div>
          
          <!-- Comments Section -->
          <div class="post-comments">
            <h4>Comments</h4>
            
            <div v-if="post.comments && post.comments.length > 0" class="comments-list">
              <div v-for="comment in post.comments" :key="comment.commentId" class="comment">
                <div class="comment-header">
                  <strong>{{ comment.userName || 'Anonymous' }}</strong>
                  <span class="comment-date">{{ formatDate(comment.createdAt) }}</span>
                </div>
                <p>{{ comment.content }}</p>
              </div>
            </div>
            <div v-else class="no-comments">
              <p>No comments yet.</p>
            </div>
            
            <!-- Add Comment Form -->
            <div class="add-comment">
              <form @submit.prevent="submitComment(post.postId)">
                <input 
                  type="text" 
                  v-model="commentInputs[post.postId]" 
                  placeholder="Write a comment..."
                  required
                />
                <button type="submit">Comment</button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
    </div>
  </div>
</template>

<style scoped>
.posts-wrapper {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

.posts-wrapper::before {
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

.posts-header {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
  position: sticky;
  top: 0;
  z-index: 100;
}

.posts-nav {
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

.posts-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem 1rem;
  position: relative;
  z-index: 1;
}

.post-form-card, .post-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.3);
  padding: 2rem;
  margin-bottom: 2rem;
  animation: fadeInUp 0.6s ease-out;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-actions {
  display: flex;
  gap: 1rem;
}

/* 編輯表單樣式 */
.post-edit-form {
  margin: 1rem 0;
}

.edit-textarea {
  width: 100%;
  padding: 1rem;
  border: 2px solid rgba(102, 126, 234, 0.3);
  border-radius: 12px;
  font-size: 1rem;
  line-height: 1.5;
  resize: vertical;
  transition: all 0.3s ease;
  font-family: inherit;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
}

.edit-textarea:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
  background: rgba(255, 255, 255, 0.95);
}

.edit-actions {
  display: flex;
  gap: 1rem;
  margin-top: 1rem;
  justify-content: flex-end;
}

/* 按鈕樣式 */
button {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 25px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 0.9rem;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
}

button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.cancel-button {
  background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
}

.cancel-button:hover {
  box-shadow: 0 8px 25px rgba(231, 76, 60, 0.3);
}

/* 輸入框樣式 */
textarea, input[type="text"] {
  width: 100%;
  padding: 1rem;
  border: 2px solid rgba(102, 126, 234, 0.3);
  border-radius: 12px;
  font-size: 1rem;
  line-height: 1.5;
  resize: vertical;
  transition: all 0.3s ease;
  font-family: inherit;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
}

textarea:focus, input[type="text"]:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
  background: rgba(255, 255, 255, 0.95);
}

.error-message {
  background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
  color: white;
  padding: 0.75rem 1rem;
  border-radius: 8px;
  margin-bottom: 1rem;
  font-weight: 500;
}

.loading-indicator, .no-posts {
  text-align: center;
  padding: 3rem;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  margin-bottom: 2rem;
  color: #657786;
  font-size: 1.1rem;
}

.post-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 1rem;
  padding-bottom: 0.5rem;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
}

.post-author {
  font-size: 1.1rem;
  color: #14171a;
}

.post-meta {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.post-date {
  color: #657786;
  font-size: 0.9rem;
}

.post-actions {
  display: flex;
  gap: 0.5rem;
}

.icon-button {
  background: none;
  border: 1px solid rgba(102, 126, 234, 0.3);
  color: #657786;
  padding: 0.4rem 0.8rem;
  border-radius: 15px;
  font-size: 0.8rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.icon-button:hover {
  background: rgba(102, 126, 234, 0.1);
  color: #667eea;
  border-color: #667eea;
  transform: none;
  box-shadow: none;
}

.edit-button:hover {
  background: rgba(52, 152, 219, 0.1);
  color: #3498db;
  border-color: #3498db;
}

.delete-button:hover {
  background: rgba(231, 76, 60, 0.1);
  color: #e74c3c;
  border-color: #e74c3c;
}

.post-content {
  margin: 1.5rem 0;
  color: #14171a;
  font-size: 1rem;
  line-height: 1.6;
}

.post-comments {
  border-top: 1px solid rgba(0, 0, 0, 0.1);
  padding-top: 1.5rem;
  margin-top: 1.5rem;
}

.post-comments h4 {
  color: #14171a;
  margin-bottom: 1rem;
  font-size: 1rem;
  font-weight: 600;
}

.comments-list {
  margin-bottom: 1.5rem;
}

.comment {
  background: rgba(247, 249, 250, 0.8);
  padding: 1rem;
  border-radius: 12px;
  margin-bottom: 0.5rem;
  backdrop-filter: blur(10px);
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}

.comment-header strong {
  color: #14171a;
  font-size: 0.9rem;
}

.comment-date {
  color: #657786;
  font-size: 0.8rem;
}

.comment p {
  color: #14171a;
  font-size: 0.9rem;
  line-height: 1.4;
}

.no-comments {
  color: #657786;
  font-style: italic;
  text-align: center;
  padding: 1rem;
  background: rgba(247, 249, 250, 0.5);
  border-radius: 12px;
  margin-bottom: 1.5rem;
}

.add-comment form {
  display: flex;
  gap: 0.5rem;
  align-items: flex-end;
}

.add-comment input {
  flex: 1;
  padding: 0.75rem;
  border: 1px solid rgba(102, 126, 234, 0.3);
  border-radius: 20px;
  font-size: 0.9rem;
  background: rgba(255, 255, 255, 0.8);
}

.add-comment button {
  padding: 0.75rem 1.2rem;
  font-size: 0.9rem;
  border-radius: 20px;
  white-space: nowrap;
}

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
  .posts-nav {
    padding: 1rem;
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
  
  .posts-container {
    padding: 1rem 0.5rem;
  }
  
  .post-form-card, .post-card {
    padding: 1.5rem;
    margin-bottom: 1.5rem;
  }
  
  .post-header {
    flex-direction: column;
    gap: 0.5rem;
    align-items: flex-start;
  }
  
  .post-meta {
    align-self: flex-end;
  }
  
  .add-comment form {
    flex-direction: column;
    gap: 0.75rem;
  }
  
  .add-comment input {
    margin-bottom: 0;
  }
}
</style> 