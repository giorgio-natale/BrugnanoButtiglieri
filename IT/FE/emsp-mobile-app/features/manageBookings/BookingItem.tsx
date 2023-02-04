import * as React from 'react';
import {useEffect, useState} from 'react';
import {Booking, BookingStatus, BookingStatusCompleted, BookingStatusInProgress, Timeframe} from "../../generated";
import {StyleSheet, View} from "react-native";
import {ActivityIndicator, Button, Divider, Text} from "react-native-paper";
import {useQuery} from "@tanstack/react-query";
import {stationConfigQuery} from "../findStation/StationApi";
import {differenceInMinutes, intervalToDuration, isBefore, isWithinInterval} from "date-fns";
import format from "date-fns/format";
import {BookingItemType} from "./BookingsScreen";

interface Props {
  booking: Booking & { status: BookingStatus },
  onDeleteBooking: (bookingId: number) => void,
  onActivateBooking: (bookingId: number) => void
}

export function BookingItem(props: Props) {

  const {booking, onDeleteBooking, onActivateBooking} = props;

  const {status, data} = useQuery(stationConfigQuery(booking.chargingStationId));

  const [timeLeftInfo, setTimeLeftInfo] = useState<TimeLeftInfo | null>(null);

  useEffect(() => {
    if (booking?.status?.bookingStatus === "BookingStatusInProgress") {
      const interval = setInterval(() =>
          setTimeLeftInfo(getTimeLeftInfo(booking))
        , 5 * 1000);
      return () => clearTimeout(interval);
    }
    // @ts-ignore
  }, [booking.status.bookingStatus, booking.status.expectedMinutesLeft]);

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

  function canPlannedBookingBeActivated(booking): boolean {
    return booking?.status?.bookingStatus === "BookingStatusPlanned" &&
      (booking.bookingType === "ON_THE_FLY" ||
        (booking.bookingType === "IN_ADVANCE" && isNowWithinTimeframe(booking.timeframe))
      );
  }

  function canPlannedBookingBeDeleted(booking): boolean {
    return booking?.status?.bookingStatus === "BookingStatusPlanned" &&
      booking.bookingType === "IN_ADVANCE" &&
      isNowBeforeTimeframe(booking.timeframe);
  }

  function getBackgroundColor(booking) {
    let backgroundColor;
    if (booking?.status?.bookingStatus === "BookingStatusInProgress" || canPlannedBookingBeActivated(booking))
      backgroundColor = "rgba(172,146,225,0.5)";
    else if (canPlannedBookingBeDeleted(booking))
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
                {`${getFormattedDateTime(booking.timeframe.startInstant)}${booking.bookingType === "IN_ADVANCE" ?
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
            {canPlannedBookingBeDeleted(booking) &&
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
            {canPlannedBookingBeActivated(booking) &&
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
                {!timeLeftInfo?.chargeStarted &&
                  <ActivityIndicator animating={true} color={"rgb(107, 79, 170)"} style={{marginRight: 5}}/>
                }
                {timeLeftInfo?.chargeStarted && timeLeftInfo?.minutesLeft > 0 &&
                  <Button
                    mode={"outlined"}
                    style={{borderColor: "transparent"}}
                    contentStyle={{flexDirection: "row-reverse"}}
                    labelStyle={{fontSize: 20}}
                    icon={timeLeftInfo?.endReason === "END_OF_BOOKING" ? "timer" : "battery-charging"}
                  >
                    - {timeLeftInfo?.minutesLeft}'
                  </Button>
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

function getMinutesLeftToEndOfBooking(booking) {
  return differenceInMinutes(new Date(booking.timeframe.endInstant), new Date(), {roundingMethod: "ceil"});
}

type TimeLeftInfo = {
  minutesLeft: number,
  endReason: "FULL_BATTERY" | "END_OF_BOOKING",
  chargeStarted: true
} | {
  chargeStarted: false
};

export function getTimeLeftInfo(booking: BookingItemType): TimeLeftInfo {
  if (booking.status.bookingStatus !== "BookingStatusInProgress")
    throw Error("Unexpected error: booking status should be in progress");
  const fullBatteryMinutesLeft = booking.status.expectedMinutesLeft;
  if(fullBatteryMinutesLeft < 0 || !fullBatteryMinutesLeft)
    return {
    chargeStarted: false
    }
  if (booking.bookingType === "ON_THE_FLY") {
    return {
      minutesLeft: fullBatteryMinutesLeft,
      endReason: "FULL_BATTERY",
      chargeStarted: true
    };
  } else {
    const endOfBookingMinutesLeft = getMinutesLeftToEndOfBooking(booking);
    if (fullBatteryMinutesLeft < endOfBookingMinutesLeft) {
      return {
        minutesLeft: fullBatteryMinutesLeft,
        endReason: "FULL_BATTERY",
        chargeStarted: true
      };
    } else {
      return {
        minutesLeft: endOfBookingMinutesLeft,
        endReason: "END_OF_BOOKING",
        chargeStarted: true
      }
    }
  }
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