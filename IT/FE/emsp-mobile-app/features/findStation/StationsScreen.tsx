import * as React from "react";
import {createNativeStackNavigator} from "@react-navigation/native-stack";
import {BookChargeScreen} from "../bookCharge/BookChargeScreen";
import {FindStationScreen} from "./FindStationScreen";
import {MainStackScreenProps, StationsStackParamList} from "../../navigation/types";

const Stack = createNativeStackNavigator<StationsStackParamList>();

export function StationsScreen(props: MainStackScreenProps<"Stations">) {
  return <Stack.Navigator initialRouteName="FindStation">
    <Stack.Screen name="FindStation" component={FindStationScreen} options={{title: "Find a station"}}/>
    <Stack.Screen name="BookCharge" component={BookChargeScreen} options={{title: "Book your charge"}}/>
  </Stack.Navigator>;
}