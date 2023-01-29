import * as React from "react";
import {LoginScreen} from "../features/authentication/LoginScreen";
import {SignupScreen} from "../features/authentication/SignupScreen";
import {createNativeStackNavigator} from "@react-navigation/native-stack";
import {AuthenticationStackParamList} from "./types";

const Stack = createNativeStackNavigator<AuthenticationStackParamList>();

export function AuthenticationStack() {
  return <Stack.Navigator initialRouteName="Login">
    <Stack.Screen name="Login" component={LoginScreen} options={{title: "Log in"}}/>
    <Stack.Screen name="Signup" component={SignupScreen} options={{title: "Create your account"}}/>
  </Stack.Navigator>
}