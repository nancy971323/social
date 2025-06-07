<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from './store/auth'

const authStore = useAuthStore()
const route = useRoute()

// Check if current route has its own navigation
const hasOwnNavigation = computed(() => 
  ['home', 'posts', 'profile'].includes(route.name)
)
</script>

<template>
  <div :class="{ 'app-container': !hasOwnNavigation, 'full-width': hasOwnNavigation }">
    <header v-if="authStore.isAuthenticated && !hasOwnNavigation" class="app-header">
      <div class="logo">Social</div>
      <nav>
        <router-link to="/">Home</router-link>
        <router-link to="/profile">Profile</router-link>
        <a href="#" @click.prevent="authStore.logout">Logout</a>
      </nav>
    </header>
    
    <main :class="{ 'app-content': !hasOwnNavigation, 'full-content': hasOwnNavigation }">
      <router-view />
    </main>
  </div>
</template>

<style>
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

html {
  margin: 0;
  padding: 0;
  width: 100%;
}

body {
  font-family: Arial, sans-serif;
  background-color: #f7f7f7;
  color: #333;
  line-height: 1.6;
  margin: 0;
  padding: 0;
  width: 100%;
  overflow-x: hidden;
}

.app-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1rem;
}

.full-width {
  width: 100%;
  max-width: none;
  margin: 0;
  padding: 0;
}

.full-content {
  min-height: 100vh;
  padding: 0;
}

.app-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 0;
  border-bottom: 1px solid #eee;
  margin-bottom: 2rem;
}

.logo {
  font-size: 1.5rem;
  font-weight: bold;
  color: #333;
}

nav {
  display: flex;
  gap: 1.5rem;
}

nav a {
  color: #555;
  text-decoration: none;
  transition: color 0.3s;
}

nav a:hover, 
nav a.router-link-active {
  color: #000;
}

.app-content {
  min-height: calc(100vh - 100px);
  padding-bottom: 2rem;
}

button {
  cursor: pointer;
  padding: 0.5rem 1rem;
  background-color: #4a6cf7;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #3a5cf7;
}

button:disabled {
  background-color: #b0b0b0;
  cursor: not-allowed;
}

input, textarea {
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  width: 100%;
  margin-bottom: 1rem;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: bold;
}

.error-message {
  color: #e74c3c;
  margin-top: 0.5rem;
  font-size: 0.9rem;
}
</style>
