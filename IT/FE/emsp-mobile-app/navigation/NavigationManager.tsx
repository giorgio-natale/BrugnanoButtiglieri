import {AuthenticationStack} from "./AuthenticationStack";
import {MainStack} from "./MainStack";
import * as React from "react";
import {useIsUserAuthenticated} from "../user-auth/UserAuthenticationUtils";

export function NavigationManager() {
  const isUserAuthenticated = useIsUserAuthenticated();

  if (isUserAuthenticated)
    return <MainStack/>;
  else
    return <AuthenticationStack/>;
}