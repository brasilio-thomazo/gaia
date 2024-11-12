import BrasilAPIService from '~/service/brasil_api';
import { $fetch } from 'ofetch';

export default defineNuxtPlugin(() => {
  const http = $fetch.create({
    baseURL: 'https://brasilapi.com.br/api',
    headers: {
      'Content-Type': 'application/json',
      Accept: 'application/json'
    }
  });

  const api = new BrasilAPIService(http);

  return { provide: { api } };
});
