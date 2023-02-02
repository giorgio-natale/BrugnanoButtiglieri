import React, {useCallback, useEffect, useState} from "react";
import * as SecureStore from 'expo-secure-store';
import {CustomerApi} from "../generated";
import {AuthInfoContext, UserAuthContext} from "./UserAuthenticationUtils";
import {Platform} from "react-native";
import {setJwtTokenForApiRequest} from "../api/ApiConfig";

export const AuthenticationManager: React.FC<{ children: JSX.Element }> = ({children}) => {

  const [isLoaded, setIsLoaded] = useState<boolean>(false);
  const [authInfo, setAuthInfo] = useState<AuthInfoContext>({isAuthenticated: false});

  const setAuthInfoCallback = useCallback((jwtToken: string, customerId: number, emailAddress: string) => {
    setAuthInfo((authInfo) => ({
      ...authInfo,
      isAuthenticated: true,
      customerId: customerId,
      emailAddress: emailAddress,
      jwtToken: jwtToken
    }));
  }, []);

  const refreshAuth = useCallback(() => {
    if(Platform.OS !== "web")
      return Promise.all([
        SecureStore.getItemAsync("emailAddress"),
        SecureStore.getItemAsync("password")
      ]).then(([emailAddress, password]) => {
        if (emailAddress && password) {
          return CustomerApi.login({emailAddress: emailAddress, password: password})
            .then(r => setAuthInfoCallback(r.token, r.customerId, r.emailAddress))
        } else {
          setAuthInfo((authInfo) => ({...authInfo, isAuthenticated: false}))
        }
      }).finally(() => setIsLoaded(true))
  }, []);

  useEffect(() => {
    refreshAuth();
  }, []);

  useEffect(() => {
    setAuthInfo((authInfo) => ({...authInfo, refreshAuth: refreshAuth}))
  }, [refreshAuth]);

  useEffect(() => {
    setAuthInfo((authInfo) => ({...authInfo, setAuthInfo: setAuthInfoCallback}))
  }, [setAuthInfoCallback]);

  useEffect(() => {
    if(authInfo.isAuthenticated)
      setJwtTokenForApiRequest(authInfo.jwtToken)
  }, [authInfo])

  return (isLoaded ?
      <UserAuthContext.Provider value={authInfo}>
        {children}
      </UserAuthContext.Provider> :
      <></>
  );
}