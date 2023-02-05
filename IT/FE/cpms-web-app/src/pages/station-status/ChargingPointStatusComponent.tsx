import React from "react";
import {ChargingPointStatusDetail} from "./StationStatusPage";
import {Card, ListGroup, ListGroupItem} from "@themesberg/react-bootstrap";
import styles from "./ChargingPointStatusComponent.module.scss";

export function ChargingPointStatusComponent(props: { chargingPoint: ChargingPointStatusDetail }) {

  const {chargingPoint} = props;

  return (
    <Card className={styles.card}>
      <Card.Header className={styles.header}>
        Code {chargingPoint.chargingPointCode}
      </Card.Header>
      <Card.Body className={styles.body}>
        <ListGroup className={styles.listGroup} variant="flush">
          {chargingPoint.socketList.map(s => (
            <ListGroupItem
              key={s.socketId}
              className={styles.listItem}
              style={{backgroundColor: s.status.status === "SocketDeliveringStatus" ? "rgba(1,170,232,0.36)" : "transparent"}}
            >
              <div className={styles.row}>
                <div className={styles.socketCode}>
                  {s.socketCode}
                </div>
                {s.status.status === "SocketDeliveringStatus" && s.status.kWhAbsorbed >=0 && s.status.expectedMinutesLeft >= 0 &&
                  <div className={styles.column}>
                    <div className={styles.label}>
                      {`Power absorbed: ${Math.trunc(s.status.kWhAbsorbed)} kWh`}
                    </div>
                    <div className={styles.label}>
                      {`Minutes left to full charge: ${s.status.expectedMinutesLeft}'`}
                    </div>
                  </div>
                }
                {s.status.status !== "SocketDeliveringStatus" &&
                  <div className={styles.available}>
                    Available
                  </div>
                }
                <div
                  className={styles.socketType}
                >{s.type}</div>
              </div>
            </ListGroupItem>
          ))}
        </ListGroup>
      </Card.Body>
    </Card>
  );
}