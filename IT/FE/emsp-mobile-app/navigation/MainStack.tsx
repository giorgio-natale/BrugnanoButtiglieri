import * as React from "react";
import {createMaterialBottomTabNavigator} from "@react-navigation/material-bottom-tabs";
import MaterialCommunityIcons from "react-native-vector-icons/MaterialCommunityIcons";
import {MainStackParamList} from "./types";
import {StationsStack} from "../features/findStation/StationsStack";
import {BookingsScreen} from "../features/manageBookings/BookingsScreen";
import {SettingsStack} from "../features/settings/SettingsStack";
import {BookingsStack} from "../features/manageBookings/BookingsStack";

const Tab = createMaterialBottomTabNavigator<MainStackParamList>();

export function MainStack() {
  return <Tab.Navigator initialRouteName="Stations" screenOptions={{}}>
    <Tab.Screen name="Settings" component={SettingsStack} options={{
      title: "Profile",
      tabBarIcon: ({color}) => (<MaterialCommunityIcons name="account" color={color} size={26}/>)
    }}
    />
    <Tab.Screen name="Stations" component={StationsStack} options={{
      title: "Find a station",
      tabBarIcon: ({color}) => (<MaterialCommunityIcons name="map-marker" color={color} size={26}/>)
    }}/>
    <Tab.Screen name="Bookings" component={BookingsStack} options={{
      title: "Bookings",
      tabBarIcon: ({color}) => (<MaterialCommunityIcons name="format-list-bulleted-square" color={color} size={26}/>)
    }}/>
  </Tab.Navigator>
}