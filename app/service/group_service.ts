import type { $Fetch } from 'ofetch';

export default class GroupService {
  http: $Fetch;
  constructor(http: $Fetch) {
    this.http = http;
  }

  async list() {
    return await this.http<Group[]>('/group');
  }

  async create(body: GroupRequest) {
    return await this.http<Group>('/group', { method: 'POST', body });
  }

  async update(id: number, body: GroupRequest) {
    return await this.http<Group>(`/group/${id}`, { method: 'PUT', body });
  }
}
