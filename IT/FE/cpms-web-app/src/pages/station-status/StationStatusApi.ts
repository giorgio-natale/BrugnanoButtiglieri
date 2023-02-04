import {QueryKeys} from "../../api/QueryKeys";
import {ChargingManagementApi} from "../../generated";

export const chargingStationStatusQuery = (stationId) => ({
  queryKey: QueryKeys.Stations.StationStatus(stationId),
  queryFn: () => ChargingManagementApi.getChargingStationStatus(stationId)
})