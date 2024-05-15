import {createRouter, createWebHistory} from 'vue-router'
import TicTacToe from "@/views/TicTacToe.vue";
import LoginPage from "@/views/LoginPage.vue";
import RegistrationPage from "@/views/RegistrationPage.vue";
import VerifyPage from "@/views/VerifyPage.vue";
import GamePage from "@/views/GamePage.vue";
import HomePage from "@/views/HomePage.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'Home',
            component: HomePage,
            meta: {
                requiresAuth: false
            }
        },
        {
            path: '/game',
            name: 'Game',
            component: GamePage,
            meta: {
                requiresAuth: true
            }
        },
        {
            path: '/login',
            name: 'Login',
            component: LoginPage
        },
        {
            path: '/register',
            name: 'Register',
            component: RegistrationPage
        },
        {
            path: '/verify',
            name: 'Verify',
            component: VerifyPage
        }
    ]
})
router.beforeEach((to, from, next) => {
    if (to.matched.some(record => record.meta.requiresAuth)) {
        if (!localStorage.getItem('accessToken')) {
            next({
                path: '/login',
            })
        } else {
            next()
        }
    } else {
        next()
    }
})

export default router
