import React, { useState } from "react";
import { View, Text, TouchableOpacity, StyleSheet, Alert, Image } from "react-native";
import { launchImageLibrary } from 'react-native-image-picker';
import Icon from 'react-native-vector-icons/FontAwesome'; // Importer l'icône
import ComponentA from "./TransactionScreen";
import ComponentB from "./ChartScreen";

const Acceuil = () => {
  const [selectedComponent, setSelectedComponent] = useState("A");
  const [profileImage, setProfileImage] = useState(
    'https://images.unsplash.com/photo-1633332755192-727a05c4013d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=facearea&facepad=2.5&w=256&h=256&q=80'
  );

  const handleImageChange = () => {
    launchImageLibrary(
      { mediaType: 'photo', quality: 1, maxWidth: 500, maxHeight: 500 },
      (response) => {
        if (response.assets && response.assets.length > 0) {
          setProfileImage(response.assets[0].uri);
        } else {
          Alert.alert("Image", "No image selected.");
        }
      }
    );
  };

  const handleLogout = () => {
    // Ajoutez ici la logique de déconnexion
    Alert.alert("Déconnexion", "Vous avez été déconnecté.");
  };

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <View style={styles.profile}>
          <View style={styles.profileTop}>
            <View style={styles.avatar}>
              <Image
                alt=""
                source={{ uri: profileImage }}
                style={styles.avatarImg}
              />
              <TouchableOpacity
                style={styles.uploadButton}
                onPress={handleImageChange}
              >
                <Text style={styles.uploadText}>+</Text>
              </TouchableOpacity>
            </View>

            <View style={styles.profileBody}>
              <Text style={styles.profileTitle}>{'Nom utilisateur'}</Text>
              <Text style={styles.profileDescription}>
                Description de l'utilisateur
              </Text>
            </View>

            {/* Bouton de déconnexion */}
            <TouchableOpacity
              style={styles.logoutButton}
              onPress={handleLogout}
            >
              <Icon name="sign-out" size={24} color="#fff" />
            </TouchableOpacity>
          </View>
        </View>
      </View>

      {/* Affichage du composant actif */}
      <View style={styles.content}>
        {selectedComponent === "A" ? <ComponentA /> : <ComponentB />}
      </View>

      {/* Footer Navbar */}
      <View style={styles.navbar}>
        <TouchableOpacity
          style={[
            styles.navButton,
            selectedComponent === "A" ? styles.activeButton : null,
          ]}
          onPress={() => setSelectedComponent("A")}
        >
          <Text style={styles.navText}>Transaction</Text>
        </TouchableOpacity>

        <TouchableOpacity
          style={[
            styles.navButton,
            selectedComponent === "B" ? styles.activeButton : null,
          ]}
          onPress={() => setSelectedComponent("B")}
        >
          <Text style={styles.navText}>Cours</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  /** Content */
  header: {
    backgroundColor: '#161617',
    color: '#fff',
    fontSize: 20,
    paddingTop: 5,
    paddingHorizontal: 2,
    height: 100,
  },
  /** Profile */
  profile: {
    paddingTop: 4,
    paddingBottom: 16,
  },
  profileTop: {
    flexDirection: 'row',
    alignItems: 'flex-start',
    justifyContent: 'space-between',
    marginBottom: 16,
  },
  profileBody: {
    flexGrow: 1,
    flexShrink: 1,
    flexBasis: 0,
    paddingLeft: 16,
  },
  profileTitle: {
    fontSize: 20,
    fontWeight: 'bold',
    lineHeight: 32,
    color: '#fff',
    marginBottom: 1,
  },
  profileDescription: {
    fontSize: 10,
    fontWeight: '500',
    lineHeight: 18,
    color: '#fff',
  },
  avatarNotification: {
    position: 'absolute',
    borderRadius: 9999,
    borderWidth: 2,
    borderColor: '#fff',
    bottom: 0,
    right: -2,
    width: 21,
    height: 21,
    backgroundColor: '#22C55E',
  },
  /** Avatar */
  avatar: {
    position: 'relative',
  },
  avatarImg: {
    width: 80,
    height: 80,
    borderRadius: 9999,
  },
  uploadButton: {
    position: 'absolute',
    bottom: 0,
    right: 0,
    width: 25,
    height: 25,
    borderRadius: 12.5,
    backgroundColor: '#ffffff',
    justifyContent: 'center',
    alignItems: 'center',
    borderWidth: 2,
    borderColor: '#fff',
  },
  uploadText: {
    fontSize: 18,
    color: '#161617',
    fontWeight: 'bold',
  },
  /** Logout Button */
  logoutButton: {
    marginRight: 10,
    justifyContent: 'center',
    alignItems: 'center',
  },
  container: { flex: 1 },
  content: { flex: 1, justifyContent: "center", alignItems: "center" },
  navbar: {
    flexDirection: "row",
    justifyContent: "space-around",
    backgroundColor: "#333",
    paddingVertical: 10,
  },
  navButton: {
    flex: 1,
    alignItems: "center",
    padding: 10,
  },
  activeButton: {
    backgroundColor: "#555",
  },
  navText: {
    color: "#fff",
    fontSize: 16,
  },
});

export default Acceuil;