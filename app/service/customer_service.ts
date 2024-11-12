import type { $Fetch } from 'ofetch';

export default class CustomerService {
  http: $Fetch;
  constructor(http: $Fetch) {
    this.http = http;
  }

  async list() {
    return await this.http<Customer[]>('/customer');
  }

  async create(body: CustomerRequest) {
    return await this.http<Customer>('/customer', { method: 'POST', body });
  }

  async update(id: string, body: CustomerRequest) {
    return await this.http<Customer>(`/customer/${id}`, { method: 'PUT', body });
  }

  async delete(id: string) {
    return await this.http<Customer>(`/customer/${id}`, { method: 'DELETE' });
  }

  async restore(id: string) {
    return await this.http<Customer>(`/customer/${id}/restore`, { method: 'PUT' });
  }
}
