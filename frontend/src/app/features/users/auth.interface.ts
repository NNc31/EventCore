export interface SignupRequest {
  username: string;
  email: string;
  password: string;
}

export interface SigninRequest {
  username: string;
  password: string;
}

export interface JwtResponse {
  token: string;
}

export interface User {
  username: string;
  email: string;
  createdAt: string;
}
