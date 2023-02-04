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

export const getRouter = ({queryClient}: { queryClient: QueryClient }) => createBrowserRouter(createRoutesFromElements(
  <Route path="/" element={<PageRoot/>} errorElement={<ErrorPage/>}>
    <Route path={WebRoutes.Login.pathSchema} element={<LoginPage/>}/>
    <Route
      path={WebRoutes.Stations.List.pathSchema}
      element={<StationListPage/>}
      // TODO
      loader={cachedOrFetchedDataLoader(queryClient, ({params}) => [])}
    />
    <Route
      path={WebRoutes.Stations.Detail.pathSchema}
      element={<StationStatusPage/>}
      // TODO
      loader={cachedOrFetchedDataLoader(queryClient, ({params}) => [])}
    />
  </Route>
));