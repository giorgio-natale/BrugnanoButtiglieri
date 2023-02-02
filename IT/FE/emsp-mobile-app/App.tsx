import * as React from 'react';
import {
  DarkTheme as NavigationDarkTheme,
  DefaultTheme as NavigationDefaultTheme,
  NavigationContainer
} from "@react-navigation/native";
import {adaptNavigationTheme, MD3DarkTheme, MD3LightTheme, Provider} from "react-native-paper";
import merge from 'deepmerge';
import {QueryClient, QueryClientProvider} from "@tanstack/react-query";
import {configApiDefault} from "./api/ApiConfig";
import {NotificationsManager} from "./notifications/NotificationsManager";
import {AuthenticationManager} from "./user-auth/AuthenticationManager";
import {NavigationManager} from "./navigation/NavigationManager";

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
        <AuthenticationManager>
          <NavigationContainer theme={CombinedLightTheme}>
            <NotificationsManager>
              <NavigationManager/>
            </NotificationsManager>
          </NavigationContainer>
        </AuthenticationManager>
      </Provider>
    </QueryClientProvider>
  );
}