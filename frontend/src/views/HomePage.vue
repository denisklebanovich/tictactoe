<template>
  <div class="home">
    <h1>Welcome to Tic Tac Toe!</h1>
    <div v-if="userStore.username">
      <p>Hello, {{ userStore.username }}!</p>
      <button class="join-game-btn" @click="router.push('/game')">Join Game</button>
    </div>
    <div v-else>
      <p>Log in to join the game</p>
      <button class="login-btn" @click="router.push('/login')">Login</button>
    </div>
  </div>
</template>

<script setup>
import router from '@/router';
import {useUserStore} from "@/stores/userStore.ts";

const userStore = useUserStore();
</script>

<style scoped>
.home {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-color: #f4f4f9;
  color: #333;
  font-family: Arial, sans-serif;
  text-align: center;
}

h1 {
  color: #5c67f2;
  font-weight: bold;
}

.join-game-btn {
  background-color: #4caf50;
  color: white;
  padding: 10px 20px;
  margin-top: 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.join-game-btn:hover {
  background-color: #45a044;
}

.login-btn {
  background-color: #f57c00;
  color: white;
  padding: 10px 20px;
  margin-top: 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}
</style>
