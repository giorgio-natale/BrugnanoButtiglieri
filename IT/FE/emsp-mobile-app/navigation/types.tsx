import {NativeStackScreenProps} from "@react-navigation/native-stack";
import {CompositeScreenProps, NavigatorScreenParams} from "@react-navigation/native";
import {MaterialBottomTabScreenProps} from "@react-navigation/material-bottom-tabs";
import {MaterialTopTabScreenProps} from "@react-navigation/material-top-tabs";

export type AuthenticationStackParamList = {
  Login: undefined;
  Signup: undefined;
};

export type AuthenticationStackScreenProps<T extends keyof AuthenticationStackParamList> =
  NativeStackScreenProps<AuthenticationStackParamList, T>;

export type MainStackParamList = {
  Settings: undefined;
  Stations: NavigatorScreenParams<StationsStackParamList>;
  Bookings: undefined;
};

export type MainStackScreenProps<T extends keyof MainStackParamList> =
  MaterialBottomTabScreenProps<MainStackParamList, T>;

export type StationsStackParamList = {
  FindStation: undefined;
  BookCharge: NavigatorScreenParams<BookChargeTabParamList>;
};

export type StationsStackScreenProps<T extends keyof StationsStackParamList> =
  NativeStackScreenProps<StationsStackParamList, T>;

export type BookChargeTabParamList = {
  BookInAdvance: undefined;
  BookOnTheFly: undefined;
};

export type BookChargeTabScreenProps<T extends keyof BookChargeTabParamList> = CompositeScreenProps<
  MaterialTopTabScreenProps<BookChargeTabParamList, T>,
  StationsStackScreenProps<keyof StationsStackParamList>
>;

declare global {
  namespace ReactNavigation {
    interface RootParamList extends AuthenticationStackParamList, MainStackParamList {
    }
  }
}