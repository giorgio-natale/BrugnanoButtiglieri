import * as React from "react";
import {useEffect, useRef, useState} from "react";
import {StyleSheet, View} from "react-native";
import {StationsStackScreenProps} from "../../navigation/types";
import MapView, {Marker, PROVIDER_GOOGLE} from 'react-native-maps'
import * as Location from 'expo-location';
import {LocationAccuracy} from 'expo-location';
import {Button, List} from "react-native-paper";
import {allStationsQuery} from "./StationApi";
import {useQuery} from "@tanstack/react-query";
import mapStyle from "../../assets/mapStyle.json";
import {ChargingStationOverview} from "../../generated";

const getSymbolForCurrency = (currency: string) => ({"EUR": "€", "USD": "$"})[currency];

function formatPriceToTwoDecimals(num: number): string {
  return Math.round(((num * 1000) / 10) / 100).toFixed(2);
}

export function FindStationScreen({navigation}: StationsStackScreenProps<"FindStation">) {

  const DEFAULT_MAP_INITIAL_COORDINATES = {
    latitude: 45.477838,
    longitude: 9.227884
  };

  const {status, data: stationList} = useQuery(allStationsQuery());

  const mapViewRef = useRef<MapView>();

  const [selectedChargingStationId, setSelectedChargingStationId] = useState<number | null>(null);
  const isChargingStationSelected = selectedChargingStationId !== null;
  const selectedChargingStation = isChargingStationSelected ? stationList?.find(s => s.chargingStationId === selectedChargingStationId) : null;

  const selectedChargingStationIcon = require("../../assets/images/MarkerActive.png");
  const defaultChargingStationIcon = require("../../assets/images/Marker.png");

  useEffect(() => {
    if (mapViewRef.current)
      Location.requestForegroundPermissionsAsync().then(s => {
        if (s.status === "granted") {
          return Location.getCurrentPositionAsync({accuracy: LocationAccuracy.Low}).then(r => {
            return {
              latitude: r.coords.latitude,
              longitude: r.coords.longitude
            }
          })
        } else {
          return DEFAULT_MAP_INITIAL_COORDINATES;
        }
      }).then(coords => mapViewRef.current.animateToRegion({
          ...coords,
          latitudeDelta: 0.1,
          longitudeDelta: 0.1
        })
      )
  }, [mapViewRef]);

  return (
    <View style={styles.container}>
      <MapView
        ref={mapViewRef}
        style={styles.map}
        provider={PROVIDER_GOOGLE}
        showsUserLocation={true}
        showsMyLocationButton={true}
        customMapStyle={mapStyle}
        onPress={() => setSelectedChargingStationId(null)}
      >
        {status === "success" && stationList.map(s => (
          <Marker key={s.chargingStationId} coordinate={{latitude: s.latitude, longitude: s.longitude}}
                  image={selectedChargingStationId === s.chargingStationId ? selectedChargingStationIcon : defaultChargingStationIcon}
                  onPress={(m) => {
                    m.stopPropagation();
                    setSelectedChargingStationId(s.chargingStationId);
                    mapViewRef.current.animateToRegion({
                        latitude: s.latitude,
                        longitude: s.longitude,
                        latitudeDelta: 0.05,
                        longitudeDelta: 0.05
                      }
                    )
                  }}
          />
        ))}
      </MapView>
      {isChargingStationSelected &&
        <List.Item
          title={getChargingStationOverviewTitle(selectedChargingStation)}
          titleNumberOfLines={2}
          titleStyle={{fontWeight: "600"}}
          description={`${selectedChargingStation.address}, ${selectedChargingStation.city}`}
          right={() => <View style={{alignSelf: "center"}}>
            <Button mode="contained" onPress={() => {
              navigation.navigate("BookCharge")
            }} labelStyle={{fontSize: 17}}>
              Book
            </Button>
          </View>}
        />
      }
    </View>
  );

  function getChargingStationOverviewTitle(selectedChargingStation: ChargingStationOverview) {
    return `${selectedChargingStation.name}\n${formatPriceToTwoDecimals(selectedChargingStation.pricePerKWh.amount)} ${getSymbolForCurrency(selectedChargingStation.pricePerKWh.currency)}/kWh${selectedChargingStation.offerPercentage !== 0 ? ` (${selectedChargingStation.offerPercentage * 100}% discount)` : ""}`;
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1
  },
  map: {
    width: '100%',
    flex: 1
  },
});