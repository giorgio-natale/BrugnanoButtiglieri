import React from "react";
import {Button} from '@themesberg/react-bootstrap';
import styles from "./ErrorPage.module.scss";
import {useNavigate} from 'react-router-dom';
import {WebRoutes} from "../../router/WebRoutes";
import {clearAuthInfo} from "../../api/ApiConfig";

export const ErrorPage = () => {
  const navigate = useNavigate();

  return (
    <div className={styles.container}>
      <div className={styles.innerContainer}>
        <h1 className="mt-5">
          An <span style={{color: "red", fontSize: "inherit", fontWeight: "bold"}}>error</span> occured
        </h1>
        <p className="lead">
          Oops! Apparently something went wrong.
        </p>
        <Button
          variant="primary"
          onClick={() => navigate(WebRoutes.Stations.List.buildPath())}
        >
          Go to the Charging Station list page
        </Button>
        <div className={styles.setupRow}>
          <div>or</div>
          <Button
            variant="outline-primary"
            onClick={() => {
              clearAuthInfo();
              navigate(WebRoutes.Login.buildPath());
            }}
          >
            do logout
          </Button>
        </div>
      </div>
    </div>
  );
};
