import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Alert, Dimensions, Animated } from 'react-native';
import { LineChart } from 'react-native-chart-kit';
import { ScrollView } from 'react-native-gesture-handler';

const ChartScreen = () => {
  const [favorites, setFavorites] = useState([]);
  const [selectedCrypto, setSelectedCrypto] = useState('BTC');
  const [chartData, setChartData] = useState({
    BTC: {
      labels: ['1H', '6H', '12H', '24H'],
      datasets: [{ data: [45000, 47000, 46500, 48000] }]
    },
    ETH: {
      labels: ['1H', '6H', '12H', '24H'],
      datasets: [{ data: [2500, 2600, 2550, 2700] }]
    },
    XRP: {
      labels: ['1H', '6H', '12H', '24H'],
      datasets: [{ data: [0.5, 0.6, 0.55, 0.65] }]
    }
  });

  const cryptos = ['BTC', 'ETH', 'XRP'];
  const cryptoNames = {
    BTC: 'Bitcoin',
    ETH: 'Ethereum',
    XRP: 'Ripple'
  };

  useEffect(() => {
    const interval = setInterval(() => {
      setChartData(prev => {
        const currentData = prev[selectedCrypto].datasets[0].data;
        const newValue = currentData[3] + (Math.random() * 2000 - 1000);
        const newData = [...currentData.slice(1), newValue > 0 ? newValue : 0];
        return {
          ...prev,
          [selectedCrypto]: {
            ...prev[selectedCrypto],
            datasets: [{ data: newData }]
          }
        };
      });
    }, 5000);

    return () => clearInterval(interval);
  }, [selectedCrypto]);

  const toggleFavorite = (crypto) => {
    setFavorites(prev => 
      prev.includes(crypto) 
        ? prev.filter(c => c !== crypto) 
        : [...prev, crypto]
    );
  };

  const simulateOperation = () => {
    if (favorites.includes(selectedCrypto)) {
      Alert.alert(
        `Mouvement sur ${selectedCrypto}!`,
        "Achat important détecté (+4.5%)"
      );
    }
  };

  return (
    <ScrollView>
      <View style={styles.container}>
        <Text style={styles.title}>BULL X NEO</Text>
        
        {/* Buttons for selecting crypto */}
        <View style={styles.cryptoButtonsContainer}>
          {cryptos.map((crypto) => (
            <TouchableOpacity 
              key={crypto} 
              onPress={() => setSelectedCrypto(crypto)}
              style={[styles.cryptoButton, selectedCrypto === crypto && styles.selectedButton]}>
              <Text style={styles.buttonText}>{cryptoNames[crypto]} ({crypto})</Text>
            </TouchableOpacity>
          ))}
        </View>

        <LineChart
          data={chartData[selectedCrypto]}
          width={400}
          height={400}
          chartConfig={chartConfig}
          bezier
          style={styles.chart}
        />

        <View style={styles.row}>
          <Text style={styles.cryptoName}>
            {cryptoNames[selectedCrypto]} ({selectedCrypto})
          </Text>

          {/* Favorite button with animation */}
          <TouchableOpacity 
            onPress={() => toggleFavorite(selectedCrypto)}
            style={[styles.favButton, favorites.includes(selectedCrypto) && styles.favButtonActive]}>
            <Text style={[styles.favText, favorites.includes(selectedCrypto) && styles.favTextActive]}>
              ♥
            </Text>
          </TouchableOpacity>

        </View>


        {/* Esory refa mety le back end notif fa test affichage fotsiny ito */}
        <TouchableOpacity 
          onPress={simulateOperation}
          style={styles.simulateButton}>
          <Text style={[styles.buttonText,{color:'#000'}]}>Simuler une opération</Text>
        </TouchableOpacity>
      </View>
    </ScrollView>
  );
};

const chartConfig = {
  backgroundColor: '#1a1a1a',
  backgroundGradientFrom: '#1a1a1a',
  backgroundGradientTo: '#1a1a1a',
  decimalPlaces: 0,
  color: (opacity = 1) => `rgba(0, 255, 100, ${opacity})`,
  labelColor: (opacity = 1) => `rgba(255, 255, 255, ${opacity})`,
  style: {
    borderRadius: 16,
  },
  propsForDots: {
    r: '4',
    strokeWidth: '2',
    stroke: '#9fd50c'
  }
};

const styles = StyleSheet.create({
  container: {
    backgroundColor: '#161617',
    width: Dimensions.get('window').width,
    height: Dimensions.get('window').height,
    flex: 1,
    backgroundColor: '#0a0a0a',
    alignItems: 'center',
    padding: 20,
  },
  title: {
    color: '#9fd50c',
    fontSize: 24,
    fontWeight: 'bold',
    marginVertical: 20,
    fontFamily: 'Roboto-Bold',
  },
  cryptoButtonsContainer: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    width: '100%',
    marginBottom: 20,
  },
  cryptoButton: {
    padding: 10,
    backgroundColor: '#1a1a1a',
    borderRadius: 8,
    borderWidth: 2,
    borderColor: '#9fd50c',
  },
  selectedButton: {
    backgroundColor: '#9fd50c44',
  },
  buttonText: {
    color: '#fff',
    fontSize: 16,
  },
  chart: {
    borderRadius: 16,
    marginVertical: 20,
  },
  row: {
    flexDirection: 'row',
    alignItems: 'center',
    marginVertical: 15,
  },
  cryptoName: {
    color: '#fff',
    fontSize: 18,
    marginRight: 15,
  },
  favButton: {
    padding: 10,
    borderRadius: 20,
    borderWidth: 2,
    borderColor: '#9fd50c',
  },
  favButtonActive: {
    backgroundColor: '#9fd50c44',
  },
  favText: {
    color: '#9fd50c',
    fontSize: 18,
  },
  favTextActive: {
    transform: [{ scale: 1.5 }],
    color: '#ff0000', // Change color when active
  },
  simulateButton: {
    backgroundColor: '#9fd50c',
    padding: 15,
    borderRadius: 10,
    marginTop: 30,
  },
});

export default ChartScreen;
