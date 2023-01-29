import * as React from "react";
import {createMaterialTopTabNavigator} from '@react-navigation/material-top-tabs';
import {BookInAdvanceScreen} from "./BookInAdvanceScreen";
import {BookOnTheFlyScreen} from "./BookOnTheFlyScreen";
import {BookChargeTabParamList, StationsStackScreenProps} from "../../navigation/types";

const Tab = createMaterialTopTabNavigator<BookChargeTabParamList>();

export function BookChargeScreen(props: StationsStackScreenProps<"BookCharge">) {
  return <Tab.Navigator initialRouteName="BookInAdvance">
    <Tab.Screen name="BookInAdvance" component={BookInAdvanceScreen} options={{title: "Book in advance"}}/>
    <Tab.Screen name="BookOnTheFly" component={BookOnTheFlyScreen} options={{title: "Book on the fly"}}/>
  </Tab.Navigator>
}