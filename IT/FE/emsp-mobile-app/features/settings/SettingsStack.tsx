import * as React from "react";
import {createNativeStackNavigator} from "@react-navigation/native-stack";
import {MainStackScreenProps, SettingsStackParamList} from "../../navigation/types";
import {SettingsScreen} from "./SettingsScreen";

const Stack = createNativeStackNavigator<SettingsStackParamList>();

export function SettingsStack(props: MainStackScreenProps<"Settings">) {
  return <Stack.Navigator>
    <Stack.Screen name="SettingsScreen" component={SettingsScreen} options={{title: "Your profile"}}/>
  </Stack.Navigator>;
}