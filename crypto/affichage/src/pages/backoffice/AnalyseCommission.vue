<template>
    <div class="content">
      <div class="container-fluid">
        <div class="row">
          <div class="col-12">
            <card class="card-body">
              <template slot="header">
                <h4 class="card-title">Analyser les commissions</h4>
              </template>
  
              <!-- Zone de liste pour le type d'analyse -->
              <div class="form-group">
                <label for="typeAnalyse">Type d'analyse</label>
                <select v-model="typeAnalyse" class="form-control">
                  <option value="somme">Somme</option>
                  <option value="moyenne">Moyenne</option>
                </select>
              </div>
  
              <!-- Zone de liste pour la sélection des cryptomonnaies -->
              <div class="form-group">
                <label for="crypto">Cryptomonnaie</label>
                <select v-model="selectedCrypto" class="form-control">
                  <option value="tous">Tous</option>
                  <option v-for="cryptoOption in cryptos" :key="cryptoOption.id" :value="cryptoOption.id">
                    {{ cryptoOption.name }}
                  </option>
                </select>
              </div>
  
              <!-- Date et heure min -->
              <div class="form-group">
                <label for="dateMin">Date et heure min</label>
                <input v-model="dateMin" type="datetime-local" class="form-control" />
              </div>
  
              <!-- Date et heure max -->
              <div class="form-group">
                <label for="dateMax">Date et heure max</label>
                <input v-model="dateMax" type="datetime-local" class="form-control" />
              </div>
  
              <!-- Bouton de validation -->
              <button @click="submitAnalysis" class="btn btn-submit">Valider</button>
  
              <!-- Affichage du résultat de l'analyse -->
              <div v-if="result">
                <hr />
                <h5>Résultat de l'analyse des commissions</h5>
                <p><strong>Type d'analyse :</strong> {{ typeAnalyse }}</p>
                <p><strong>Cryptomonnaie sélectionnée :</strong> {{ selectedCrypto }}</p>
                <p><strong>Plage de dates :</strong> {{ dateMin }} à {{ dateMax }}</p>
                <p><strong>Résultat :</strong> {{ result }}</p>
              </div>
            </card>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import Card from 'src/components/Cards/Card.vue';
  import axios from 'axios';
  
  export default {
    components: {
      Card
    },
    data() {
      return {
        typeAnalyse: '', // Type d'analyse sélectionné (Somme, Moyenne)
        selectedCrypto: 'tous', // Cryptomonnaie sélectionnée
        cryptos: [ // Liste des cryptomonnaies
          { name: 'Bitcoin (BTC)', id: 'bitcoin' },
          { name: 'Ethereum (ETH)', id: 'ethereum' },
          { name: 'Tether (USDT)', id: 'usdt' },
          { name: 'Litecoin (LTC)', id: 'litecoin' },
          { name: 'Ripple (XRP)', id: 'ripple' },
        ],
        dateMin: '',      // Date min
        dateMax: '',      // Date max
        result: null,     // Résultat de l'analyse
      };
    },
    methods: {
      async fetchCryptos() {
        try {
          const response = await axios.get('http://localhost:8081/api/cryptos');
          this.cryptos = response.data;
        } catch (error) {
          console.error('Error fetching cryptos:', error);
        }
      },
      async submitAnalysis() {
        // Vérifier si les paramètres sont définis
        if (!this.dateMin || !this.dateMax) {
          alert("Veuillez définir une plage de dates.");
          return;
        }
  
        try {
          const response = await axios.post('http://localhost:8081/api/analyseCommission', {
            typeAnalyse: this.typeAnalyse,
            selectedCrypto: this.selectedCrypto,
            dateMin: this.dateMin,
            dateMax: this.dateMax
          });
          this.result = response.data.result;
        } catch (error) {
          console.error('Error performing analysis:', error);
        }
      }
    },
    created() {
      this.fetchCryptos();
    }
  };
  </script>
  
  <style scoped>
  /* Corps principal */
  .content {
    padding: 20px;
  }
  
  .card-body {
    padding: 20px;
    background-color: #fff;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
  }
  
  /* Titres */
  .card-title {
    font-size: 1.5rem;
    font-weight: bold;
    margin-bottom: 10px;
  }
  
  h5 {
    font-size: 1.2rem;
    font-weight: bold;
    margin-top: 20px;
  }
  
  /* Formulaires */
  .form-group {
    margin-bottom: 15px;
  }
  
  label {
    font-weight: 500;
    color: #333;
    margin-bottom: 5px;
  }
  
  input.form-control, select.form-control {
    border: 1px solid #ccc;
    border-radius: 4px;
    padding: 8px 12px;
    width: 100%;
    font-size: 1rem;
  }
  
  /* Bouton de validation */
  .btn-submit {
    background-color: #000;
    color: #fff;
    border: none;
    padding: 10px 20px;
    font-size: 1rem;
    font-weight: 500;
    cursor: pointer;
    border-radius: 5px;
    transition: background-color 0.3s;
  }
  
  .btn-submit:hover {
    background-color: #333;
  }
  
  /* Résultat */
  hr {
    border: 0;
    border-top: 1px solid #ccc;
    margin: 20px 0;
  }
  
  p {
    font-size: 1rem;
    color: #333;
  }
  
  strong {
    font-weight: 600;
  }
  </style>
