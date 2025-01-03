<template>
    <div class="wallets">
      <h3>Portefeuille</h3>
      <table>
        <thead>
          <tr>
            <th>Monnaie</th>
            <th>Quantité</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="wallet in wallets" :key="wallet.id">
            <td>{{ wallet.currency }}</td>
            <td>{{ wallet.amount }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        wallets: [
          { id: 1, currency: "Bitcoin (BTC)", amount: "0.25" },
          { id: 2, currency: "Ethereum (ETH)", amount: "1.5" },
          { id: 3, currency: "USD", amount: "500.00" },
          { id: 4, currency: "Euro", amount: "300.00" },
        ],
      };
    },
  };
  </script>
  
  <style scoped>
  .wallets h3 {
    color: #00ff88;
    margin-bottom: 10px;
  }
  
  table {
    width: 100%;
    border-collapse: collapse;
  }
  
  th, td {
    padding: 10px;
    text-align: left;
    border-bottom: 1px solid #444;
    color: #e4e4e4;
  }
  
  thead th {
    background-color: #333;
  }
  
  tbody tr:hover {
    background-color: #444;
  }
  </style>
  