import * as React from 'react';
import {View} from "react-native";
import {MainStackScreenProps} from "../../navigation/types";
import {Button, List, Text as TextPaper, TextInput} from "react-native-paper";
import {styles} from "../authentication/SignupScreen";
import MaterialCommunityIcons from "react-native-vector-icons/MaterialCommunityIcons";
import {useLogout} from "../../user-auth/UserAuthenticationUtils";

export function SettingsScreen(props: MainStackScreenProps<"Settings">) {

  const logout = useLogout();

  return <View style={{flex: 1}}>
    <List.Item
      style={{marginTop: 30, marginBottom: 30}}
      title="Mary Jane"
      description="mary.jane@gmail.com"
      left={props => <MaterialCommunityIcons {...props} name="account-circle" size={80}/>}
    />
    <TextPaper
      variant="titleMedium"
      style={styles.title}
    >
      Payment credentials
    </TextPaper>
    <TextInput
      label="Card number"
      mode="outlined"
      value="1234 1234 1234 1234"
      style={styles.textInput}
      editable={false}
    />
    <TextInput
      label="Billing name and surname"
      mode="outlined"
      value="Mary Jane"
      style={styles.textInput}
      editable={false}
    />
    <Button
      mode="contained-tonal"
      style={styles.button}
    >
      Change payment credentials
    </Button>
    <View style={{marginTop: "auto"}}>
      <Button
        mode="contained-tonal"
        style={{...styles.button, ...{marginBottom: 20}}}
      >
        Change password
      </Button>
      <Button
        mode="contained"
        style={{...styles.button, ...{marginBottom: 20}}}
        onPress={logout}
      >
        Log out
      </Button>
    </View>
  </View>
}