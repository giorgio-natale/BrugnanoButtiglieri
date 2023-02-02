import * as React from "react";
import {createNativeStackNavigator} from "@react-navigation/native-stack";
import {BookingsStackParamList, MainStackScreenProps} from "../../navigation/types";
import {BookingsScreen} from "./BookingsScreen";

const Stack = createNativeStackNavigator<BookingsStackParamList>();

export function BookingsStack(props: MainStackScreenProps<"Bookings">) {
  return <Stack.Navigator>
    <Stack.Screen name="BookingsScreen" component={BookingsScreen} options={{title: "Your bookings"}}/>
  </Stack.Navigator>;
}