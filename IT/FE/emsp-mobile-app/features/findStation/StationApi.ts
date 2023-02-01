import {QueryKeys} from "../../api/QueryKeys";
import {EmspChargingStationApi} from "../../generated";

export const allStationsQuery = () => ({
  queryKey: QueryKeys.Stations.All(),
  queryFn: () => EmspChargingStationApi.getChargingStationOverviewList()
})

export const stationQuery = (stationId: number) => ({
  queryKey: QueryKeys.Stations.Station(stationId),
  queryFn: () => EmspChargingStationApi.getChargingStationOverview(stationId)
})