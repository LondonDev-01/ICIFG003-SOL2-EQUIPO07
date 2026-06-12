export interface AuthUser {
  rut: string;
  nombre: string;
}

export interface AuthResponse {
  token: string;
  rut: string;
  nombre: string;
  expiresIn: number;
}

export interface LoginRequest {
  rut: string;
  password: string;
}

export interface RegisterRequest {
  rut: string;
  nombre: string;
  apellido: string;
  correo: string;
  telefono?: string;
  idCarrera: number;
  password: string;
}