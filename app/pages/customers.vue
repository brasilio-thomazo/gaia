<script setup lang="ts">
definePageMeta({ middleware: 'auth' });
const { $api, $customer, $dateFormat } = useNuxtApp();
type Filter = 'name' | 'document';
const filters = ['name', 'document'] as Filter[];
const filterBy = ref<Filter>(filters[0]);
const filter = ref('');
const showFilter = ref(false);
const showForm = ref(false);
const pending = ref(false);
const error = ref<ResponseError>({ message: '', status: 0, timestamp: 0 });
const rows = ref<Customer[]>([]);
const data = ref<Customer[]>([]);
const current = ref<Customer>();
const form = reactive<CustomerRequest>({
  name: '',
  phone: '',
  email: '',
  document: '',
  address: '',
  active: true,
  contacts: [{ name: '', phone: '', email: '', job_title: '' }]
});

try {
  data.value = await $customer.list();
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
  form.phone = '';
  form.email = '';
  form.document = '';
  form.address = '';
  form.active = true;
  form.contacts = [{ name: '', phone: '', email: '', job_title: '' }];
}

function onEdit(row: Customer) {
  showForm.value = true;
  error.value = { message: '', status: 0, timestamp: 0 };
  current.value = row;
  form.name = row.name;
  form.phone = row.phone;
  form.email = row.email;
  form.document = row.document;
  form.address = row.address;
  form.active = row.active;
  form.contacts = row.contacts;
}

function addContact() {
  form.contacts.push({ name: '', phone: '', email: '', job_title: '' });
}

function removeContact(index: number) {
  form.contacts.splice(index, 1);
}

async function findCNPJ() {
  try {
    var cnpj = form.document.replace(/[^\d]+/g, '');
    const response = await $api.cnpj(cnpj);
    form.name = response.razao_social;
    form.email = response.email;
    form.phone = response.ddd_telefone_1;
    form.address = `${response.logradouro}, ${response.numero}, ${response.bairro}, ${response.municipio} - ${response.uf}`;
  } catch (e: any) {
    error.value = e;
  }
}

async function save() {
  error.value = { message: '', status: 0, timestamp: 0 };
  if (!form.name || !form.email || !form.phone || !form.document) {
    error.value = { message: 'fields: name, phone, email and document are required', status: 0, timestamp: 0 };
    return;
  }
  if (form.contacts === undefined) {
    error.value = { message: 'sponsor is required', status: 0, timestamp: 0 };
    return;
  }
  pending.value = true;
  try {
    if (current.value) {
      const response = await $customer.update(current.value.id, form);
      data.value = data.value.map((row) => (row.id === response.id ? response : row));
      rows.value = [...data.value];
    } else {
      const response = await $customer.create(form);
      data.value = [...data.value, response];
      rows.value = [...data.value];
    }
    showForm.value = false;
  } catch (e: any) {
    error.value = e;
  }
  pending.value = false;
}

async function onDelete(row: Customer) {}
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
              <label :for="f">{{ $t(`filter by ${f}`).toLowerCase() }}</label>
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
            <th>Telefone</th>
            <th>Email</th>
            <th>CPF/CNPJ</th>
            <th>Data de registro</th>
            <th>Ultima alteração</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="row in rows" :key="row.id">
            <td>{{ row.name }}</td>
            <td>{{ row.phone }}</td>
            <td>{{ row.email }}</td>
            <td>{{ row.document }}</td>
            <td>{{ $dateFormat(row.created_at) }}</td>
            <td>{{ $dateFormat(row.updated_at) }}</td>
            <td>
              <div class="actions">
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
        <label for="name">Razão social:</label>
        <input type="text" name="name" id="name" v-model="form.name" />
      </div>
      <div class="form-line">
        <div class="input-box">
          <label for="document">CPF/CNPJ:</label>
          <input type="text" name="document" id="document" v-model="form.document" />
          <button type="button" class="icon" @click="findCNPJ"><Icon name="line-md:search" /></button>
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
        <label for="address">Endereço:</label>
        <input type="text" name="address" id="address" v-model="form.address" />
      </div>
      <div class="form-actions">
        <span class="label">Contatos</span>
        <div class="actions">
          <button type="button" class="btn-icon" title="novo contato" @click="addContact">
            <Icon name="ic:baseline-add-circle-outline" />
          </button>
        </div>
      </div>
      <fieldset v-for="(_, i) in form.contacts" :key="i">
        <legend>
          #{{ i + 1 }}
          <button
            type="button"
            class="btn-icon"
            v-if="form.contacts.length > 1"
            title="excluir contato"
            @click="removeContact(i)"
          >
            <Icon name="ic:baseline-remove-circle-outline" />
          </button>
        </legend>
        <div class="form-line">
          <div class="input-box">
            <label :for="`name_${i}`">Nome:</label>
            <input type="text" :id="`name_${i}`" v-model="form.contacts[i].name" />
          </div>
          <div class="input-box">
            <label :for="`phone_${i}`">Telefone:</label>
            <input type="tel" :id="`phone_${i}`" v-model="form.contacts[i].phone" />
          </div>
        </div>
        <div class="form-line">
          <div class="input-box">
            <label :for="`email_${i}`">Email:</label>
            <input type="email" :id="`email_${i}`" v-model="form.contacts[i].email" />
          </div>
          <div class="input-box">
            <label :for="`job_title_${i}`">Cargo:</label>
            <input type="tel" :id="`job_title_${i}`" v-model="form.contacts[i].job_title" />
          </div>
        </div>
      </fieldset>
      <div class="form-message error" v-if="error.message">{{ $t(error.message) }}</div>
      <div class="form-message error" v-if="error.errors">
        <div class="error" v-for="e in error.errors" :key="e.field">
          Erro no campo: <strong>{{ $t(e.field) }}</strong
          >, {{ e.error }}
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
.form-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.form-actions .actions {
  display: flex;
  align-items: center;
}

.form-actions .actions button {
  padding: 0 0.2rem;
}

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
  max-width: 780px;
  margin-inline: auto;
}

form .input-box label {
  width: 80px;
  text-align: right;
  font-size: 0.8rem;
}

form #name,
form #address {
  text-transform: uppercase;
}

form #email {
  text-transform: lowercase;
}

fieldset {
  margin-bottom: 10px;
  padding: 10px;
  border: var(--border);
}

fieldset legend {
  padding: 0 10px;
  color: var(--color);
  display: flex;
  gap: 5px;
  align-items: center;
}

fieldset legend .btn-icon {
  padding: 0 2px;
  font-size: 1rem;
}

fieldset .input-box {
  margin-bottom: 10px;
}
</style>
