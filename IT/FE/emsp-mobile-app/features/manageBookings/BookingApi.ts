import {QueryKeys} from "../../api/QueryKeys";
import {BookingService} from "../../generated";

export const allBookingsQuery = (customerId: number) => ({
  queryKey: QueryKeys.Booking.All(customerId),
  queryFn: () => BookingService.getAllBookings(customerId)
})

export const bookingQuery = (customerId: number, bookingId: number) => ({
  queryKey: QueryKeys.Booking.Booking(customerId, bookingId),
  queryFn: () => BookingService.getBooking(customerId, bookingId)
})

export const bookingStatusQuery = (customerId: number, bookingId: number) => ({
  queryKey: QueryKeys.Booking.BookingStatus(customerId, bookingId),
  queryFn: () => BookingService.getBookingStatus(customerId, bookingId)
})