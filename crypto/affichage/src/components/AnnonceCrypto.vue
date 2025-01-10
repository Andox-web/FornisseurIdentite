<template>
  <div class="annonces-crypto">
    <h2>Annonces de Vente</h2>
    <div class="annonces-list">
      <ul>
        <li v-for="annonce in annonces" :key="annonce.id">
          <div class="annonce-header">
            <strong>{{ annonce.cryptomonnaie }}</strong>
            <span>{{ annonce.prix }}$</span>
          </div>
          <div class="annonce-footer">
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
  padding: 1.5rem;
  background: #1e1e1e;
  color: #f2f2f2;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  max-width: 500px;
  margin: 0 auto;
}

h2 {
  text-align: center;
  font-size: 1.8rem;
  margin-bottom: 20px;
  color: #4caf50;
}

.annonces-list {
  margin-top: 10px;
}

.annonces-list ul {
  list-style: none;
  padding: 0;
}

.annonces-list li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #2a2a2a;
  padding: 15px;
  margin-bottom: 15px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  transition: background 0.3s ease-in-out;
}

.annonces-list li:hover {
  background: #3a3a3a;
}

.annonce-header {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
}

.annonce-header strong {
  font-size: 1.3rem;
  color: #ff9800;
}

.annonce-header span {
  font-size: 1.1rem;
  color: #e3e3e3;
}

.annonce-footer {
  display: flex;
  justify-content: space-between;
  width: 100%;
  align-items: center;
}

.buy-btn {
  background-color: #4caf50;
  color: white;
  border: none;
  padding: 8px 15px;
  border-radius: 20px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.buy-btn:hover {
  background-color: #45a049;
}

.buy-btn:active {
  background-color: #388e3c;
}
</style>
