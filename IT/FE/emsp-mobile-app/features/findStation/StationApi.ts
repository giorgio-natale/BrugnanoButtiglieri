import {QueryKeys} from "../../api/QueryKeys";
import {CpmsChargingStationConfigurationApi, EmspChargingStationApi} from "../../generated";

export const allStationsOverviewQuery = () => ({
  queryKey: QueryKeys.StationsOverview.All(),
  queryFn: () => EmspChargingStationApi.getChargingStationOverviewList()
})

export const stationOverviewQuery = (stationId: number) => ({
  queryKey: QueryKeys.StationsOverview.Station(stationId),
  queryFn: () => EmspChargingStationApi.getChargingStationOverview(stationId)
})

export const allStationsConfigQuery = () => ({
  queryKey: QueryKeys.StationsConfig.All(),
  queryFn: () => CpmsChargingStationConfigurationApi.getChargingStationConfigurationList()
})

export const stationConfigQuery = (stationId: number) => ({
  queryKey: QueryKeys.StationsConfig.Station(stationId),
  queryFn: () => CpmsChargingStationConfigurationApi.getChargingStationConfiguration(stationId)
})