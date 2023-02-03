import * as React from 'react';
import {ScrollView} from "react-native";
import {BookingsStackScreenProps} from "../../navigation/types";
import {allBookingsQuery, allBookingsStatusQuery} from "./BookingApi";
import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {useGetAuthInfo} from "../../user-auth/UserAuthenticationUtils";
import {BookingApi, BookingStatus, BookingStatusCancelled, BookingStatusInProgress} from "../../generated";
import {BookingItem} from "./BookingItem";
import {compareDesc} from "date-fns";

export function BookingsScreen(props: BookingsStackScreenProps<"BookingsScreen">) {
  const authInfo = useGetAuthInfo();
  const queryClient = useQueryClient();

  const bookingStatusListQuery = useQuery({
      ...allBookingsStatusQuery(authInfo.customerId),
      refetchInterval: 30 * 1000
    }
  );
  const bookingStatusList = bookingStatusListQuery.status === "success" ? bookingStatusListQuery.data : [];

  const bookingListQuery = useQuery(allBookingsQuery(authInfo.customerId));
  const bookingList = (bookingListQuery.status === "success" && bookingStatusListQuery.status === "success") ?
    bookingListQuery.data
      .sort((a, b) =>
        compareDesc(new Date(a.timeframe.startInstant), new Date(b.timeframe.startInstant))
      ).map(booking => ({
      ...booking,
      status: bookingStatusList.find(b => b.bookingId === booking.bookingId) as BookingStatus
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

  return <ScrollView>
    {(bookingListQuery.status === "success" && bookingStatusListQuery.status === "success")
      && bookingList.map(b => (
        <BookingItem
          key={b.bookingId}
          booking={b}
          onActivateBooking={activateBookingMutation.mutate}
          onDeleteBooking={deleteBookingMutation.mutate}
        />
      ))}
  </ScrollView>
}