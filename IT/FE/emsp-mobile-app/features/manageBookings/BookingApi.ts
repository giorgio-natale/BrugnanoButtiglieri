import {QueryKeys} from "../../api/QueryKeys";
import {BookingApi} from "../../generated/services/BookingApi";

export const allBookingsQuery = (customerId: number) => ({
  queryKey: QueryKeys.Bookings.All(customerId),
  queryFn: () => BookingApi.getAllBookings(customerId)
})

export const bookingQuery = (customerId: number, bookingId: number) => ({
  queryKey: QueryKeys.Bookings.Booking(customerId, bookingId),
  queryFn: () => BookingApi.getBooking(customerId, bookingId)
})

export const allBookingsStatusQuery = (customerId: number) => ({
  queryKey: QueryKeys.Bookings.AllBookingStatus(customerId),
  queryFn: () => BookingApi.getAllBookingStatuses(customerId)
})

export const bookingStatusQuery = (customerId: number, bookingId: number) => ({
  queryKey: QueryKeys.Bookings.BookingStatus(customerId, bookingId),
  queryFn: () => BookingApi.getBookingStatus(customerId, bookingId)
})