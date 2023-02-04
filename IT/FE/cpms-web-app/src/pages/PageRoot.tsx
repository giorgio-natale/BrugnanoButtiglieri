import {Outlet, useNavigate} from "react-router-dom"
import {useEffect} from "react";
import {WebRoutes} from "../router/WebRoutes";
import {getAuthInfo} from "../api/ApiConfig";

export const PageRoot = () => {

  const navigate = useNavigate();

  useEffect(() => {
    if (getAuthInfo().token)
      navigate(WebRoutes.Stations.List.buildPath());
    else
      navigate(WebRoutes.Login.buildPath());
  }, [])

  return (
    <div className="page-centered">
      <Outlet/>
    </div>
  );
}