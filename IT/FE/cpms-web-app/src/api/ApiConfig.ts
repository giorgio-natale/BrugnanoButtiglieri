import {OpenAPI} from "../generated";

// @ts-ignore
const beUrl = BE_URL; // eslint-disable-line

export function configApiDefault() {
  OpenAPI.BASE = beUrl
  console.log(`BE_URL: ${beUrl}`)
  const token = window.localStorage.getItem("token");
  if (token)
    setJwtTokenForApiRequest(token);
}

export function setJwtTokenForApiRequest(token: string) {
  OpenAPI.TOKEN = token;
}

interface AuthInfo {
  token: string,
  employeeId: number,
  employeeName: string,
  employeeSurname: string,
  employeeEmailAddress: string
}

export function saveAuthInfo({token, employeeId, employeeName, employeeSurname, employeeEmailAddress}: AuthInfo) {
  window.localStorage.setItem("token", token);
  window.localStorage.setItem("employeeId", employeeId.toString());
  window.localStorage.setItem("employeeName", employeeName);
  window.localStorage.setItem("employeeSurname", employeeSurname);
  window.localStorage.setItem("employeeEmailAddress", employeeEmailAddress);

  setJwtTokenForApiRequest(token);
}

export function clearAuthInfo() {
  window.localStorage.clear();
}

export function getAuthInfo(): AuthInfo {
  const token = window.localStorage.getItem("token");
  const employeeId = parseInt(window.localStorage.getItem("employeeId"));
  const employeeName = window.localStorage.getItem("employeeName");
  const employeeSurname = window.localStorage.getItem("employeeSurname");
  const employeeEmailAddress = window.localStorage.getItem("employeeEmailAddress");

  return {
    token,
    employeeId,
    employeeName,
    employeeSurname,
    employeeEmailAddress
  };
}