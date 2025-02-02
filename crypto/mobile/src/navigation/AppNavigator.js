import React from "react";
import { createStackNavigator } from "@react-navigation/stack";
import LoginScreen from "../screens/LoginScreen";
import TransactionScreen from "../screens/TransactionScreen";
import AcceuilScreen from "../screens/AcceuilScreen";



const Stack = createStackNavigator();

export default function AppNavigator() {
  return (
    <Stack.Navigator initialRouteName="Login">
      <Stack.Screen name="Login" component={LoginScreen} options={{ headerShown: false }} />
      <Stack.Screen name="Transaction" component={TransactionScreen} options={{ title: "Transaction" }} />
      <Stack.Screen name="Acceuil" component={AcceuilScreen} options={{ title: "Acceuil" }} />
    </Stack.Navigator>
  );
}
