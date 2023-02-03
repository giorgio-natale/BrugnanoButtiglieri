import * as React from 'react';
import {useContext} from 'react';
import {Platform, StyleSheet, View} from "react-native";
import {BookChargeTabScreenProps} from "../../navigation/types";
import {ActivityIndicator, Button, List, SegmentedButtons, Text, Text as TextPaper} from "react-native-paper";
import MaterialCommunityIcons from "react-native-vector-icons/MaterialCommunityIcons";
import RNDateTimePicker, {DateTimePickerAndroid} from '@react-native-community/datetimepicker';
import {BookChargeContext} from "./BookChargeScreen";
import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {StackActions, useNavigation} from "@react-navigation/native";
import {useGetAuthInfo} from "../../user-auth/UserAuthenticationUtils";
import {BookingApi, BookingInAdvance, SocketType} from "../../generated";
import {Formik} from "formik";
import format from 'date-fns/format'
import {stationConfigQuery} from "../findStation/StationApi";
import {addMinutes} from "date-fns";

export function BookInAdvanceScreen(props: BookChargeTabScreenProps<"BookInAdvance">) {

  const {chargingStationId} = useContext(BookChargeContext);
  const {status, data} = useQuery(stationConfigQuery(chargingStationId));

  const queryClient = useQueryClient();
  const navigation = useNavigation();
  const authInfo = useGetAuthInfo();

  const postBookingMutation = useMutation(
    (values: BookingInAdvance) =>
      BookingApi.postBooking(authInfo.customerId, values),
    {
      // TODO setQueryData
      onSuccess: () => queryClient.invalidateQueries()
        .then(() => navigation.dispatch(StackActions.pop(1)))
        .then(() => navigation.navigate("Bookings"))
    }
  );

  return (
    <Formik<{ socketType: SocketType, timeframe: { startDate: Date, startTime: Date, duration: Date } }>
      initialValues={{
        socketType: null,
        timeframe: {startDate: new Date(), startTime: new Date(), duration: new Date("1970-01-01")}
      }}
      onSubmit={values => {
        const start = new Date(
          values.timeframe.startDate.getFullYear(),
          values.timeframe.startDate.getMonth(),
          values.timeframe.startDate.getDate(),
          values.timeframe.startTime.getHours(),
          values.timeframe.startTime.getMinutes()
        );
        const end = addMinutes(
          new Date(start),
          values.timeframe.duration.getMinutes() + 60 * values.timeframe.duration.getHours()
        );
        postBookingMutation.mutate({
          socketType: values.socketType,
          timeframe: {
            startInstant: start.toISOString(),
            endInstant: end.toISOString()
          },
          bookingType: 'BookingInAdvance' as BookingInAdvance["bookingType"],
          customerId: authInfo.customerId,
          chargingStationId: chargingStationId
        });
      }}
    >
      {({handleChange, handleSubmit, values, setFieldValue}) => (
        <View style={styles.container}>
          <List.Item
            title={status === "success" ? data.name : ""}
            description={status === "success" ? `${data.address} - ${data.city}` : ""}
            left={props => <MaterialCommunityIcons {...props} name="map-marker" size={35}/>}
          />
          <View style={{...styles.startDatetimeContainer, ...styles.viewElement}}>
            <TextPaper
              variant="titleMedium"
              style={{marginRight: 10}}
            >
              Start datetime
            </TextPaper>
            {Platform.OS === "ios" &&
              <>
                <RNDateTimePicker
                  mode="date" value={values.timeframe.startDate} style={{marginRight: 10}}
                  accentColor="rgb(103, 80, 164)"
                  themeVariant={"light"}
                  onChange={e => setFieldValue("timeframe.startDate", new Date(e.nativeEvent.timestamp))}
                />
                <RNDateTimePicker
                  mode="time" value={values.timeframe.startTime} accentColor="rgb(103, 80, 164)" themeVariant={"light"}
                  onChange={e => setFieldValue("timeframe.startTime", new Date(e.nativeEvent.timestamp))}
                />
              </>
            }
            {Platform.OS === "android" &&
              <>
                <Button
                  onPress={v => DateTimePickerAndroid.open({
                    value: values.timeframe.startDate,
                    onChange: e => setFieldValue("timeframe.startDate", new Date(e.nativeEvent.timestamp)),
                    mode: "date"
                  })}
                >
                  {format(values.timeframe.startDate, "PP")}
                </Button>
                <Button
                  onPress={v => DateTimePickerAndroid.open({
                    value: values.timeframe.startTime,
                    onChange: e => setFieldValue("timeframe.startTime", new Date(e.nativeEvent.timestamp)),
                    mode: "time",
                    is24Hour: true
                  })}
                >
                  {format(values.timeframe.startTime, "HH:mm")}
                </Button>
              </>
            }
          </View>
          <View style={{...styles.startDatetimeContainer, ...styles.viewElement}}>
            <TextPaper
              variant="titleMedium"
              style={{marginRight: 10}}
            >
              Charge duration
            </TextPaper>
            {Platform.OS === "ios" &&
              <RNDateTimePicker
                mode="time" value={values.timeframe.duration} accentColor="rgb(103, 80, 164)" themeVariant={"light"}
                onChange={e => setFieldValue("timeframe.duration", new Date(e.nativeEvent.timestamp))}
              />
            }
            {Platform.OS === "android" &&
              <Button
                onPress={v => DateTimePickerAndroid.open({
                  value: values.timeframe.duration,
                  onChange: e => setFieldValue("timeframe.duration", new Date(e.nativeEvent.timestamp)),
                  mode: "time",
                  is24Hour: true
                })}
              >
                {format(values.timeframe.duration, "HH:mm")}
              </Button>
            }
          </View>
          <View style={styles.viewElement}>
            <TextPaper variant="titleMedium">
              Desired socket type
            </TextPaper>
            <SegmentedButtons
              value={values.socketType}
              style={styles.viewElement}
              onValueChange={handleChange("socketType")}
              buttons={[
                {
                  value: SocketType.SLOW,
                  label: 'Slow',
                  showSelectedCheck: true
                },
                {
                  value: SocketType.FAST,
                  label: 'Fast',
                  showSelectedCheck: true
                },
                {
                  value: SocketType.RAPID,
                  label: 'Rapid',
                  showSelectedCheck: true
                }
              ]}
            />
          </View>
          <View style={{marginTop: "auto", marginBottom: 20, width: "100%", height: 45}} pointerEvents={"box-none"}>
            <View style={styles.submitBtn} pointerEvents={"none"}>
              <Text>Submit booking</Text>
              {postBookingMutation.isLoading &&
                <ActivityIndicator animating={true} color={"#FFF"} style={{marginLeft: 10}}/>
              }
            </View>
            <Button
              disabled={values.timeframe === null || values.socketType === null}
              onPress={() => handleSubmit()}
              mode="contained"
              style={{height: "100%", width: "100%"}}
            >
            </Button>
          </View>
          <View>
            {postBookingMutation.isError &&
              <Text style={{color: "#F00"}}>
                Oops, an error occured in the booking. Please change your preferences and retry
              </Text>
            }
          </View>
        </View>
      )
      }
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
  startDatetimeContainer: {
    display: "flex",
    flexDirection: "row",
    alignItems: "center"
  },
  viewElement: {
    marginLeft: 10,
    marginRight: 10,
    marginTop: 20
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
