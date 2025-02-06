<template>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div class="col-12">
          <card class="card-body">
            <template slot="header">
              <h4 class="card-title">Créer une annonce de vente</h4>
            </template>
            <div class="form-group">
              <label for="crypto">Cryptomonnaie</label>
              <select v-model="crypto" class="form-control">
                <option value="bitcoin">Bitcoin (BTC)</option>
                <option value="ethereum">Ethereum (ETH)</option>
                <option value="usdt">Tether (USDT)</option>
              </select>
            </div>
            <div class="form-group">
              <label for="quantity">Quantité à vendre</label>
              <input v-model="quantity" type="number" class="form-control" placeholder="Entrez la quantité">
            </div>
            <button @click="submitSale" class="btn btn-primary">Publier l'annonce</button>
          </card>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Card from 'src/components/Cards/Card.vue';

export default {
  components: {
    Card
  },
  data() {
    return {
      crypto: '',
      quantity: '',
      price: ''
    };
  },
  methods: {
    submitSale() {
      alert(`Annonce publiée : Vente de ${this.quantity} ${this.crypto.toUpperCase()}`);
    }
  }
};
</script>

<style>
</style>
