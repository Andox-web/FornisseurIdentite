<template>
    <div class="funds">
      <h3>Fonds</h3>
      <div class="form-group">
        <label for="action">Action</label>
        <select v-model="action" id="action">
          <option value="deposit">Dépôt</option>
          <option value="withdrawal">Retrait</option>
        </select>
      </div>
      <div class="form-group">
        <label for="currency">Monnaie</label>
        <select v-model="currency" id="currency">
          <option value="btc">Bitcoin (BTC)</option>
          <option value="eth">Ethereum (ETH)</option>
          <option value="usd">USD</option>
          <option value="eur">Euro</option>
        </select>
      </div>
      <div class="form-group">
        <label for="amount">Montant</label>
        <input v-model="amount" type="number" id="amount" placeholder="Entrez un montant" />
      </div>
      <button @click="submitRequest">{{ action === "deposit" ? "Déposer" : "Retirer" }}</button>
      <p v-if="message" class="success-message">{{ message }}</p>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        action: "deposit",
        currency: "btc",
        amount: "",
        message: "",
      };
    },
    methods: {
      submitRequest() {
        if (!this.amount || this.amount <= 0) {
          alert("Veuillez entrer un montant valide.");
          return;
        }
  
        this.message = `Votre demande de ${this.action === "deposit" ? "dépôt" : "retrait"} de ${this.amount} ${this.currency.toUpperCase()} a été soumise avec succès.`;
        this.amount = "";
      },
    },
  };
  </script>
  
  <style scoped>
  .funds h3 {
    color: #00ff88;
    margin-bottom: 10px;
  }
  
  .form-group {
    margin-bottom: 20px;
  }
  
  label {
    display: block;
    margin-bottom: 5px;
    color: #bbb;
  }
  
  select, input {
    width: 100%;
    padding: 10px;
    border: 2px solid #444;
    border-radius: 4px;
    background-color: #333;
    color: #e4e4e4;
  }
  
  button {
    background-color: #00ff88;
    padding: 10px;
    border: none;
    border-radius: 4px;
    color: #121212;
    cursor: pointer;
    width: 100%;
    transition: background-color 0.3s ease-in-out;
  }
  
  button:hover {
    background-color: #00cc6a;
  }
  
  .success-message {
    margin-top: 10px;
    color: #00ff88;
  }
  </style>
  