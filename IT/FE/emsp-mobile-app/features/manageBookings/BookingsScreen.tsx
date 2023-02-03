import * as React from 'react';
import {ScrollView} from "react-native";
import {BookingsStackScreenProps} from "../../navigation/types";
import {allBookingsQuery, allBookingsStatusQuery} from "./BookingApi";
import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {useGetAuthInfo} from "../../user-auth/UserAuthenticationUtils";
import {Booking, BookingApi, BookingStatus, BookingStatusCancelled, BookingStatusInProgress} from "../../generated";
import {BookingItem, getTimeLeftInfo} from "./BookingItem";
import {compareDesc} from "date-fns";

export type BookingItem = Booking & {status: BookingStatus};

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
        const bookingEnding = bookingListQuery.status === "success" && data
          .filter(s => s.bookingStatus === "BookingStatusInProgress")
          .map(s => ({...bookingListQuery.data.find(b => b.bookingId === s.bookingId), status: s}))
          .map(b => getTimeLeftInfo(b))
          .filter(b => b.chargeStarted)
          // @ts-ignore
          .some(b => b.minutesLeft <= 2);
        if(bookingEnding)
          return 3 * 1000;
        else
          return 30 * 1000;
      }
    }
  );
  const bookingStatusList = bookingStatusListQuery.status === "success" ? bookingStatusListQuery.data : [];

  const bookingList: BookingItem[] = (bookingListQuery.status === "success" && bookingStatusListQuery.status === "success") ?
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