import {createContext} from "react";

export const BookChargeContext = createContext<{ chargingStationId: number | null }>({chargingStationId: null});
