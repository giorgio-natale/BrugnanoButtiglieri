import * as React from 'react';
import {ScrollView, StyleSheet, View} from "react-native";
import {Button, Divider, Text} from 'react-native-paper';
import {MainStackScreenProps} from "../../navigation/types";
import {allBookingsQuery} from "./BookingApi";
import {useQuery} from "@tanstack/react-query";

export function BookingsScreen(props: MainStackScreenProps<"Bookings">) {

  // const bookingList = useQuery(allBookingsQuery(102)).data;

  return <ScrollView>
    <Text variant="titleLarge" style={styles.pageTitle}>
      Your bookings
    </Text>
    <Divider/>
    <View style={{...styles.item, ...{backgroundColor: "rgba(255,255,255)"}}}>
      <View>
        <Text style={styles.itemTitle}>
          Booking #AB1234
        </Text>
        <Text style={styles.itemSubtitle}>
          30/01/23, 2:00 pm (2:00 h)
        </Text>
        <Text style={styles.description}>
          Socket A-4
        </Text>
        <Text style={styles.description}>
          12 Main Street, Town
        </Text>
      </View>
      <View style={{alignSelf: "center"}}>
        <Button mode={"contained-tonal"} onPress={() => {
          alert("deleted booking")
        }} labelStyle={{fontSize: 15}}>
          Delete
        </Button>
      </View>
    </View>
    <Divider/>
    <View style={{...styles.item, ...{backgroundColor: "rgba(172,146,225,0.5)"}}}>
      <View>
        <Text style={styles.itemTitle}>
          Booking #CD5678
        </Text>
        <Text style={styles.itemSubtitle}>
          09/01/23, 11:00 am (0:30 h)
        </Text>
        <Text style={styles.description}>
          Socket A-10
        </Text>
        <Text style={styles.description}>
          12 Main Street, Town
        </Text>
      </View>
      <View style={{alignSelf: "center"}}>
        <Button mode="contained" onPress={() => {
          alert("start charge")
        }} labelStyle={{fontSize: 17}}>
          Charge
        </Button>
      </View>
    </View>
    <Divider/>
    <View style={{...styles.item, ...{backgroundColor: "rgba(229,227,227,0.5)"}}}>
      <View>
        <Text style={styles.itemTitle}>
          Booking #EF9012
        </Text>
        <Text style={styles.itemSubtitle}>
          11/12/22, 8:00 pm
        </Text>
        <Text style={styles.description}>
          Socket B-4
        </Text>
        <Text style={styles.description}>
          1 Secondary Street, Another Town
        </Text>
      </View>
      <View>
        <Text variant="labelLarge">
          Completed
        </Text>
      </View>
    </View>
    <Divider/>
    <View style={{...styles.item, ...{backgroundColor: "rgba(229,227,227,0.5)"}}}>
      <View>
        <Text style={styles.itemTitle}>
          Booking #GH1234
        </Text>
        <Text style={styles.itemSubtitle}>
          10/12/22, 4:10 pm (0:30 h)
        </Text>
        <Text style={styles.description}>
          Socket A-9
        </Text>
        <Text style={styles.description}>
          12 Main Street, Town
        </Text>
      </View>
      <View>
        <Text variant="labelLarge">
          Expired
        </Text>
      </View>
    </View>
    <Divider/>
    <View style={{...styles.item, ...{backgroundColor: "rgba(229,227,227,0.5)"}}}>
      <View>
        <Text style={styles.itemTitle}>
          Booking #IL5678
        </Text>
        <Text style={styles.itemSubtitle}>
          01/11/22, 4:20 pm (3:15 h)
        </Text>
        <Text style={styles.description}>
          Socket A-9
        </Text>
        <Text style={styles.description}>
          12 Main Street, Town
        </Text>
      </View>
      <View>
        <Text variant="labelLarge">
          Cancelled
        </Text>
      </View>
    </View>
    <Divider/>
  </ScrollView>
}

const styles = StyleSheet.create({
  pageTitle: {
    padding: 18,
    paddingTop: 25,
    paddingBottom: 10
  },
  item: {
    padding: 10,
    paddingLeft: 25,
    display: "flex",
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "flex-start"
  },
  itemTitle: {
    fontWeight: "600",
    fontSize: 18
  },
  itemSubtitle: {
    fontSize: 16
  },
  description: {
    fontSize: 14
  }
})