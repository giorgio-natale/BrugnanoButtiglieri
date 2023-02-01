export const QueryKeys = {
  Bookings: {
    All: (customerId: number) => ["Bookings", customerId],
    Booking: (customerId: number, bookingId: number) => ["Bookings", customerId, bookingId],
    BookingStatus: (customerId: number, bookingId: number) => ["Bookings", customerId, bookingId, "Status"]
  },
  Stations: {
    All: () => ["Stations"],
    Station: (stationId: number) => ["Stations", stationId]
  }
}