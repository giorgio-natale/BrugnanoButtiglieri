export const QueryKeys = {
  Stations: {
    AllStationConfig: () => ["StationsConfig"],
    StationConfig: (stationId: number) => ["StationsConfig", stationId],
    StationStatus: (stationId: number) => ["StationsConfig", stationId, "StationsStatus"]
  },
  Pricing: {
    AllStationPricing: () => ["Pricing"]
  },
  Employee: {
    Employee: (employeeId: number) => ["Customer", employeeId]
  }
}