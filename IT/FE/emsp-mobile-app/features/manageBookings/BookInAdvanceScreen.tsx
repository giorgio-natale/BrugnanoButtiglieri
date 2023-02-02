import * as React from 'react';
import {StyleSheet, View} from "react-native";
import {BookChargeTabScreenProps} from "../../navigation/types";
import {Button, List, SegmentedButtons, Text as TextPaper} from "react-native-paper";
import MaterialCommunityIcons from "react-native-vector-icons/MaterialCommunityIcons";
import RNDateTimePicker from '@react-native-community/datetimepicker';

export function BookInAdvanceScreen(props: BookChargeTabScreenProps<"BookInAdvance">) {

  return <View style={{flex: 1}}>
    <List.Item
      style={{...styles.viewElement, ...{marginTop: 10}}}
      title="1 Secondary Street, Another Town"
      left={props => <MaterialCommunityIcons {...props} name="map-marker" size={35}/>}
    />
    <View style={{...styles.startDatetimeContainer, ...styles.viewElement}}>
      <TextPaper
        variant="titleMedium"
        style={{marginRight: 10}}
      >
        Start datetime
      </TextPaper>
      <RNDateTimePicker mode="date" value={new Date()} style={{marginRight: 10}} accentColor="rgb(103, 80, 164)" themeVariant={"light"}/>
      <RNDateTimePicker mode="time" value={new Date()} accentColor="rgb(103, 80, 164)" themeVariant={"light"}/>
    </View>
    <View style={{...styles.startDatetimeContainer, ...styles.viewElement}}>
      <TextPaper
        variant="titleMedium"
        style={{marginRight: 10}}
      >
        Charge duration
      </TextPaper>
      <RNDateTimePicker mode="time" value={new Date("2015-03-25")} accentColor="rgb(103, 80, 164)" themeVariant={"light"}/>
    </View>
    <View style={styles.viewElement}>
      <TextPaper
        variant="titleMedium"
        style={{marginBottom: 10}}
      >
        Desired socket type
      </TextPaper>
      <SegmentedButtons
        value={"rapid"}
        style={styles.viewElement}
        onValueChange={() => {
        }}
        buttons={[
          {
            value: 'slow',
            label: 'Slow',
          },
          {
            value: 'fast',
            label: 'Fast',
          },
          {
            value: 'rapid',
            label: 'Rapid'
          },
        ]}
      />
    </View>
    <View style={{marginTop: "auto"}}>
      <Button
        mode="contained"
        style={styles.button}
      >
        Submit booking
      </Button>
    </View>
  </View>
}

const styles = StyleSheet.create({
  startDatetimeContainer: {
    display: "flex",
    flexDirection: "row",
    alignItems: "center"
  },
  viewElement: {
    marginBottom: 30,
    marginLeft: 10,
    marginRight: 10
  },
  button: {
    // height: 50,
    // justifyContent: "center",
    margin: 10,
    marginBottom: 30
  }
});
