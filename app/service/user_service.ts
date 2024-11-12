import type { $Fetch } from 'ofetch';

export default class UserService {
  http: $Fetch;
  constructor(http: $Fetch) {
    this.http = http;
  }

  async list() {
    return await this.http<User[]>('/user');
  }

  async create(body: UserRequest) {
    return await this.http<User>('/user', { method: 'POST', body });
  }

  async update(id: string, body: UserRequest) {
    return await this.http<User>(`/user/${id}`, { method: 'PUT', body });
  }
}
