<script setup lang="ts">
import {computed, ref} from 'vue'
import axios from "axios";
import router from "@/router";

const username = ref('')
const game = ref(undefined)

const submitUsername = async () => {
  await axios.post('http://localhost:8000/api/game/register', {username: username.value})
}

const getGame = async () => {
  const response = await axios.get('http://localhost:8000/api/game/create')
  game.value = response.data
}
//check for game every 5 seconds and when game is created, redirect to game page

computed(() => {
  setInterval(() => {
    getGame()
    if (game.value && game.value.id) {
      router.push({name: 'GameBoard', params: {game: game}})
    }
  }, 5000)
})

</script>

<template>
  <div>
    <input v-model="username" type="text" placeholder="Enter your username"/>
    <button @click="submitUsername">Submit</button>
  </div>
</template>

<style scoped>

</style>