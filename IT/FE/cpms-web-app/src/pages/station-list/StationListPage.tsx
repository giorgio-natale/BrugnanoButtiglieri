import {useQuery} from "@tanstack/react-query";
import {chargingStationListQuery, pricingListQuery} from "./StationListApi";
import {ChargingStation, Pricing} from "../../generated";
import React from "react";
import {WebRoutes} from "../../router/WebRoutes";
import {Button} from "@themesberg/react-bootstrap";
import {useNavigate} from "react-router-dom";

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

    console.log(chargingStationList)

    return (
      <div>
        STATION LIST PAGE
        <div>
          {chargingStationList.map(s => (
            <Button
              key={s.chargingStationId}
              onClick={() => navigate(WebRoutes.Stations.Detail.buildPath(s.chargingStationId))}>
              {s.name}
            </Button>
          ))}
        </div>
      </div>
    );
  } else {
    return (
      <>
      </>
    );
  }
}