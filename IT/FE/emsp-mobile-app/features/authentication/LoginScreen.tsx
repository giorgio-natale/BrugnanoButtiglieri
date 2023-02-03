import * as React from 'react';
import {View} from "react-native";
import {Button, Text, TextInput} from "react-native-paper";
import {AuthenticationStackScreenProps} from "../../navigation/types";
import {styles} from "./SignupScreen";
import {LoginRequest} from "../../generated";
import {Formik} from "formik";
import {useLogin} from "../../user-auth/UserAuthenticationUtils";
import {useMutation} from "@tanstack/react-query";

export function LoginScreen({navigation}: AuthenticationStackScreenProps<"Login">) {

  const login = useLogin();
  const loginMutation = useMutation(({emailAddress, password}: LoginRequest) => login(emailAddress, password));

  return (
    <Formik<LoginRequest>
      initialValues={{emailAddress: null, password: null}}
      onSubmit={values =>
        loginMutation.mutate({
          emailAddress: values.emailAddress,
          password: values.password
        })}
    >
      {({handleChange, handleBlur, handleSubmit, values}) => (
        <View style={{justifyContent: "center", flex: 1, padding: 10}}>
          <TextInput
            label="Email address"
            mode="outlined"
            value={values.emailAddress}
            onChangeText={handleChange("emailAddress")}
            style={{...styles.textInput, ...{marginTop: 10}}}
          />
          <TextInput
            label="Password"
            mode="outlined"
            value={values.password}
            onChangeText={handleChange("password")}
            secureTextEntry={true}
            style={{...styles.textInput, ...{marginBottom: 0}}}
          />
          <View style={{alignItems: "flex-start", justifyContent: "center", height: 36, paddingLeft: 10, paddingRight: 10}}>
            {loginMutation.isError &&
              <Text style={{color: "#F00"}}>
                Login failed, please retry
              </Text>
            }
          </View>
          <Button
            mode="contained"
            style={styles.button}
            onPress={() => handleSubmit()}
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
      )}
    </Formik>
  );
}