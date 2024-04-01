import { createRouter, createWebHistory } from 'vue-router'
import TicTacToe from "@/views/TicTacToe.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'TicTacToe',
      component: TicTacToe
    },
  ]
})

export default router
