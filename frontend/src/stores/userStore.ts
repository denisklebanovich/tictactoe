import {defineStore} from "pinia";
import {computed, ref} from "vue";

export const useUserStore = defineStore('user', () => {
    const _username = ref('');
    const username = computed(() => {
        const localUsername = localStorage.getItem('username');
        _username.value = localUsername || '';
        return _username;
    });

    return {
        username
    }
})
