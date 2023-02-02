export const QueryKeys = {
  Bookings: {
    All: (customerId: number) => ["Bookings", customerId],
    Booking: (customerId: number, bookingId: number) => ["Bookings", customerId, bookingId],
    BookingStatus: (customerId: number, bookingId: number) => ["Bookings", customerId, bookingId, "Status"]
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