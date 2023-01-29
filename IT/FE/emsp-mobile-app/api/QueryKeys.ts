export const QueryKeys = {
  Booking: {
    All: (customerId: number) => ["Bookings", customerId],
    Booking: (customerId: number, bookingId: number) => ["Bookings", customerId, bookingId],
    BookingStatus: (customerId: number, bookingId: number) => ["Bookings", customerId, bookingId, "Status"]
  }
}