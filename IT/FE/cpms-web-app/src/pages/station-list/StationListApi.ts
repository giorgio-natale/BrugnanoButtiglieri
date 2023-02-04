import {QueryKeys} from "../../api/QueryKeys";
import {CpmsChargingStationConfigurationApi, CpmsPricingApi} from "../../generated";

export const chargingStationListQuery = () => ({
  queryKey: QueryKeys.Stations.AllStationConfig(),
  queryFn: () => CpmsChargingStationConfigurationApi.getChargingStationConfigurationList()
})

export const pricingListQuery = () => ({
  queryKey: QueryKeys.Pricing.AllStationPricing(),
  queryFn: () => CpmsPricingApi.getPricingList()
})