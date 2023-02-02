import * as React from 'react';
import {useState} from 'react';
import {StyleSheet, View} from "react-native";
import {AuthenticationStackScreenProps} from "../../navigation/types";
import {Button, Text as TextPaper, Text, TextInput} from "react-native-paper";
import {Formik} from "formik";
import {CustomerApi, SignupRequest} from "../../generated";
import {useMutation} from "@tanstack/react-query";
import {useLogin} from "../../user-auth/UserAuthenticationUtils";

export function SignupScreen(props: AuthenticationStackScreenProps<"Signup">) {

  const login = useLogin();
  const [isConfirmPasswordNotCorrect, setIsConfirmPasswordNotCorrect] = useState<boolean>(false);

  const signupMutation = useMutation(
    (customer: SignupRequest) => CustomerApi.signup(customer).then(() => customer),
    {onSuccess: (customer) => login(customer.emailAddress, customer.password)}
  );

  return (
    <Formik<SignupRequest & { confirmPassword: string }>
      initialValues={{"emailAddress": "", "password": "", "confirmPassword": "", "name": "", "surname": ""}}
      onSubmit={values => {
        if (values.password === values.confirmPassword) {
          signupMutation.mutate(values);
          setIsConfirmPasswordNotCorrect(false);
        } else {
          setIsConfirmPasswordNotCorrect(true);
        }
      }}
    >
      {({handleChange, handleSubmit, values}) => (
        <View style={{justifyContent: "center", flex: 1}}>
          <TextInput
            label="Name"
            mode="outlined"
            value={values.name}
            onChangeText={handleChange("name")}
            style={styles.textInput}
          />
          <TextInput
            label="Surname"
            mode="outlined"
            value={values.surname}
            onChangeText={handleChange("surname")}
            style={styles.textInput}
          />
          <TextInput
            label="Email address"
            mode="outlined"
            value={values.emailAddress}
            onChangeText={handleChange("emailAddress")}
            style={styles.textInput}
          />
          <TextInput
            label="Password"
            mode="outlined"
            value={values.password}
            onChangeText={handleChange("password")}
            secureTextEntry={true}
            style={styles.textInput}
          />
          <TextInput
            label="Confirm password"
            mode="outlined"
            value={values.confirmPassword}
            onChangeText={handleChange("confirmPassword")}
            secureTextEntry={true}
            style={styles.textInput}
          />
          <View style={{height: 20}}>
            {signupMutation.isError &&
              <Text style={{color: "#F00"}}>
                Login failed, please retry
              </Text>
            }
            {isConfirmPasswordNotCorrect &&
              <Text style={{color: "#F00"}}>
                The two passwords are not the same, please insert them again
              </Text>
            }
          </View>
          <Button
            mode="contained"
            onPress={() => handleSubmit()}
            style={{...styles.button, marginTop: 30}}
          >
            Sign up
          </Button>
        </View>
      )}
    </Formik>
  );
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