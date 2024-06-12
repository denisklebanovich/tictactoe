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
    <div>
      <h1>Results</h1>
      <ul class="results">
        <li v-for="result in results" :key="result.id">
          <span>{{ result.firstPlayer }} vs {{ result.secondPlayer }}</span>
          <p>{{ result.status }}</p>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import router from '@/router';
import {useUserStore} from "@/stores/userStore.ts";
import {onMounted, ref} from "vue";
import * as GameService from "@/services/GameService.ts";

const userStore = useUserStore();

const results = ref([]);

onMounted(async () => {
  results.value = await GameService.getResults();
});
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

.results {
  list-style-type: none;
  padding: 0;

  li {
    margin: 10px 0;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 5px;
    background-color: #fff;
  }
}
</style>
