export const QueryKeys = {
  Bookings: {
    All: (customerId: number) => ["Bookings", customerId, "List"],
    Booking: (customerId: number, bookingId: number) => ["Bookings", customerId, bookingId],
    AllBookingStatus: (customerId: number) => ["Bookings", customerId, "Status", "List"],
    BookingStatus: (customerId: number, bookingId: number) => ["Bookings", customerId, "Status", bookingId]
  },
  StationsOverview: {
    All: () => ["StationsOverview"],
    Station: (stationId: number) => ["StationsOverview", stationId]
  },
  StationsConfig: {
    All: () => ["StationsConfig"],
    Station: (stationId: number) => ["StationsConfig", stationId]
  },
  Customer: {
    CustomerProfile: (customerId: number) => ["Customer", customerId]
  }
}