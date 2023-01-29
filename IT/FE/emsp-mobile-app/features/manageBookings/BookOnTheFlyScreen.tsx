import * as React from 'react';
import {StyleSheet, View} from "react-native";
import {BookChargeTabScreenProps} from "../../navigation/types";
import {Button, List, TextInput} from "react-native-paper";
import MaterialCommunityIcons from "react-native-vector-icons/MaterialCommunityIcons";

export function BookOnTheFlyScreen(props: BookChargeTabScreenProps<"BookInAdvance">) {
  return <View style={{flex: 1}}>
    <List.Item
      style={{...styles.viewElement, ...{marginTop: 10}}}
      title="1 Secondary Street, Another Town"
      left={props => <MaterialCommunityIcons {...props} name="map-marker" size={35}/>}
    />
    <TextInput
      label="Charging point identifier"
      mode="outlined"
      value="A"
      style={styles.viewElement}
    />
    <TextInput
      label="Socket identifier"
      mode="outlined"
      value="10"
      style={styles.viewElement}
    />
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
  viewElement: {
    marginBottom: 30,
    marginLeft: 10,
    marginRight: 10
  },
  button: {
    margin: 10,
    marginBottom: 20
  }
});
