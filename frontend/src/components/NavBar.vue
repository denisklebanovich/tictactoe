<template>
  <nav class="navbar">
    <router-link to="/" class="nav-link">Home</router-link>
    <template v-if="userStore.username">
      <router-link to="/game" class="nav-link">Game</router-link>
      <button @click="signOut" class="action-button">Logout</button>
    </template>
    <template v-else>
      <router-link to="/login" class="nav-link">Login</router-link>
    </template>
  </nav>
</template>

<script setup>
import {logout} from "@/services/AuthService.ts";
import {useUserStore} from "@/stores/userStore.ts";
import router from "@/router/index.js";

const userStore = useUserStore();

const signOut = async () => {
  await logout();
  await router.push('/login');
};
</script>

<style scoped>
.navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 20px;
  background: #f5f5f5;
  border-bottom: 1px solid #dddddd;
}

.nav-link {
  padding: 10px 15px;
  margin-right: 10px;
  text-decoration: none;
  color: #333;
  font-size: 16px;
  border-radius: 8px;
  transition: background-color 0.3s;
}

.nav-link:hover {
  background-color: #e0e0e0;
}

.action-button {
  padding: 10px 15px;
  background-color: #ffffff;
  border: 2px solid #dddddd;
  color: #333;
  font-size: 16px;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s, box-shadow 0.2s;
}

.action-button:hover {
  background-color: #e0e0e0;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}
</style>
