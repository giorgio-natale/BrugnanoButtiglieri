import * as React from 'react';
import {StyleSheet, View} from "react-native";
import {SettingsStackScreenProps} from "../../navigation/types";
import {Button, List, Text, TextInput} from "react-native-paper";
import MaterialCommunityIcons from "react-native-vector-icons/MaterialCommunityIcons";
import {useGetAuthInfo, useLogout} from "../../user-auth/UserAuthenticationUtils";
import {useQuery} from "@tanstack/react-query";
import {customerQuery} from "./CustomerApi";

export function SettingsScreen(props: SettingsStackScreenProps<"SettingsScreen">) {

  const authInfo = useGetAuthInfo();
  const customerProfileQuery = useQuery(customerQuery(authInfo.customerId));

  const logout = useLogout();

  return <View style={{flex: 1, margin: 10}}>
    <List.Item
      style={{marginTop: 30, marginBottom: 20}}
      title={`Hi ${customerProfileQuery?.data?.name}!`}
      titleStyle={{fontSize: 20, fontWeight: "500"}}
      left={props => <MaterialCommunityIcons {...props} name="account-circle" size={80}/>}
    />
    <TextInput
      key="nameTextInput"
      label="Name"
      style={{marginBottom: 7}}
      mode="outlined"
      value={customerProfileQuery?.data?.name}
      outlineColor={"rgba(0,0,0,0.12)"}
      editable={false}
    />
    <TextInput
      key="surnameTextInput"
      label="Surname"
      style={{marginBottom: 7}}
      mode="outlined"
      value={customerProfileQuery?.data?.surname}
      outlineColor={"rgba(0,0,0,0.12)"}
      editable={false}
    />
    <TextInput
      key="emailAddressTextInput"
      label="Email address"
      mode="outlined"
      value={customerProfileQuery?.data?.emailAddress}
      outlineColor={"rgba(0,0,0,0.12)"}
      editable={false}
    />
      <View style={styles.buttonContainer}>
      <Button
        mode="contained"
        style={styles.button}
        onPress={logout}
      >
        Log out
      </Button>
    </View>
  </View>
}

export const styles = StyleSheet.create({
  buttonContainer: {
    marginTop: "auto",
    marginBottom: 20,
    width: "100%",
    height: 45
  },
  button: {
    height: "100%",
    width: "100%",
    alignItems: "center",
    flexDirection: "row",
    display: "flex",
    justifyContent: "center"
  }
});