import * as React from "react";
import {createContext} from "react";
import {createMaterialTopTabNavigator} from '@react-navigation/material-top-tabs';
import {BookInAdvanceScreen} from "./BookInAdvanceScreen";
import {BookOnTheFlyScreen} from "./BookOnTheFlyScreen";
import {BookChargeTabParamList, StationsStackScreenProps} from "../../navigation/types";

const Tab = createMaterialTopTabNavigator<BookChargeTabParamList>();

export const BookChargeContext = createContext<{ chargingStationId: number | null }>({chargingStationId: null});

export function BookChargeScreen(props: StationsStackScreenProps<"BookCharge">) {
  return <BookChargeContext.Provider value={{chargingStationId: props.route.params.chargingStationId}}>
    <Tab.Navigator initialRouteName="BookInAdvance">
      <Tab.Screen name="BookInAdvance" component={BookInAdvanceScreen} options={{title: "Book in advance"}}/>
      <Tab.Screen name="BookOnTheFly" component={BookOnTheFlyScreen} options={{title: "Book on the fly"}}/>
    </Tab.Navigator>
  </BookChargeContext.Provider>
}