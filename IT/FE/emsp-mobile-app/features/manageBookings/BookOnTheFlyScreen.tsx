import * as React from 'react';
import {useContext} from 'react';
import {StyleSheet, View} from "react-native";
import {BookChargeTabScreenProps} from "../../navigation/types";
import {ActivityIndicator, Button, List, RadioButton, Text} from "react-native-paper";
import MaterialCommunityIcons from "react-native-vector-icons/MaterialCommunityIcons";
import {Formik} from 'formik';
import {stationConfigQuery} from "../findStation/StationApi";
import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {StackActions, useNavigation} from "@react-navigation/native";
import {BookingApi, BookingOnTheFly} from "../../generated";
import {BookChargeContext} from "./BookChargeScreen";
import {useGetAuthInfo} from "../../user-auth/UserAuthenticationUtils";

export function BookOnTheFlyScreen(props: BookChargeTabScreenProps<"BookOnTheFly">) {

  const {chargingStationId} = useContext(BookChargeContext);
  const {status, data} = useQuery(stationConfigQuery(chargingStationId));

  const chargingPointList = status === "success" ? data.chargingPointList : [];
  const selectableSocketList = chargingPointList
    .filter(cp => cp.mode === "ON_THE_FLY")
    .flatMap(cp => cp.socketList.map(s => ({
      chargingPointCode: cp.chargingPointCode,
      chargingPointId: cp.chargingPointId,
      socketCode: s.socketCode,
      socketId: s.socketId,
      socketType: s.type
    })));

  const queryClient = useQueryClient();
  const navigation = useNavigation();
  const authInfo = useGetAuthInfo();

  const postBookingMutation = useMutation(
    (values: BookingOnTheFly & { customerId: number, chargingStationId: number }) =>
      BookingApi.postBooking(authInfo.customerId, values),
    {
      onSuccess: () => queryClient.invalidateQueries()
        .then(() => navigation.dispatch(StackActions.pop(1)))
        .then(() => navigation.navigate("Bookings"))
    }
  );

  return (
    <Formik
      initialValues={{"chargingPointId": null, "socketId": null}}
      onSubmit={values => {
        postBookingMutation.mutate({
          ...values,
          bookingType: 'BookingOnTheFly' as BookingOnTheFly["bookingType"],
          customerId: authInfo.customerId,
          chargingStationId: data?.chargingStationId
        });
      }
      }
    >
      {({handleSubmit, values, setFieldValue}) => (
        <View style={styles.container}>
          <List.Item
            title={status === "success" ? data.name : ""}
            description={status === "success" ? `${data.address} - ${data.city}` : ""}
            left={props => <MaterialCommunityIcons {...props} name="map-marker" size={35}/>}
          />
          <View style={{marginBottom: 10, alignSelf: "center"}}>
            <Text variant="titleMedium">Select the tuple of charging point - socket:</Text>
          </View>
          {selectableSocketList.map(s => {

            const socketKey = s.chargingPointCode + "-" + s.socketCode;

            function isItemChecked() {
              return values.chargingPointId === s.chargingPointId && values.socketId === s.socketId
            }

            return (
              <RadioButton.Item
                key={socketKey}
                label={`${socketKey} (Type: ${s.socketType})`}
                labelStyle={{fontSize: 18}}
                style={{backgroundColor: isItemChecked() ? "rgba(151,114,227,0.5)" : "rgba(234,234,234,0.34)"}}
                value={socketKey}
                onPress={() => {
                  setFieldValue("chargingPointId", s.chargingPointId);
                  setFieldValue("socketId", s.socketId);
                }}
                status={isItemChecked() ? "checked" : "unchecked"}
              />
            )
          })}
          <View style={{marginTop: "auto", marginBottom: 20, width: "100%", height: 45}} pointerEvents={"box-none"}>
            <View style={styles.submitBtn} pointerEvents={"none"}>
              <Text>Submit booking</Text>
              {postBookingMutation.isLoading &&
                <ActivityIndicator animating={true} color={"#FFF"} style={{marginLeft: 10}}/>
              }
            </View>
            <Button
              disabled={values.chargingPointId === null || values.socketId === null}
              onPress={() => handleSubmit()}
              mode="contained"
              style={{height: "100%", width: "100%"}}
            >
            </Button>
          </View>
          <View>
            {postBookingMutation.isError &&
              <Text style={{color: "#F00"}}>
                Oops, an error occured in the booking. Please retry
              </Text>
            }
          </View>
        </View>
      )}
    </Formik>
  );
}

const styles = StyleSheet.create({
  container: {
    display: "flex",
    flex: 1,
    flexDirection: "column",
    padding: 10
  },
  submitBtn: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    position: "absolute",
    height: "100%",
    width: "100%",
    zIndex: 100
  }
});
