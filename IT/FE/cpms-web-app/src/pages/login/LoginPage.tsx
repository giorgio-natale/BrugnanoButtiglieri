import React from "react";
import {Button, Card} from '@themesberg/react-bootstrap';
import {Form, Formik} from 'formik';
import {InputField} from "./InputField";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faEnvelope, faUnlockAlt} from "@fortawesome/free-solid-svg-icons";
import {EmployeeApi, LoginRequest} from "../../generated";
import {saveAuthInfo} from "../../api/ApiConfig";
import {useNavigate} from "react-router-dom";
import {WebRoutes} from "../../router/WebRoutes";

export function LoginPage() {
  const navigate = useNavigate();

  return (
    <Formik<LoginRequest>
      initialValues={{
        emailAddress: null,
        password: null
      }}
      onSubmit={(values, {setErrors}) =>
        EmployeeApi.employeeLogin(values)
          .then(res => {
            saveAuthInfo({
              token: res.token,
              employeeId: res.employeeId,
              employeeName: res.name,
              employeeSurname: res.surname,
              employeeEmailAddress: res.emailAddress
            });
            navigate(WebRoutes.Stations.List.buildPath());
          })
          .catch(e => {
              setErrors({
                emailAddress: "",
                password: "Login failed, please retry"
              })
            }
          )
      }
    >
      {({isSubmitting, submitForm, values}) => (
        <Form>
          <main style={{height: `100vh`, position: "relative"}}>
            <Card
              style={{
                padding: "45px", position: "absolute", top: "50%", left: "50%", width: "550px",
                transform: "translate(-50%, -50%)"
              }}
            >
              <div style={{display: "flex", flexDirection: "column", gap: "25px"}}>
                <InputField
                  name="emailAddress" type="email" placeholder="Insert your email address" autoCompleteEnabled={true}
                  showValid={false}
                  label={
                    <div style={{
                      display: "flex",
                      flexDirection: "row",
                      gap: "8px",
                      alignItems: "center",
                      paddingLeft: "8px"
                    }}>
                      <FontAwesomeIcon icon={faEnvelope} style={{color: "#66799e"}}/>
                      Email address
                    </div>
                  }
                />
                <InputField
                  name="password" placeholder="Insert password" type="password" autoCompleteEnabled={true}
                  showValid={false}
                  label={
                    <div style={{
                      display: "flex",
                      flexDirection: "row",
                      gap: "8px",
                      alignItems: "center",
                      paddingLeft: "8px"
                    }}>
                      <FontAwesomeIcon icon={faUnlockAlt} style={{color: "#66799e"}}/>
                      Password
                    </div>
                  }
                />
                <Button onClick={submitForm} disabled={isSubmitting || !values.emailAddress || !values.password}>
                  Login
                </Button>
              </div>
            </Card>
          </main>
        </Form>
      )}
    </Formik>
  )
}