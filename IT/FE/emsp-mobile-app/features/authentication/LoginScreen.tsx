import * as React from 'react';
import {View} from "react-native";
import {Button, TextInput} from "react-native-paper";
import {AuthenticationStackScreenProps} from "../../navigation/types";
import {styles} from "./SignupScreen";

export function LoginScreen({navigation}: AuthenticationStackScreenProps<"Login">) {
  return <View style={{justifyContent: "center", flex: 1}}>
    <TextInput
      label="Email address"
      mode="outlined"
      value="mary.jane@gmail.com"
      style={{...styles.textInput, ...{marginTop: 10}}}
    />
    <TextInput
      label="Password"
      mode="outlined"
      value="xxxxxxxxxxx"
      secureTextEntry={true}
      style={{...styles.textInput, ...{marginBottom: 0}}}
    />
    <Button>
      Forgot password?
    </Button>
    <Button
      mode="contained"
      style={styles.button}
    >
      Login
    </Button>
    <Button
      onPress={() => navigation.navigate("Signup")}
      style={{marginTop: 30}}
    >
      Sign up
    </Button>
  </View>
}