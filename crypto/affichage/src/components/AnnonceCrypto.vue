<template>
    <div class="annonces-crypto">
      <h2>Annonces de Vente</h2>
      <div class="annonces-list">
        <ul>
          <li v-for="annonce in annonces" :key="annonce.id">
            <div>
              <strong>{{ annonce.cryptomonnaie }}</strong>
              <span>{{ annonce.prix }}$</span>
            </div>
            <div>
              <span>Vendeur: {{ annonce.vendeur }}</span>
              <button @click="acheterAnnonce(annonce)" class="buy-btn">Acheter</button>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        annonces: [
          { id: 1, cryptomonnaie: 'Bitcoin', prix: 54000, vendeur: 'Alice' },
          { id: 2, cryptomonnaie: 'Ethereum', prix: 4000, vendeur: 'Bob' },
          { id: 3, cryptomonnaie: 'Litecoin', prix: 150, vendeur: 'Charlie' },
        ],
      };
    },
    methods: {
      acheterAnnonce(annonce) {
        alert(`Vous avez acheté ${annonce.cryptomonnaie} pour ${annonce.prix}$`);
      },
    },
  };
  </script>
  
  <style scoped>
  .annonces-crypto {
    padding: 1rem;
    background: #333;
    color: white;
    border-radius: 8px;
  }
  
  h2 {
    text-align: center;
    font-size: 1.5rem;
  }
  
  .annonces-list {
    margin-top: 20px;
  }
  
  .annonces-list ul {
    list-style: none;
    padding: 0;
  }
  
  .annonces-list li {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: #444;
    padding: 10px;
    margin-bottom: 10px;
    border-radius: 5px;
  }
  
  .buy-btn {
    background-color: #28a745;
    color: white;
    border: none;
    padding: 5px 10px;
    border-radius: 5px;
    cursor: pointer;
  }
  
  .buy-btn:hover {
    background-color: #218838;
  }
  </style>
  