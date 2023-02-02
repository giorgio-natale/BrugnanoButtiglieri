import {OpenAPI} from "../generated";

export function configApiDefault() {
  OpenAPI.BASE = "https://api.papaia.smg-team.net";
}

export function setJwtTokenForApiRequest(token: string) {
  OpenAPI.TOKEN = token;
}