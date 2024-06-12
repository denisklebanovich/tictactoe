<template>
  <nav class="navbar">
    <button class="nav-link" @click="loadCpu">Load CPU</button>
    <router-link to="/" class="nav-link">Home</router-link>
    <template v-if="userStore.username">
      <router-link to="/game" class="nav-link">Game</router-link>
      <button @click="signOut" class="action-button">Logout</button>
    </template>
    <template v-else>
      <router-link to="/login" class="nav-link">Login</router-link>
    </template>
    <div class="avatar">
      <img v-if="profileImage" :src="profileImage" alt="Profile Image" width="40" height="40">
      <img v-else src="https://cdn-icons-png.flaticon.com/512/3541/3541871.png" alt="Profile Image" width="40"
           height="40">
      <input type="file" @change="uploadProfileImage" class="profile-image-input">
    </div>
  </nav>
</template>

<script setup>
import {logout} from "@/services/AuthService.ts";
import {useUserStore} from "@/stores/userStore.ts";
import router from "@/router/index.js";
import {loadCPU} from "@/services/CpuService.ts";
import {onMounted, ref} from "vue";

const userStore = useUserStore();
const profileImage = ref(null);

const signOut = async () => {
  await logout();
  await router.push('/login');
};

const loadCpu = async () => {
  console.log(await loadCPU());
};

const uploadProfileImage = async (event) => {
  const file = event.target.files[0];
  await userStore.setProfileImage(file);
};

const fetchProfileImage = async () => {
  try {
    const imageUrl = await userStore.getProfileImage(userStore.username.value);
    if (imageUrl) {
      profileImage.value = `${imageUrl}?${new Date().getTime()}`; // Add a cache-busting query parameter
    }
  } catch (error) {
    console.error('Error fetching profile image:', error);
  }
};

onMounted(async () => {
  await fetchProfileImage();
});

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

.avatar {
  display: flex;
  align-items: center;
  position: relative;
}

.profile-image-input {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 0;
  cursor: pointer;
}
</style>
