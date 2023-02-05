import {Outlet, useNavigate} from "react-router-dom"
import {useEffect} from "react";
import {WebRoutes} from "../router/WebRoutes";
import {clearAuthInfo, getAuthInfo} from "../api/ApiConfig";
import {Button} from "@themesberg/react-bootstrap";
import styles from "./PageRoot.module.scss";
import {useQuery} from "@tanstack/react-query";
import {employeeQuery} from "./login/EmployeeApi";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCircleUser} from "@fortawesome/free-solid-svg-icons/faCircleUser";

export const PageRoot = () => {

  const navigate = useNavigate();

  useEffect(() => {
    if (!getAuthInfo().token)
      navigate(WebRoutes.Login.buildPath());
  }, [])

  return (
    <div className={styles.pageContainer}>
      {getAuthInfo().token &&
        <div className={styles.header}>
          <FontAwesomeIcon icon={faCircleUser} size={"3x"}/>
          <div className={styles.loggedUser}>
            {`${getAuthInfo().employeeName} ${getAuthInfo().employeeSurname}`}
          </div>
          <Button
            style={{marginLeft: "auto"}}
            variant="outline-primary"
            onClick={() => {
            clearAuthInfo();
            navigate(WebRoutes.Login.buildPath());
          }}>
            Logout
          </Button>
        </div>
      }
      <Outlet/>
    </div>
  );
}