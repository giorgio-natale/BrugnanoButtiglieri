import * as React from 'react';
import {ScrollView, Text, View} from "react-native";
import {BookingsStackScreenProps} from "../../navigation/types";
import {allBookingsQuery, allBookingsStatusQuery} from "./BookingApi";
import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {useGetAuthInfo} from "../../user-auth/UserAuthenticationUtils";
import {Booking, BookingApi, BookingStatus, BookingStatusCancelled, BookingStatusInProgress} from "../../generated";
import {BookingItem, getTimeLeftInfo} from "./BookingItem";
import {compareDesc} from "date-fns";

export type BookingItemType = Booking & {status: BookingStatus};

export function BookingsScreen(props: BookingsStackScreenProps<"BookingsScreen">) {
  const authInfo = useGetAuthInfo();
  const queryClient = useQueryClient();

  const bookingListQuery = useQuery({
      ...allBookingsQuery(authInfo.customerId)
    }
  );

  const bookingStatusListQuery = useQuery({
      ...allBookingsStatusQuery(authInfo.customerId),
      refetchInterval: (data: BookingStatus[]) => {
        const fastRefresh = bookingListQuery.status === "success" && data &&
          data
          .filter(s => s.bookingStatus === "BookingStatusInProgress")
          .map(s => ({...bookingListQuery.data.find(b => b.bookingId === s.bookingId), status: s}))
          .map(b => getTimeLeftInfo(b))
          .some(b => !b.chargeStarted || b.minutesLeft <= 2);
        if(fastRefresh)
          return 2 * 1000;
        else
          return 30 * 1000;
      }
    }
  );
  const bookingStatusList = bookingStatusListQuery.status === "success" ? bookingStatusListQuery.data : [];

  const bookingList: BookingItemType[] = (bookingListQuery.status === "success" && bookingStatusListQuery.status === "success") ?
    bookingListQuery.data
      .sort((a, b) =>
        compareDesc(new Date(a.timeframe.startInstant), new Date(b.timeframe.startInstant))
      ).map(booking => ({
      ...booking,
      status: bookingStatusList.find(b => b.bookingId === booking.bookingId)
    })) : [];

  const deleteBookingMutation = useMutation(
    (bookingId: number) =>
      BookingApi.putBookingStatus(authInfo.customerId, bookingId, {
        bookingId: bookingId,
        bookingStatus: "BookingStatusCancelled"
      } as BookingStatusCancelled),
    {
      onMutate: (bookingId) => ({bookingId: bookingId}),
      onSuccess: (data, variables, context) => {
        queryClient.invalidateQueries(["Bookings", authInfo.customerId, "Status"]);
        queryClient.invalidateQueries(["Bookings", authInfo.customerId, "Status", context.bookingId]);
      }
    }
  );

  const activateBookingMutation = useMutation(
    (bookingId: number) =>
      BookingApi.putBookingStatus(authInfo.customerId, bookingId, {
        bookingId: bookingId,
        bookingStatus: "BookingStatusInProgress"
      } as BookingStatusInProgress),
    {
      onMutate: (bookingId) => ({bookingId: bookingId}),
      onSuccess: (data, variables, context) => {
        queryClient.invalidateQueries(["Bookings", authInfo.customerId, "Status"]);
        queryClient.invalidateQueries(["Bookings", authInfo.customerId, "Status", context.bookingId]);
      }
    }
  );

  if(bookingListQuery.status === "success" && bookingStatusListQuery.status === "success" && bookingList.length > 0)
    return <ScrollView>
      {bookingList.map(b => {
        if(b && b.status)
          return (
            <BookingItem
              key={b.bookingId}
              booking={b}
              onActivateBooking={activateBookingMutation.mutate}
              onDeleteBooking={deleteBookingMutation.mutate}
            />
          )
      })}
    </ScrollView>;
  else if(bookingListQuery.status === "success" && bookingStatusListQuery.status === "success" && bookingList.length === 0)
    return <View style={{padding: 20}}>
      <Text style={{fontSize: 17, marginBottom: 7}}>You don't have any bookings.</Text>
      <Text style={{fontSize: 17}}>Find a station and book yours!</Text>
    </View>
}