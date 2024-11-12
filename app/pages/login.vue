<script lang="ts" setup>
definePageMeta({ layout: false });
const { $auth } = useNuxtApp();
const form = reactive({ username: '', password: '' });
const error = ref({ message: '', status: 0, timestamp: 0 });
const pending = ref(false);
const token = useCookie('token');
const principal = usePrincipal();

async function login() {
  pending.value = true;
  error.value = { message: '', status: 0, timestamp: 0 };
  try {
    const response = await $auth.login(form);
    token.value = response.token;
    principal.value = response;
    navigateTo('/');
  } catch (e: any) {
    error.value = e;
  }
  pending.value = false;
}
</script>
<template>
  <section id="login">
    <form @submit.prevent="login">
      <div class="input-box">
        <label for="username">Usuário:</label>
        <input type="text" id="username" name="username" v-model="form.username" />
      </div>
      <div class="input-box">
        <label for="password">Senha:</label>
        <input type="password" id="password" name="password" v-model="form.password" />
      </div>
      <div v-if="error.message" class="form-message">{{ $t(error.message) }}</div>
      <div class="form-button">
        <button type="submit">
          <span>Entrar</span>
          <Icon name="line-md:loading-twotone-loop" v-if="pending" />
          <Icon name="line-md:login" v-else />
        </button>
      </div>
    </form>
  </section>
</template>
<style>
#login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

form {
  display: flex;
  flex-direction: column;
  width: 300px;
  gap: 10px;
}

.input-box label {
  text-align: right;
  padding: 0 10px;
  width: 70px;
}
</style>
