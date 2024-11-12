<script setup lang="ts">
definePageMeta({ middleware: 'auth' });
const { $group, $user, $dateFormat } = useNuxtApp();
type Filter = 'name' | 'username';
const filters = ['name', 'username'] as Filter[];
const filterBy = ref<Filter>(filters[0]);
const filter = ref('');
const showFilter = ref(false);
const showForm = ref(false);
const pending = ref(false);
const error = ref<ResponseError>({ message: '', status: 0, timestamp: 0 });
const rows = ref<User[]>([]);
const groups = ref<Group[]>([]);
const data = ref<User[]>([]);
const current = ref<User>();
const form = reactive<UserRequest>({
  name: '',
  email: '',
  username: '',
  password: '',
  password_confirm: '',
  locked: false
});

try {
  groups.value = await $group.list();
  data.value = await $user.list();
  rows.value = [...data.value];
} catch (e: any) {
  error.value = e;
}

function onFilter() {
  if (!rows.value) return;
  if (!filter.value) {
    rows.value = [...data.value];
  }
  rows.value = data.value.filter((row) => {
    return row[filterBy.value].toLowerCase().includes(filter.value.toLowerCase());
  });
}

function onCreate() {
  showForm.value = true;
  error.value = { message: '', status: 0, timestamp: 0 };
  current.value = undefined;
  form.name = '';
  form.group_id = undefined;
  form.job_title = undefined;
  form.phone = undefined;
  form.email = '';
  form.username = '';
  form.password = '';
  form.password_confirm = '';
  form.locked = false;
}

function onEdit(row: User) {
  showForm.value = true;
  error.value = { message: '', status: 0, timestamp: 0 };
  current.value = row;
  form.name = row.name;
  form.group_id = row.group.id;
  form.job_title = row.job_title;
  form.phone = row.phone;
  form.email = row.email;
  form.username = row.username;
  form.password = null;
  form.password_confirm = null;
  form.locked = row.locked;
}

async function save() {
  error.value = { message: '', status: 0, timestamp: 0 };
  if (current.value) {
    if (!form.name || !form.email || !form.username) {
      error.value = { message: 'fields: name, email and username are required', status: 0, timestamp: 0 };
      return;
    }
  } else {
    if (!form.name || !form.email || !form.username || !form.password || !form.password_confirm) {
      error.value = { message: 'fields: name, email, username and password are required', status: 0, timestamp: 0 };
      return;
    }
  }
  if (form.password !== form.password_confirm) {
    error.value = { message: 'password mismatch', status: 0, timestamp: 0 };
    return;
  }
  if (form.group_id === undefined) {
    error.value = { message: 'group is required', status: 0, timestamp: 0 };
    return;
  }
  pending.value = true;
  try {
    if (current.value) {
      const response = await $user.update(current.value.id, form);
      data.value = data.value.map((row) => (row.id === response.id ? response : row));
      rows.value = [...data.value];
    } else {
      const response = await $user.create(form);
      data.value = [...data.value, response];
      rows.value = [...data.value];
    }
    showForm.value = false;
  } catch (e: any) {
    error.value = e;
  }
  pending.value = false;
}

async function onDelete(row: User) {}
</script>
<template>
  <section id="users">
    <div class="header">
      <h1>Usuários</h1>
      <div class="actions">
        <div class="input-box">
          <label for="filter">Filtro:</label>
          <input type="text" id="filter" class="with-icon" v-model="filter" @input="onFilter" />
          <button type="button" class="icon" @click="showFilter = !showFilter"><Icon name="line-md:filter" /></button>
          <ul class="dropdown" :class="{ active: showFilter }">
            <li class="radio" v-for="f in filters" :key="f">
              <input type="radio" name="filter" :id="f" :value="f" v-model="filterBy" />
              <label :for="f">filtrar por {{ $t(f).toLowerCase() }}</label>
            </li>
          </ul>
        </div>
        <button type="button" class="btn-icon" title="novo grupo" @click="onCreate">
          <Icon name="line-md:document-add" />
        </button>
      </div>
    </div>
    <div class="table">
      <table>
        <thead>
          <tr>
            <th>Nome</th>
            <th>Cargo</th>
            <th>Telefone</th>
            <th>Email</th>
            <th>Usuário</th>
            <th>Data de registro</th>
            <th>Ultima alteração</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="row in rows" :key="row.id">
            <td>{{ row.name }}</td>
            <td>{{ row.job_title }}</td>
            <td>{{ row.phone }}</td>
            <td>{{ row.email }}</td>
            <td>{{ row.username }}</td>
            <td>{{ $dateFormat(row.created_at) }}</td>
            <td>{{ $dateFormat(row.updated_at) }}</td>
            <td>
              <div class="actions" v-if="row.editable">
                <button type="button" class="btn-icon" title="editar" @click="onEdit(row)">
                  <Icon name="line-md:edit" />
                </button>
                <button type="button" class="btn-icon" title="excluir" @click="onDelete(row)">
                  <Icon name="line-md:document-delete" />
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <form @submit.prevent="save" v-if="showForm">
      <div class="input-box">
        <label for="name">Nome:</label>
        <input type="text" name="name" id="name" v-model="form.name" />
      </div>
      <div class="form-line">
        <div class="input-box">
          <label for="job_title">Cargo:</label>
          <input type="text" name="job_title" id="job_title" v-model="form.job_title" />
        </div>
        <div class="input-box">
          <label for="phone">Telefone:</label>
          <input type="tel" name="phone" id="phone" v-model="form.phone" />
        </div>
      </div>
      <div class="input-box">
        <label for="email">Email:</label>
        <input type="email" name="email" id="email" v-model="form.email" />
      </div>
      <div class="input-box">
        <label for="username">Usuário:</label>
        <input type="text" name="username" id="username" v-model="form.username" />
      </div>
      <div class="form-line">
        <div class="input-box">
          <label for="password">Senha:</label>
          <input type="password" name="password" id="password" v-model="form.password" />
        </div>
        <div class="input-box">
          <label for="password_confirm">Confirmar senha:</label>
          <input type="password" name="password_confirm" id="password_confirm" v-model="form.password_confirm" />
        </div>
      </div>
      <div class="input-group">
        <p>Grupo:</p>
        <div class="boxes">
          <div v-for="g in groups" class="box" :key="g.name">
            <input type="radio" name="group_id" :id="g.name" :value="g.id" v-model="form.group_id" />
            <label :for="g.name">{{ g.name }}</label>
          </div>
        </div>
      </div>
      <div class="form-message error" v-if="error.message">{{ $t(error.message) }}</div>
      <div class="form-message error" v-if="error.errors">
        <div class="error" v-for="e in error.errors" :key="e.field">
          Erro no campo: <strong>{{ $t(e.field) }}</strong> {{ e.error }}
        </div>
      </div>
      <div class="form-button">
        <button type="submit">
          <span>Salvar</span>
          <Icon name="line-md:loading-twotone-loop" v-if="pending" />
          <Icon name="ic:save" v-else />
        </button>
      </div>
    </form>
  </section>
</template>
<style scoped>
#users {
  padding: 1rem 0.5rem;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.8rem;
  gap: 0.5rem;
}

.header .actions {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.header .input-box {
  width: 100%;
  max-width: 320px;
  position: relative;
}

#users form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-top: 1rem;
  width: 100%;
  max-width: 780px;
  margin-inline: auto;
}

form .input-box label {
  min-width: 75px;
  text-align: right;
}

form #username,
form #email {
  text-transform: lowercase;
}
</style>
