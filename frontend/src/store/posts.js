import { defineStore } from 'pinia'
import axios from 'axios'

export const usePostsStore = defineStore('posts', {
  state: () => ({
    posts: [],
    isLoading: false,
    error: null
  }),
  
  actions: {
    async fetchPosts() {
      this.isLoading = true
      this.error = null
      try {
        const response = await axios.get('/api/post/list')
        if (response.data.success) {
          this.posts = response.data.data || []
        } else {
          this.posts = []
        }
        return this.posts
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch posts'
        this.posts = []
        throw error
      } finally {
        this.isLoading = false
      }
    },
    
    async createPost(content) {
      this.isLoading = true
      this.error = null
      try {
        const response = await axios.post('/api/post/create', {
          content: content
        })
        
        // Add the new post to our posts array
        if (response.data.success && response.data.data) {
          await this.fetchPosts() // Refresh posts to get the latest data
        }
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to create post'
        throw error
      } finally {
        this.isLoading = false
      }
    },
    
    async updatePost(postId, content) {
      this.isLoading = true
      this.error = null
      try {
        const response = await axios.put('/api/post/edit', {
          postId: postId,
          content: content
        })
        
        // Refresh posts to get the latest data
        if (response.data.success) {
          await this.fetchPosts()
        }
        
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to update post'
        throw error
      } finally {
        this.isLoading = false
      }
    },
    
    async deletePost(postId) {
      this.isLoading = true
      this.error = null
      try {
        const response = await axios.delete(`/api/post/delete/${postId}`)
        
        // Refresh posts to get the latest data
        if (response.data.success) {
          await this.fetchPosts()
        }
        
        return true
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to delete post'
        throw error
      } finally {
        this.isLoading = false
      }
    },
    
    async addComment(postId, content) {
      try {
        const response = await axios.post('/api/comment/create', {
          postId: postId,
          content: content
        })
        
        // Refresh posts to get the latest data with comments
        if (response.data.success) {
          await this.fetchPosts()
        }
        
        return response.data
      } catch (error) {
        throw error.response?.data?.message || 'Failed to add comment'
      }
    }
  }
}) 