<template>
  <div class="login-container">
    <div class="login-form">
      <h1>Register</h1>
      <p v-if="errorMessage">{{ errorMessage }}</p>
      <form @submit.prevent="handleSubmit" v-if="!showVerification">
        <label for="email">Email:</label>
        <input type="email" id="email" v-model="email" required/>
        <label for="username">Username:</label>
        <input type="text" id="username" v-model="username" required/>
        <label for="password">Password:</label>
        <input type="password" id="password" v-model="password" required/>
        <button type="submit">Register</button>
      </form>
      <form @submit.prevent="handleVerification" v-if="showVerification">
        <label for="verificationCode">Verification Code:</label>
        <input type="text" id="verificationCode" v-model="verificationCode" required/>
        <button type="submit">Verify</button>
      </form>
      <div class="register-link">
        <span>Aleady have an account?</span>
        <router-link to="/login">Login Here</router-link>
      </div>
    </div>
  </div>
</template>
<script setup>
import {ref} from 'vue';
import {useRouter} from 'vue-router';
import axios from "axios"; // Import router

const email = ref('');
const username = ref('');
const password = ref('');
const errorMessage = ref(null);

const router = useRouter();

const verificationCode = ref('');
const showVerification = ref(false);

const handleSubmit = async () => {
  localStorage.setItem('username', username.value);
  const response = await axios.post('/api/auth/register', {
    email: email.value,
    username: username.value,
    password: password.value,
  })
      .catch(error => {
        errorMessage.value = error.response.data;
      });
  if (response.status === 200) {
    await router.push('/verify');
  }
};

const handleVerification = async () => {
  const response = await axios.post('/api/auth/confirm', {
    username: username.value,
    verificationCode: verificationCode.value,
  })
      .catch(error => {
        errorMessage.value = error.response.data;
      });
  if (response.status === 200) {
    router.push('/login');
  }
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


