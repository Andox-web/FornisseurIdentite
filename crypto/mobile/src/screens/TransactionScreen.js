import React, { useState } from 'react';
import {
  StyleSheet,
  Dimensions,
  SafeAreaView,
  ScrollView,
  View,
  TouchableOpacity,
  TextInput,
  Text,
  Image,
} from 'react-native';

const CRYPTO_PORTFOLIO = {
  current: [
    { devis: 'USD', quantity: 45000 }
  ],
  history: [
    { type: 'Depot', devis: 'USD', quantity: 130000},
    { type: 'Retrait', devis: 'USD', quantity: 55000 },
  ],
};

export default function Transaction() {
  const [transactionType, setTransactionType] = useState('');
  const [quantity, setQuantity] = useState('');

  const handleRequest = () => {
    // Placeholder for API request to handle transaction
    alert(`Transaction Request: Type - ${transactionType}, Quantité - ${quantity}`);
  };

  return (
    <SafeAreaView style={{ flex: 1, backgroundColor: '#fff' }}>
      <ScrollView style={{ overflow: 'hidden', width: Dimensions.get('window').width }}>
        <View style={styles.list}>
          <View style={styles.listHeader}>
            <Text style={[styles.cardTitle, { textAlign: 'center', fontWeight: '800', fontSize: 25, marginTop: 5 ,borderBottomWidth: 0 ,color:"#000"}]}>
              Faire une Transaction
            </Text>
          </View>

          {/* Transaction Request Form */}
          <View style={[styles.card, { gap: 40, borderWidth: 2, borderColor: '#000' }]}>
            <Text style={[styles.cardTitle,{borderBottomColor:'#9fd50c'}]}>Type de Transaction</Text>
            <TouchableOpacity
              style={[styles.btn, transactionType === 'Depot' && { backgroundColor: '#d1f0db', borderColor: '#d1f0db' }]}
              onPress={() => setTransactionType('Depot')}>
              <Text style={styles.btnText}>Depot</Text>
            </TouchableOpacity>
            <TouchableOpacity
              style={[styles.btn, transactionType === 'Retrait' && { backgroundColor: '#f4c7c3', borderColor: '#f4c7c3' }]}
              onPress={() => setTransactionType('Retrait')}>
              <Text style={styles.btnText}>Retrait</Text>
            </TouchableOpacity>

            <Text style={[styles.cardTitle,{borderBottomColor:'#9fd50c'}]}>Quantité</Text>
            <TextInput
              style={[styles.input, { height: 50 }]}
              placeholder="Quantité"
              keyboardType="numeric"
              value={quantity}
              onChangeText={setQuantity}
            />

            <TouchableOpacity
              style={styles.btnPrimary}
              onPress={handleRequest}>
              <Text style={styles.btnPrimaryText}>Demander</Text>
            </TouchableOpacity>
          </View>

          <View style={styles.card}>
            <Text style={[styles.cardTitle,{borderBottomColor:'#9fd50c'}]}>Portefeuille Crypto</Text>
            {CRYPTO_PORTFOLIO.current.map((item, index) => (
              <View key={index} style={[styles.transactionItem, { backgroundColor: '#f1f6fe' }]}>
                <Text style={{ fontWeight: '700', fontSize: 15 }}>{item.devis}</Text>
                <View style={{ alignItems: 'flex-end' }}>
                  <Text style={styles.cryptoQuantity}>{item.quantity}</Text>
                </View>
              </View>
            ))}
          </View>

          {/* Transaction History */}
          <View style={styles.card}>
            <Text style={[styles.cardTitle,{borderBottomColor:'#9fd50c'}]}>Historique des Transactions</Text>
            {CRYPTO_PORTFOLIO.history.map((item, index) => (
              <View
                key={index}
                style={[
                  styles.transactionItem,
                  item.type === 'Depot' ? styles.transactionTypeDepot : styles.transactionTypeRetrait,
                ]}>
                <View style={{ flex: 1 }}>
                  <Text style={[styles.typeText, item.type === 'Depot' ? styles.DepotText : styles.RetraitText]}>
                    {item.type}
                  </Text>
                  <Text style={{ color: '#666', marginTop: 4 }}>{item.devis}</Text>
                </View>
                <View style={{ alignItems: 'flex-end' }}>
                  <Text style={styles.cryptoQuantity}>{item.quantity}</Text>
                </View>
              </View>
            ))}
          </View>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  SafeAreaView: {
    width: Dimensions.get('window').width,
  },
  /** Header */
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'flex-start',
    paddingVertical: 8,
    paddingHorizontal: 12,
    borderBottomWidth: 1,
    borderColor: '#e3e3e3',
  },
  headerAction: {
    width: 40,
    alignItems: 'flex-start',
    justifyContent: 'center',
  },
  /** Content */
  content: {
    paddingTop: 12,
    paddingHorizontal: 24,
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
    fontSize: 28,
    fontWeight: 'bold',
    lineHeight: 32,
    color: '#121a26',
    marginBottom: 6,
  },
  profileDescription: {
    fontSize: 14,
    fontWeight: '500',
    lineHeight: 18,
    color: '#778599',
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
  /** Button */
  btn: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 8,
    paddingVertical: 8,
    paddingHorizontal: 16,
    borderWidth: 2,
    backgroundColor: '#fff',
    borderColor: '#fff',
  },
  btnText: {
    fontSize: 14,
    lineHeight: 20,
    fontWeight: '700',
    color: '#000',
  },
  btnPrimary: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 8,
    paddingVertical: 8,
    paddingHorizontal: 16,
    borderWidth: 2,
    backgroundColor: '#9fd50c',
    borderColor: '#9fd50c',
  },
  btnPrimaryText: {
    fontSize: 14,
    lineHeight: 20,
    fontWeight: '700',
    color: '#fff',
  },
  /** Card */
  card: {
    width: Dimensions.get('window').width - 20, // Responsive width
    paddingVertical: 20,
    paddingHorizontal: 24,
    borderRadius: 16,
    backgroundColor: '#fff',
    marginHorizontal: 6,
    marginBottom: 16, // Espace entre les cartes
    shadowColor: '#161617',
    backgroundColor: '#161617',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.1,
    shadowRadius: 6,
    elevation: 3,
  },
  cardTitle: {
    fontSize: 18,
    fontWeight: '700',
    color: '#fff',
    marginBottom: 12,
    borderBottomWidth: 2,
    borderBottomColor: '#266EF1',
    paddingBottom: 8,
  },
  cardText: {
    fontSize: 15,
    color: '#fff',
    marginBottom: 10,
    paddingVertical: 8,
    paddingHorizontal: 12,
    backgroundColor: '#f8f9fa',
    borderRadius: 8,
  },
  transactionItem: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 8,
    padding: 12,
    borderRadius: 8,
  },
  transactionTypeDepot: {
    backgroundColor: '#e6f4ea',
  },
  transactionTypeRetrait: {
    backgroundColor: '#fce8e6',
  },
  typeText: {
    fontWeight: '700',
    fontSize: 13,
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 4,
  },
  DepotText: {
    color: '#137333',
    backgroundColor: '#d1f0db',
  },
  RetraitText: {
    color: '#a50e0e',
    backgroundColor: '#f4c7c3',
  },
  cryptoQuantity: {
    fontWeight: '600',
    color: '#1a1a1a',
  },
  input: {
    height: 50,
    borderWidth: 2,
    borderColor: '#e3e3e3',
    borderRadius: 12,
    padding: 12,
    fontSize: 16,
    fontWeight: '600',
    color: '#1a1a1a',
    backgroundColor: '#fff',
  },
});
