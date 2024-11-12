import type { $Fetch } from 'ofetch';

export default class BrasilAPIService {
  http: $Fetch;
  constructor(http: $Fetch) {
    this.http = http;
  }

  async cnpj(cnpj: string) {
    return await this.http<CNPJResponse>(`/cnpj/v1/${cnpj}`);
  }

  async cep(cep: string) {
    return await this.http<any>(`/cep/v2/${cep}`);
  }
}
