import * as React from 'react';
import {
  DarkTheme as NavigationDarkTheme,
  DefaultTheme as NavigationDefaultTheme,
  NavigationContainer
} from "@react-navigation/native";
import {adaptNavigationTheme, MD3DarkTheme, MD3LightTheme, Provider} from "react-native-paper";
import {AuthenticationStack} from "./navigation/AuthenticationStack";
import merge from 'deepmerge';
import {MainStack} from "./navigation/MainStack";

const {LightTheme, DarkTheme} = adaptNavigationTheme({
  reactNavigationLight: NavigationDefaultTheme,
  reactNavigationDark: NavigationDarkTheme,
});

const CombinedLightTheme = merge(MD3LightTheme, LightTheme);
const CombinedDarkTheme = merge(MD3DarkTheme, DarkTheme);

export default function App() {
  return (
    <Provider theme={CombinedLightTheme}>
      <NavigationContainer theme={CombinedLightTheme}>
        {/*<AuthenticationStack/>*/}
        <MainStack/>
      </NavigationContainer>
    </Provider>
  );
}