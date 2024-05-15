<template>
  <div class="login-container">
    <div class="login-form">
      <h1>Login</h1>
      <form @submit.prevent="handleSubmit">
        <label for="verificationCode">Verification Code:</label>
        <input type="text" id="verificationCode" v-model="verificationCode" required/>
        <button type="submit">Verify</button>
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
import axios from "axios"; // Import router

const verificationCode = ref('');
const errorMessage = ref(null);

const router = useRouter();

const handleSubmit = async () => {
  const username = localStorage.getItem('username');
  const response = await axios.post('/api/auth/confirm', {
    username: username,
    confirmationCode: verificationCode.value,
  })
      .catch(error => {
        errorMessage.value = error.response.data.message;
      })

  if (response.status === 200) {
    await router.push('/login');
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


