import {createContext, useCallback, useContext} from "react";
import * as SecureStore from "expo-secure-store";
import {CustomerApi} from "../generated";
import {Platform} from "react-native";

export interface AuthInfoContext {
  isAuthenticated: boolean,
  jwtToken?: string,
  customerId?: number,
  emailAddress?: string,
  refreshAuth?: () => void,
  setAuthInfo?: (jwtToken: string, customerId: number, emailAddress: string) => void
}

export const UserAuthContext = createContext<AuthInfoContext | null>(null);

export function useIsUserAuthenticated() {
  const authInfo = useContext(UserAuthContext);
  return authInfo.isAuthenticated;
}

export function useGetAuthInfo() {
  const {jwtToken, customerId, emailAddress} = useContext(UserAuthContext);
  return {jwtToken, customerId, emailAddress};
}

export function useLogin() {
  const authInfo = useContext(UserAuthContext);

  return useCallback((emailAddress, password) =>
      CustomerApi.login({emailAddress: emailAddress, password: password})
        .then(loginInfo => {
          if (Platform.OS !== "web")
            return Promise.all([
              SecureStore.setItemAsync("emailAddress", emailAddress),
              SecureStore.setItemAsync("password", password)
            ]).then(r => loginInfo)
          else
            return loginInfo
        }).then(login => authInfo.setAuthInfo(login.token, login.customerId, login.emailAddress)),
    [authInfo]
  );
}

export function useLogout() {
  const authInfo = useContext(UserAuthContext);

  return useCallback(() => {
      if (Platform.OS !== "web")
        return Promise.all([
          SecureStore.deleteItemAsync("emailAddress"),
          SecureStore.deleteItemAsync("password")
        ]).then(r => authInfo.refreshAuth())
    }, [authInfo]
  );
}