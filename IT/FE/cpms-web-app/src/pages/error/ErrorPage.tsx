import React from "react";
import {Button} from '@themesberg/react-bootstrap';
import styles from "./ErrorPage.module.scss";
import {useNavigate} from 'react-router-dom';
import {WebRoutes} from "../../router/WebRoutes";
import {clearAuthInfo} from "../../api/ApiConfig";

export const ErrorPage = () => {
  const navigate = useNavigate();

  return (
    <div className="page-centered">
      <div style={{display: "flex", flexDirection: "column", alignItems: "center"}}>
        <h1 className="mt-5">
          An <span style={{color: "red", fontSize: "inherit", fontWeight: "bold"}}>error</span> occured
        </h1>
        <p className="lead">
          Oops! Apparently something went wrong.
        </p>
        <Button
          className="stdButton mt-4"
          onClick={() => navigate(WebRoutes.Stations.List.buildPath())}
        >
          Go to the Charging Station list page
        </Button>
        <div className={styles.setupRow}>
          <div className={styles.text}> or</div>
          <Button
            className={styles.secondaryBtn}
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
