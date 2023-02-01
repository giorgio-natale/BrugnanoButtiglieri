import React, {useEffect, useRef, useState} from "react";
import * as Notifications from "expo-notifications";
import {registerForPushNotificationsAsync} from "./NotificationsUtils";
import {StyleSheet, TouchableWithoutFeedback, View} from "react-native";
import {Text} from "react-native-paper";
import {useNavigation} from "@react-navigation/native";
import MaterialCommunityIcons from "react-native-vector-icons/MaterialCommunityIcons";

interface Props {
  children: JSX.Element
}

interface Notification {
  title: string,
  body: string,
  data: {
    bookingId: number,
    notificationType: "BOOKING_COMPLETED"
  }
}

export const NotificationsManager: React.FC<Props> = ({children}) => {

  const navigation = useNavigation();

  const notificationListener = useRef<any>();
  const responseListener = useRef<any>();

  const timeout = useRef<NodeJS.Timeout>();

  const [notification, setNotification] = useState<Notification | null>(null);

  useEffect(() => {
    registerForPushNotificationsAsync()
    // TODO .then(token => setExpoPushToken(token));

    notificationListener.current = Notifications.addNotificationReceivedListener(notification => {
      const notificationContent = notification.request.content;
      if (notificationContent.data.notificationType === "BOOKING_COMPLETED") {
        if (timeout.current)
          clearTimeout(timeout.current);
        setNotification({
          title: notificationContent.title,
          body: notificationContent.body,
          data: notificationContent.data as Notification["data"]
        });
        timeout.current = setTimeout(() => setNotification(null), 10 * 1000);
      }
    });

    responseListener.current = Notifications.addNotificationResponseReceivedListener(response => {
      setNotification(null);
      navigation.navigate("Bookings");
    });

    return () => {
      Notifications.removeNotificationSubscription(notificationListener.current);
      Notifications.removeNotificationSubscription(responseListener.current);
    };
  }, []);

  return (
    <View style={{height: "100%", width: "100%"}}>
      {notification !== null &&
        <TouchableWithoutFeedback onPress={() => {
          setNotification(null);
          navigation.navigate("Bookings");
        }}>
          <View style={styles.notificationContainer}>
            <View style={styles.notificationInnerContainer}>
              <View style={styles.notificationTextContainer}>
                <Text style={styles.notificationTitle}>{notification.title}</Text>
                <Text style={styles.notificationBody}>{notification.body}</Text>
              </View>
              <MaterialCommunityIcons name="chevron-right" size={50} style={{color: "#FFF"}}/>
            </View>
          </View>
        </TouchableWithoutFeedback>
      }
      {children}
    </View>
  );
}

const styles = StyleSheet.create({
  notificationContainer: {
    position: "absolute",
    top: 50,
    zIndex: 100,
    display: "flex",
    flexDirection: "row",
    justifyContent: "center",
    alignItems: "center",
    width: "100%"
  },
  notificationInnerContainer: {
    display: "flex",
    flexDirection: "row",
    width: "90%",
    alignItems: "center",
    justifyContent: "center",
    backgroundColor: "rgba(44,43,43,0.92)",
    borderRadius: 10,
    padding: 18,
    paddingTop: 15,
    paddingBottom: 15,
    shadowColor: '#000',
    shadowOffset: {width: 2, height: 2},
    shadowOpacity: 0.3,
    shadowRadius: 2
  },
  notificationTextContainer: {
    display: "flex"
  },
  notificationTitle: {
    fontWeight: "bold",
    fontSize: 18,
    paddingBottom: 5,
    color: "#FFF"
  },
  notificationBody: {
    fontSize: 17,
    color: "#FFF"
  }
});