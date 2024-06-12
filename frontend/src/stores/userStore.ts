import {defineStore} from "pinia";
import {computed, ref} from "vue";
import ProfileService from "@/services/ProfileService";

export const useUserStore = defineStore('user', () => {
        const _username = ref('');
        const username = computed(() => {
            const localUsername = localStorage.getItem('username');
            _username.value = localUsername || '';
            return _username;
        });
        const profileImage = ref();

        function setUsername(name: string) {
            _username.value = name;
        }

        async function getProfileImage(username: string) {
            return await ProfileService.getProfileImage(username);
        }

        async function setProfileImage(image: File) {
            await ProfileService.setProfileImage(image);
            return await this.getProfileImage(this.username);
        }

        return {
            username, setUsername, profileImage, getProfileImage, setProfileImage
        }
    }
)
