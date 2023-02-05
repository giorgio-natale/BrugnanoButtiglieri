import {useQuery} from "@tanstack/react-query";
import {chargingStationListQuery, pricingListQuery} from "./StationListApi";
import {ChargingStation, Pricing} from "../../generated";
import React from "react";
import {WebRoutes} from "../../router/WebRoutes";
import {Table} from "@themesberg/react-bootstrap";
import {useNavigate} from "react-router-dom";
import styles from "./StationListPage.module.scss";

const getSymbolForCurrency = (currency: string) => ({"EUR": "€", "USD": "$"})[currency];

export function StationListPage() {
  const navigate = useNavigate();

  const stationListQuery = useQuery(chargingStationListQuery());
  const pricingQuery = useQuery(pricingListQuery());

  if (stationListQuery.status === "success" && pricingQuery.status === "success") {
    const chargingStationList: (ChargingStation & Pricing)[] = stationListQuery.data.map(station => {
      const pricing = pricingQuery.data.find(s => s.chargingStationId === station.chargingStationId);
      return {
        ...station,
        price: pricing.price,
        percentageOffer: pricing.percentageOffer
      }
    });

    return (
      <div className={styles.pageContainer}>
        <div className={styles.title}>
          Charging station list
        </div>
        <Table>
          <thead style={{backgroundColor: "rgba(1,170,232,0.69)"}}>
          <tr>
            <th className={styles.headerCell}>Charging station</th>
            <th className={styles.headerCell}>Address</th>
            <th className={styles.headerCell}>City</th>
            <th className={styles.headerCell}>Current price</th>
            <th className={styles.headerCell}>Current offer</th>
          </tr>
          </thead>
          <tbody style={{backgroundColor: "#FFF"}}>
          {chargingStationList.map(s => (
            <tr
              key={s.chargingStationId}
              onClick={() => navigate(WebRoutes.Stations.Detail.buildPath(s.chargingStationId))}
              className={styles.row}
            >
              <td className={styles.cell}>
                <span>{s.name}</span>
              </td>
              <td className={styles.cell}>
                <span>{s.address}</span>
              </td>
              <td className={styles.cell}>
                <span>{s.city}</span>
              </td>
              <td className={styles.cell}>
                <span>{`${getSymbolForCurrency(s.price.currency)} ${(s.price.amount).toFixed(2)}`}</span>
              </td>
              <td className={styles.cell}>
                <span>{s.percentageOffer > 0 ? `${s.percentageOffer * 100}%` : "-"}</span>
              </td>
            </tr>
          ))}
          </tbody>
        </Table>
      </div>
    );
  } else {
    return (
      <>
      </>
    );
  }
}