import {createBrowserRouter, createRoutesFromElements, Route} from "react-router-dom";
import React from "react";
import {WebRoutes} from "./WebRoutes";
import {QueryClient} from "@tanstack/react-query";
import {cachedOrFetchedDataLoader} from "../api/PageDataLoaders";
import {PageRoot} from "../pages/PageRoot";
import {ErrorPage} from "../pages/error/ErrorPage";
import {LoginPage} from "../pages/login/LoginPage";
import {StationListPage} from "../pages/station-list/StationListPage";
import {StationStatusPage} from "../pages/station-status/StationStatusPage";
import {chargingStationListQuery, pricingListQuery} from "../pages/station-list/StationListApi";
import {chargingStationStatusQuery} from "../pages/station-status/StationStatusApi";

export const getRouter = ({queryClient}: { queryClient: QueryClient }) => createBrowserRouter(createRoutesFromElements(
  <Route path="/" element={<PageRoot/>} errorElement={<ErrorPage/>}>
    <Route path={WebRoutes.Login.pathSchema} element={<LoginPage/>}/>
    <Route
      path={WebRoutes.Stations.List.pathSchema}
      element={<StationListPage/>}
      loader={cachedOrFetchedDataLoader(queryClient,
        () => [chargingStationListQuery(), pricingListQuery()]
      )}
    />
    <Route
      path={WebRoutes.Stations.Detail.pathSchema}
      element={<StationStatusPage/>}
      loader={cachedOrFetchedDataLoader(queryClient,
        ({params}) => [chargingStationListQuery(),
          chargingStationStatusQuery(parseInt(params.stationId))
        ]
      )}
    />
  </Route>
));