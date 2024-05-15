<template>
  <div class="login-container">
    <div class="login-form">
      <h1>Login</h1>
      <form @submit.prevent="handleSubmit">
        <label for="username">Username:</label>
        <input type="text" id="username" v-model="username" required/>
        <label for="password">Password:</label>
        <input type="password" id="password" v-model="password" required/>
        <button type="submit">Login</button>
        <p v-if="errorMessage">{{ errorMessage }}</p>
      </form>
      <div class="register-link">
        <span>Don't have an account?</span>
        <router-link to="/register">Register Here</router-link>
      </div>
    </div>
  </div>
</template>
<script setup>
import {ref} from 'vue';
import {useRouter} from 'vue-router';
import {login} from '@/services/AuthService.ts';
import {useUserStore} from "@/stores/userStore.ts";

const username = ref('');
const password = ref('');
const errorMessage = ref(null);
const userStore = useUserStore();

const router = useRouter();

const handleSubmit = async () => {
  login(username.value, password.value)
    .then(() => {
      userStore.username = username.value;
      router.push('/');
    })
    .catch((error) => {
      errorMessage.value = error.response.data.message;
    });
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-size: cover;
  background-position: center;
}

.login-form {
  background-color: rgba(0, 0, 0, 0.7);
  padding: 30px;
  border-radius: 5px;
  color: #fff;
  text-align: center;
}

.login-form h1 {
  margin-bottom: 20px;
}

.login-form label {
  display: block;
  margin-bottom: 5px;
}

.login-form input,
.login-form button {
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 3px;
}

.login-form button {
  background-color: #00bcd4;
  color: #fff;
  cursor: pointer;
}

.login-form button:hover {
  background-color: #00acc1;
}

.login-form p {
  color: red;
  margin-top: 10px;
}

.register-link {
  margin-top: 15px;
  color: #ccc;
}

.register-link a {
  color: #00bcd4;
  text-decoration: none;
}

.register-link a:hover {
  text-decoration: underline;
}
</style>


