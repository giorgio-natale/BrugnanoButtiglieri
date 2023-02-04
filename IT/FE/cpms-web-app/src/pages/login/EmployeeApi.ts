import {QueryKeys} from "../../api/QueryKeys";
import {EmployeeApi} from "../../generated";

export const employeeQuery = (employeeId: number) => ({
  queryKey: QueryKeys.Employee.Employee(employeeId),
  queryFn: () => EmployeeApi.getEmployee(employeeId)
})