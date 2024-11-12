import { $fetch as _fetch } from 'ofetch';
import AuthService from '~/service/auth_service';
import CustomerService from '~/service/customer_service';
import GroupService from '~/service/group_service';
import UserService from '~/service/user_service';

export default defineNuxtPlugin(() => {
  const http = _fetch.create({
    baseURL: 'http://localhost:8080',
    onResponseError: ({ response }) => {
      let errors = [] as { field: string; error: string }[];
      if (response._data.errors) {
        const arr = response._data.errors as string[];
        errors = arr.map((e) => {
          return {
            field: e.split(',')[0],
            error: e.split(',')[1]
          };
        });
      }
      throw {
        message: response._data.message,
        status: response.status,
        timestamp: response._data.timestamp,
        errors: errors.length > 0 ? errors : undefined
      };
    },
    onRequest: ({ options, request }) => {
      const token = useCookie('token').value;
      if (token && request !== '/auth/login') {
        options.headers.append('Authorization', `Bearer ${token}`);
      }
    }
  });
  const auth = new AuthService(http);
  const group = new GroupService(http);
  const user = new UserService(http);
  const customer = new CustomerService(http);
  return { provide: { auth, group, user, customer, http } };
});
