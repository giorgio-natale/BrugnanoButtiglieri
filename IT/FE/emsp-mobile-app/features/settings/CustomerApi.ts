import {QueryKeys} from "../../api/QueryKeys";
import {CustomerApi} from "../../generated";

export const customerQuery = (customerId: number) => ({
  queryKey: QueryKeys.Customer.CustomerProfile(customerId),
  queryFn: () => CustomerApi.getCustomerCustomerId(customerId)
})