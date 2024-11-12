import type { $Fetch } from 'ofetch';

export default class AuthService {
  http: $Fetch;

  constructor(http: $Fetch) {
    this.http = http;
  }

  async login(body: { username: string; password: string }) {
    return await this.http<LoginResponse>('/auth/login', { method: 'POST', body });
  }

  async me() {
    return await this.http<User>('/auth/me');
  }

  decode(token: string) {
    const parts = token.split('.');
    return JSON.parse(atob(parts[1]));
  }

  validate(token: string) {
    const decoded = this.decode(token);
    return decoded.exp > Date.now() / 1000;
  }
}
