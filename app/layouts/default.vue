<script lang="ts" setup>
const principal = usePrincipal();
</script>
<template>
  <nav>
    <div class="brand">
      <span class="header">Optimus&lt;GAIA&gt;</span>
      <span class="subheader">Gerenciador de aplicações</span>
    </div>
    <ul class="menu">
      <li>
        <NuxtLink to="/">
          <Icon name="ic:home" />
          <span>Home</span>
        </NuxtLink>
      </li>
      <li>
        <NuxtLink to="/groups">
          <Icon name="ic:group" />
          <span>Grupos</span>
        </NuxtLink>
      </li>
      <li>
        <NuxtLink to="/users">
          <Icon name="ic:person" />
          <span>Usuários</span>
        </NuxtLink>
      </li>
      <li>
        <NuxtLink to="/customers">
          <Icon name="ic:account-balance" />
          <span>Clientes</span>
        </NuxtLink>
      </li>
      <li>
        <NuxtLink to="/apps">
          <Icon name="ic:apps" />
          <span>Aplicações</span>
        </NuxtLink>
      </li>
    </ul>
  </nav>
  <slot />
</template>
<style scoped>
nav {
  display: flex;
  border-bottom: 1px solid #6ea9f0;
  align-items: center;
  justify-content: space-between;
  height: 50px;
  font-family: 'Roboto Condensed', sans-serif;
  padding: 0 0.5rem;
}

.brand {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  font-family: 'Roboto Condensed', sans-serif;
}

.brand .header {
  font-size: 1.3rem;
  font-weight: 700;
  text-transform: uppercase;
  color: #6ea9f0;
}

.brand .subheader {
  font-size: 0.7rem;
  color: var(--color-light);
}

.menu {
  display: flex;
  border-radius: 0.5rem;
  list-style: none;
  align-items: center;
  gap: 1rem;
}

.menu .router-link-active {
  color: #6ea9f0;
}

.menu li a {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: var(--color-light);
  text-decoration: none;
  transition: color 0.3s ease;
}

.menu li a:hover {
  color: var(--primary);
}

.menu li a span {
  font-size: 0.8rem;
}

.menu li a .iconify {
  font-size: 1.5rem;
}
</style>
