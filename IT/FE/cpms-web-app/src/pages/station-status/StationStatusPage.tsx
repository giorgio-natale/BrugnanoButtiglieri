import React from "react";
import {useNavigate, useParams} from "react-router-dom";
import {useQuery} from "@tanstack/react-query";
import {chargingStationListQuery} from "../station-list/StationListApi";
import {AvailabilityOverview, ChargingPoint, ChargingStation, SocketForDashboard, SocketType} from "../../generated";
import {chargingStationStatusQuery} from "./StationStatusApi";
import {Button} from "@themesberg/react-bootstrap";
import {WebRoutes} from "../../router/WebRoutes";
import styles from "./StationStatusPage.module.scss";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faLocationDot} from "@fortawesome/free-solid-svg-icons/faLocationDot";
import {ChargingPointStatusComponent} from "./ChargingPointStatusComponent";
import {CircularProgressbar} from "react-circular-progressbar";
import 'react-circular-progressbar/dist/styles.css';

export type SocketStatusDetail = SocketForDashboard & { type: SocketType };
export type ChargingPointStatusDetail = Omit<ChargingPoint, "socketList"> & { socketList: Array<SocketStatusDetail> };
export type StationStatusDetail =
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

    return (
      <div className={styles.pageContainer}>
        <div className={styles.header}>
          <div className={styles.leftHeader}>
            <Button variant="outline-primary" onClick={() => navigate(WebRoutes.Stations.List.buildPath())}>
              Go back to station list page
            </Button>
            <div className={styles.title}>
              <div className={styles.name}>
                {station.name}
              </div>
              <div className={styles.address}>
                <FontAwesomeIcon icon={faLocationDot} size="sm" style={{marginRight: "10px"}}/>
                {station.address} - {station.city}
              </div>
            </div>
          </div>
          <div className={styles.rightHeader}>
            <div className={styles.graphComponent}>
              <div className={styles.graphSocketType}>
                SLOW SOCKETS
              </div>
              <div className={styles.graphContainer}>
                <SocketOccupationProgressBar
                  key={"slow-socket-graph"}
                  availableSocketNumber={station.overview.slow.availableSocketNumber}
                  totalSocketNumber={station.overview.slow.totalSocketNumber}/>
              </div>
              <div className={styles.socketMinutesLeft}>
                - {station.overview.slow.nearestExpectedMinutesLeft}' to free a socket
              </div>
            </div>
            <div className={styles.graphComponent}>
              <div className={styles.graphSocketType}>
                FAST SOCKETS
              </div>
              <div className={styles.graphContainer}>
                <SocketOccupationProgressBar
                  key={"fast-socket-graph"}
                  availableSocketNumber={station.overview.fast.availableSocketNumber}
                  totalSocketNumber={station.overview.fast.totalSocketNumber}/>
              </div>
              <div className={styles.socketMinutesLeft}>
                - {station.overview.fast.nearestExpectedMinutesLeft}' to free a socket
              </div>
            </div>
            <div className={styles.graphComponent}>
              <div className={styles.graphSocketType}>
                RAPID SOCKETS
              </div>
              <div className={styles.graphContainer}>
                <SocketOccupationProgressBar
                key={"rapid-socket-graph"}
                availableSocketNumber={station.overview.rapid.availableSocketNumber}
                totalSocketNumber={station.overview.rapid.totalSocketNumber}/>
            </div>
              <div className={styles.socketMinutesLeft}>
                - {station.overview.rapid.nearestExpectedMinutesLeft}' to free a socket
              </div>
            </div>
          </div>
        </div>
        <div className={styles.subtitle}>
          Charging points in advance
        </div>
        <div className={styles.horizontalFlex}>
          {station.chargingPointList.filter(cp => cp.mode === "IN_ADVANCE").map(cp => (
            <ChargingPointStatusComponent chargingPoint={cp} key={cp.chargingPointId}/>
          ))}
        </div>
        <div className={styles.subtitle}>
          Charging points on the fly
        </div>
        {station.chargingPointList.filter(cp => cp.mode === "ON_THE_FLY").map(cp => (
          <ChargingPointStatusComponent chargingPoint={cp} key={cp.chargingPointId}/>
        ))}
      </div>
    );
  } else {
    return (
      <>
      </>
    );
  }
}

function SocketOccupationProgressBar({availableSocketNumber, totalSocketNumber}) {
  return <CircularProgressbar
    value={totalSocketNumber - availableSocketNumber}
    minValue={0}
    maxValue={totalSocketNumber}
    text={`${Math.trunc((totalSocketNumber - availableSocketNumber)/totalSocketNumber*100)}%`}
  />
}

