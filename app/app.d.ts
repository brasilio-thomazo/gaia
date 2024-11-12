declare global {
  interface ResponseError {
    message: string;
    status: number;
    timestamp: number;
    errors?: { field: string; error: string }[];
  }
  interface Group {
    id: number;
    name: string;
    permissions: string[];
    visible: boolean;
    editable: boolean;
    locked: boolean;
    created_at: number;
    updated_at: number;
    deleted_at: number;
  }

  interface GroupRequest {
    name: string;
    permissions: string[];
    visible: boolean;
    editable: boolean;
    locked: boolean;
  }

  interface User {
    id: string;
    name: string;
    job_title: string;
    phone: string;
    email: string;
    username: string;
    group_id: number;
    group: Group;
    visible: boolean;
    editable: boolean;
    locked: boolean;
    created_at: number;
    updated_at: number;
    deleted_at: number;
  }

  interface UserRequest {
    name: string;
    job_title?: string;
    phone?: string;
    email: string;
    locked: boolean;
    username: string;
    password: string | null;
    password_confirm: string | null;
    group_id?: number;
  }

  interface LoginResponse extends User {
    token: string;
  }

  interface CustomerRequest {
    name: string;
    phone: string;
    email: string;
    document: string;
    address: string;
    active: boolean;
    contacts: {
      name: string;
      phone: string;
      email: string;
      job_title: string;
    }[];
  }

  interface Customer extends CustomerRequest {
    id: string;
    created_at: number;
    updated_at: number;
    deleted_at: number;
  }

  interface CNPJResponse {
    cnpj: string;
    razao_social: string;
    logradouro: string;
    bairro: string;
    numero: string;
    municipio: string;
    uf: string;
    cep: string;
    complemento: string;
    ddd_telefone_1: string;
    email: string;
  }
}

export {};
