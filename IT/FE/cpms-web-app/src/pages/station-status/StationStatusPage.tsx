import React from "react";
import {useNavigate, useParams} from "react-router-dom";
import {useQuery} from "@tanstack/react-query";
import {chargingStationListQuery} from "../station-list/StationListApi";
import {AvailabilityOverview, ChargingPoint, ChargingStation, SocketForDashboard, SocketType} from "../../generated";
import {chargingStationStatusQuery} from "./StationStatusApi";
import {Button} from "@themesberg/react-bootstrap";
import {WebRoutes} from "../../router/WebRoutes";

type SocketStatusDetail = SocketForDashboard & { type: SocketType };
type ChargingPointStatusDetail = Omit<ChargingPoint, "socketList"> & { socketList: Array<SocketStatusDetail> };
type StationStatusDetail =
  Omit<ChargingStation, "chargingPointList"> &
  { overview: AvailabilityOverview } &
  { chargingPointList: Array<ChargingPointStatusDetail> };

export function StationStatusPage() {

  const params = useParams();
  const navigate = useNavigate();
  const stationId = parseInt(params.stationId);

  const stationListQuery = useQuery(chargingStationListQuery());
  const stationStatusQuery = useQuery({
    ...chargingStationStatusQuery(stationId),
    refetchInterval: 15 * 1000
  });

  if (stationListQuery.status === "success" && stationStatusQuery.status === "success") {
    const stationConfig = stationListQuery.data.find(s => s.chargingStationId === stationId);
    const station: StationStatusDetail = {
      ...(({chargingPointList, ...config}) => config)(stationConfig),
      ...stationStatusQuery.data,
      chargingPointList: stationStatusQuery.data.chargingPointList.map(cp => ({
        ...cp,
        socketList: cp.socketList.map(socket => ({
          ...socket,
          type: stationConfig.chargingPointList
            .flatMap(cp => cp.socketList)
            .find(s => s.socketId === socket.socketId)
            .type
        }))
      }))
    };

    console.log(station)

    return (
      <div>
        STATION STATUS PAGE
        <Button onClick={() => navigate(WebRoutes.Stations.List.buildPath())}>
          Go back to station list page
        </Button>
      </div>
    );
  } else {
    return (
      <>
      </>
    );
  }
}