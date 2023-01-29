import * as React from "react";
import {createMaterialBottomTabNavigator} from "@react-navigation/material-bottom-tabs";
import MaterialCommunityIcons from "react-native-vector-icons/MaterialCommunityIcons";
import {MainStackParamList} from "./types";
import {StationsScreen} from "../features/findStation/StationsScreen";
import {SettingsScreen} from "../features/settings/SettingsScreen";
import {BookingsScreen} from "../features/manageBookings/BookingsScreen";

const Tab = createMaterialBottomTabNavigator<MainStackParamList>();

export function MainStack() {
  return <Tab.Navigator initialRouteName="Stations" screenOptions={{}}>
    <Tab.Screen name="Settings" component={SettingsScreen} options={{
      title: "Settings",
      tabBarIcon: ({color}) => (<MaterialCommunityIcons name="account" color={color} size={26}/>)
    }}
    />
    <Tab.Screen name="Stations" component={StationsScreen} options={{
      title: "Find stations",
      tabBarIcon: ({color}) => (<MaterialCommunityIcons name="map-marker" color={color} size={26}/>)
    }}/>
    <Tab.Screen name="Bookings" component={BookingsScreen} options={{
      title: "Your bookings",
      tabBarIcon: ({color}) => (<MaterialCommunityIcons name="format-list-bulleted-square" color={color} size={26}/>)
    }}/>
  </Tab.Navigator>
}