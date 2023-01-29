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
import {QueryClient, QueryClientProvider} from "@tanstack/react-query";
import {configApiDefault} from "./api/ApiConfig";

const {LightTheme, DarkTheme} = adaptNavigationTheme({
  reactNavigationLight: NavigationDefaultTheme,
  reactNavigationDark: NavigationDarkTheme,
});

const CombinedLightTheme = merge(MD3LightTheme, LightTheme);
const CombinedDarkTheme = merge(MD3DarkTheme, DarkTheme);

const queryClient = new QueryClient();
configApiDefault();

export default function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <Provider theme={CombinedLightTheme}>
        <NavigationContainer theme={CombinedLightTheme}>
          {/*<AuthenticationStack/>*/}
          <MainStack/>
        </NavigationContainer>
      </Provider>
    </QueryClientProvider>
  );
}