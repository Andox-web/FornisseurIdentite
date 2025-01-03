<template>
    <div class="transaction-dashboard">
      <!-- Entête -->
      <h1>Historique des Transactions</h1>
  
      <!-- Liste des transactions -->
      <div v-if="transactions.length" class="transaction-list">
        <div class="transaction-item" v-for="(transaction, index) in transactions" :key="index">
          <div class="transaction-details">
            <span class="transaction-type" :class="{'buy': transaction.type === 'achat', 'sell': transaction.type === 'vente'}">
              {{ transaction.type === 'achat' ? 'Achat' : 'Vente' }}
            </span>
            <span class="transaction-date">{{ formatDate(transaction.date) }}</span>
          </div>
          <div class="transaction-amount">
            <span>{{ transaction.amount }} BTC</span>
            <span class="transaction-price">{{ transaction.price }} €</span>
          </div>
        </div>
      </div>
      <!-- Message si aucune transaction -->
      <p v-else class="no-transactions">Aucune transaction enregistrée.</p>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        // Exemple de données de transactions
        transactions: [
          { type: 'achat', date: '2024-01-01', amount: 0.5, price: 20000 },
          { type: 'vente', date: '2024-01-02', amount: 0.3, price: 21000 },
          { type: 'achat', date: '2024-01-03', amount: 1.2, price: 19000 },
        ],
      };
    },
    methods: {
      formatDate(date) {
        const options = { year: 'numeric', month: 'short', day: 'numeric' };
        return new Date(date).toLocaleDateString('fr-FR', options);
      },
    },
  };
  </script>
  
  <style scoped>
  .transaction-dashboard {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 2rem;
    background: #1c1c1c;
    border-radius: 8px;
    width: 100%;
    height: 100%;
    color: white;
    box-sizing: border-box;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
  }
  
  h1 {
    font-size: 2.2rem;
    font-weight: 600;
    margin-bottom: 20px;
    color: #f39c12;
  }
  
  .transaction-list {
    width: 100%;
    max-width: 800px;
    margin-top: 30px;
    padding: 1rem;
    background: #333;
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
  }
  
  .transaction-item {
    display: flex;
    justify-content: space-between;
    padding: 18px;
    border-bottom: 1px solid #444;
    transition: transform 0.3s ease-in-out, background-color 0.3s ease;
  }
  
  .transaction-item:last-child {
    border-bottom: none;
  }
  
  .transaction-item:hover {
    transform: scale(1.02);
    background-color: #444;
  }
  
  .transaction-details {
    display: flex;
    flex-direction: column;
    justify-content: center;
  }
  
  .transaction-type {
    font-weight: 700;
    font-size: 1.1rem;
    color: #f39c12; /* Jaune pour les achats */
  }
  
  .transaction-type.buy {
    color: #2ecc71; /* Vert pour les achats */
  }
  
  .transaction-type.sell {
    color: #e74c3c; /* Rouge pour les ventes */
  }
  
  .transaction-date {
    font-size: 0.9rem;
    color: #aaa;
  }
  
  .transaction-amount {
    display: flex;
    flex-direction: column;
    justify-content: center;
    text-align: right;
  }
  
  .transaction-amount span {
    display: block;
    font-size: 1.1rem;
  }
  
  .transaction-price {
    font-size: 1.2rem;
    font-weight: bold;
    color: #3498db;
  }
  
  .no-transactions {
    text-align: center;
    color: #aaa;
    font-size: 1.1rem;
    margin-top: 20px;
  }
  
  .transaction-list::-webkit-scrollbar {
    width: 8px;
  }
  
  .transaction-list::-webkit-scrollbar-thumb {
    background-color: #555;
    border-radius: 8px;
  }
  
  .transaction-list::-webkit-scrollbar-thumb:hover {
    background-color: #777;
  }
  
  @media (max-width: 768px) {
    .transaction-dashboard {
      padding: 1rem;
    }
  
    h1 {
      font-size: 1.6rem;
    }
  
    .transaction-list {
      padding: 0.5rem;
    }
  
    .transaction-item {
      padding: 10px;
    }
  
    .transaction-amount span {
      font-size: 1rem;
    }
  
    .transaction-price {
      font-size: 1rem;
    }
  }
  </style>
  