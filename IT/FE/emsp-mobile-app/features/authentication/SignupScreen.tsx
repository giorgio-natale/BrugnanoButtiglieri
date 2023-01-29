import * as React from 'react';
import {View, StyleSheet} from "react-native";
import {AuthenticationStackScreenProps} from "../../navigation/types";
import {TextInput, Text as TextPaper, Button} from "react-native-paper";

export function SignupScreen(props: AuthenticationStackScreenProps<"Signup">) {
  return <View>
    <TextPaper
      variant="titleMedium"
      style={styles.title}
    >
      Personal information
    </TextPaper>
    <TextInput
      label="Name"
      mode="outlined"
      value="Mary"
      style={styles.textInput}
    />
    <TextInput
      label="Surname"
      mode="outlined"
      value="Jane"
      style={styles.textInput}
    />
    <TextInput
      label="Email address"
      mode="outlined"
      value="mary.jane@gmail.com"
      style={styles.textInput}
    />
    <TextInput
      label="Password"
      mode="outlined"
      value="xxxxxxxxxxx"
      secureTextEntry={true}
      style={styles.textInput}
    />
    <TextInput
      label="Confirm password"
      mode="outlined"
      value="xxxxxxxxxxx"
      secureTextEntry={true}
      style={styles.textInput}
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
    />
    <TextInput
      label="Billing name and surname"
      mode="outlined"
      value="Mary Jane"
      style={styles.textInput}
    />
    <TextInput
      label="CVV/CVC"
      mode="outlined"
      value="123"
      style={styles.textInput}
    />
    <Button
      mode="contained"
      style={styles.button}
    >
      Sign up
    </Button>
  </View>
}

export const styles = StyleSheet.create({
  title: {
    margin: 10,
    marginTop: 10
  },
  textInput: {
    margin: 10,
    marginBottom: 7,
    marginTop: 0,
    height: 42
  },
  button: {
    margin: 10
  }
});