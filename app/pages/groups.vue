<script setup lang="ts">
definePageMeta({ middleware: 'auth' });
const { $group, $dateFormat } = useNuxtApp();
type Filter = 'name';
const filters = ['name'] as Filter[];
const filterBy = ref<Filter>(filters[0]);
const filter = ref('');
const showFilter = ref(false);
const showForm = ref(false);
const pending = ref(false);
const error = ref({ message: '', status: 0, timestamp: 0 });
const rows = ref<Group[]>([]);
const data = ref<Group[]>([]);
const form = reactive<GroupRequest>({ name: '', permissions: [], visible: true, editable: true, locked: false });
const current = ref<Group>();
const permissions = [
  'group:list',
  'group:create',
  'group:update',
  'group:delete',
  'user:list',
  'user:create',
  'user:update',
  'user:delete',
  'customer:list',
  'customer:create',
  'customer:update',
  'customer:delete',
  'app:list',
  'app:create',
  'app:update',
  'app:delete'
];

try {
  data.value = await $group.list();
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
  form.permissions = [];
  form.visible = true;
  form.editable = true;
  form.locked = false;
}

function onEdit(group: Group) {
  showForm.value = true;
  error.value = { message: '', status: 0, timestamp: 0 };
  current.value = group;
  form.name = group.name;
  form.permissions = group.permissions || [];
  form.visible = group.visible;
  form.editable = group.editable;
  form.locked = group.locked;
}

async function save() {
  pending.value = true;
  error.value = { message: '', status: 0, timestamp: 0 };
  try {
    if (current.value) {
      const response = await $group.update(current.value.id, form);
      data.value = data.value.map((row) => (row.id === response.id ? response : row));
      rows.value = [...data.value];
    } else {
      const response = await $group.create(form);
      data.value = [...data.value, response];
      rows.value = [...data.value];
    }
    showForm.value = false;
  } catch (e: any) {
    error.value = e;
  }
  pending.value = false;
}

async function onDelete(group: Group) {}
</script>
<template>
  <section id="groups">
    <div class="header">
      <h1>Grupos</h1>
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
            <th>Data de registro</th>
            <th>Ultima alteração</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="row in rows" :key="row.id">
            <td>{{ row.name }}</td>
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
      <div class="input-group">
        <p>Permissões:</p>
        <div class="boxes">
          <div v-for="p in permissions" class="box" :key="p">
            <input type="checkbox" name="permissions[]" :id="p" :value="p" v-model="form.permissions" />
            <label :for="p">{{ $t(p) }}</label>
          </div>
        </div>
      </div>
      <div class="form-message error" v-if="error.message">{{ $t(error.message) }}</div>
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
#groups {
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

#groups form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-top: 1rem;
  width: 100%;
  max-width: 600px;
  margin-inline: auto;
}

form #name {
  text-transform: lowercase;
}

.box {
  min-width: calc(100% / 4 - 0.5rem);
}
</style>
