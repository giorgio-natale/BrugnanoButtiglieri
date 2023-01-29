import * as React from "react";
import {View} from "react-native";
import {Button, List} from 'react-native-paper';
import {StationsStackScreenProps} from "../../navigation/types";

export function FindStationScreen({navigation}: StationsStackScreenProps<"FindStation">) {
  return (
    <View>
      <List.Item
        title={`Modern Station\n0.45 €/kWh (10% discount)`}
        titleNumberOfLines={2}
        titleStyle={{fontWeight: "600"}}
        description={`1 Secondary Street, Another Town`}
        descriptionNumberOfLines={2}
        right={() => <View style={{alignSelf: "center"}}>
          <Button mode="contained" onPress={() => {
            navigation.navigate("BookCharge")
          }} labelStyle={{fontSize: 17}}>
            Book
          </Button>
        </View>}
      />
    </View>
  );
}