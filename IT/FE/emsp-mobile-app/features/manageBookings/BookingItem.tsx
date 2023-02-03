import * as React from 'react';
import {Booking, BookingStatus, BookingStatusCompleted, Timeframe} from "../../generated";
import {StyleSheet, View} from "react-native";
import {Button, Divider, Text} from "react-native-paper";
import {useQuery} from "@tanstack/react-query";
import {stationConfigQuery} from "../findStation/StationApi";
import {formatDistanceStrict, intervalToDuration, isBefore, isWithinInterval} from "date-fns";
import format from "date-fns/format";

interface Props {
  booking: Booking & { status: BookingStatus },
  onDeleteBooking: (bookingId: number) => void,
  onActivateBooking: (bookingId: number) => void
}

export function BookingItem(props: Props) {

  const {booking, onDeleteBooking, onActivateBooking} = props;

  const {status, data} = useQuery(stationConfigQuery(booking.chargingStationId));

  let chargingPointList, chargingPoint, socket;

  if (status === "success") {
    chargingPointList = data.chargingPointList;
    chargingPoint = chargingPointList.find(cp => cp.chargingPointId === booking.chargingPointId);
    socket = chargingPoint.socketList.find(s => s.socketId === booking.socketId);
  }

  function isNowWithinTimeframe(timeframe: Timeframe): boolean {
    const now = new Date();
    const start = new Date(timeframe.startInstant);
    const end = new Date(timeframe.endInstant);
    return isWithinInterval(now, {start: start, end: end});
  }

  function isNowBeforeTimeframe(timeframe: Timeframe): boolean {
    const now = new Date();
    const start = new Date(timeframe.startInstant);
    return isBefore(now, start);
  }

  function getBackgroundColor(booking) {
    let backgroundColor;
    if (booking.status.bookingStatus === "BookingStatusInProgress" ||
      (booking.status.bookingStatus === "BookingStatusPlanned" && isNowWithinTimeframe(booking.timeframe)))
      backgroundColor = "rgba(172,146,225,0.5)";
    else if (booking.status.bookingStatus === "BookingStatusPlanned" && isNowBeforeTimeframe(booking.timeframe))
      backgroundColor = "rgba(255,255,255)";
    else
      backgroundColor = "rgba(229,227,227,0.5)";
    return backgroundColor;
  }

  function getFormattedDateTime(instant: string) {
    return format(new Date(instant), "PP - HH:mm");
  }

  function getFormattedDifferenceBetweenTwoInstants(startInstant: string, endInstant: string) {
    const duration = intervalToDuration({start: new Date(startInstant), end: new Date(endInstant)});
    return `${duration.hours}h ${duration.minutes}m`;
  }

  return (
    <>
      {status === "success" &&
        <>
          <View style={{...styles.item, backgroundColor: getBackgroundColor(booking)}}>
            <View>
              <Text style={styles.itemTitle}>
                Booking #{booking.bookingCode}
              </Text>
              <Text style={styles.itemSubtitle}>
                {`${getFormattedDateTime(booking.timeframe.startInstant)}${booking.timeframe?.endInstant ? 
                ` (${getFormattedDifferenceBetweenTwoInstants(booking.timeframe.startInstant, booking.timeframe.endInstant)})` : ""
                }`}
              </Text>
              <Text style={styles.description}>
                {`Socket ${chargingPoint.chargingPointCode}-${socket.socketCode} (${socket.type})`}
              </Text>
              <Text style={styles.description}>
                {`${data?.address}, ${data?.city}`}
              </Text>
            </View>
            {(booking.status.bookingStatus === "BookingStatusPlanned" && isNowBeforeTimeframe(booking.timeframe)) &&
              <View style={{alignSelf: "center"}}>
                <Button
                  mode={"contained-tonal"}
                  onPress={() => onDeleteBooking(booking.bookingId)}
                  labelStyle={{fontSize: 15}}
                >
                  Delete
                </Button>
              </View>
            }
            {(booking.status.bookingStatus === "BookingStatusPlanned" && isNowWithinTimeframe(booking.timeframe)) &&
              <View style={{alignSelf: "center"}}>
                <Button
                  mode={"contained"}
                  onPress={() => onActivateBooking(booking.bookingId)}
                  labelStyle={{fontSize: 17}}
                >
                  Charge
                </Button>
              </View>
            }
            {booking.status.bookingStatus === "BookingStatusInProgress" &&
              <View style={{alignSelf: "center"}}>
                {booking.status.expectedMinutesLeft >= 0 ?
                  <Button
                    mode={"outlined"}
                    labelStyle={{fontSize: 15}}
                  >
                    {/*TODO update expected time left*/}
                    {booking.status.expectedMinutesLeft}
                  </Button>
                  :
                  <Text>Plug it!</Text>
                }
              </View>
            }
            {(booking.status.bookingStatus === "BookingStatusCompleted" ||
                booking.status.bookingStatus === "BookingStatusExpired" ||
                booking.status.bookingStatus === "BookingStatusCancelled") &&
              <View>
                <Text variant="labelLarge">
                  {booking.status.bookingStatus === "BookingStatusCompleted" ? "Completed" : (
                    booking.status.bookingStatus === "BookingStatusExpired" ? "Expired" : "Cancelled"
                  )}
                </Text>
              </View>
            }
          </View>
          <Divider/>
        </>
      }
    </>
  );
}

const styles = StyleSheet.create({
  item: {
    padding: 10,
    paddingLeft: 25,
    display: "flex",
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "flex-start"
  },
  itemTitle: {
    fontWeight: "600",
    fontSize: 18
  },
  itemSubtitle: {
    fontSize: 16
  },
  description: {
    fontSize: 14
  }
})