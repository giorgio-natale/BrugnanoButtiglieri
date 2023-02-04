import {Outlet, useNavigate} from "react-router-dom"
import {useEffect} from "react";
import {WebRoutes} from "../router/WebRoutes";
import {clearAuthInfo, getAuthInfo} from "../api/ApiConfig";
import {Button} from "@themesberg/react-bootstrap";

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
      {getAuthInfo().token &&
        <Button onClick={() => {
          clearAuthInfo();
          navigate(WebRoutes.Login.buildPath());
        }}>
          Logout
        </Button>
      }
      <Outlet/>
    </div>
  );
}