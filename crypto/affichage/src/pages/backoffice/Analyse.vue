<template>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div class="col-12">
          <card class="card-body">
            <template slot="header">
              <h4 class="card-title">Analyser les données</h4>
            </template>

            <!-- Zone de liste pour le type d'analyse -->
            <div class="form-group">
              <label for="typeAnalyse">Type d'analyse</label>
              <select v-model="typeAnalyse" class="form-control">
                <option value="quartile">1er quartile</option>
                <option value="max">Max</option>
                <option value="min">Min</option>
                <option value="moyenne">Moyenne</option>
                <option value="ecart-type">Écart-type</option>
              </select>
            </div>

            <!-- Case à cocher pour la sélection des cryptomonnaies -->
            <div class="form-group">
              <label for="crypto">Cryptomonnaie</label>
              <div>
                <input type="checkbox" v-model="selectedCryptos" value="tous" /> Tous
                <div v-for="crypto in cryptos" :key="crypto.id">
                  <input type="checkbox" v-model="selectedCryptos" :value="crypto.id" /> {{ crypto.name }}
                </div>
              </div>
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
              <h5>Résultat de l'analyse</h5>
              <p><strong>Type d'analyse :</strong> {{ typeAnalyse }}</p>
              <p><strong>Cryptomonnaies sélectionnées :</strong> {{ selectedCryptos.join(', ') }}</p>
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
      typeAnalyse: '', // Type d'analyse sélectionné
      selectedCryptos: [], // Cryptomonnaies sélectionnées
      cryptos: [], // Liste des cryptomonnaies
      dateMin: '',      // Date min
      dateMax: '',      // Date max
      result: null,     // Résultat de l'analyse
    };
  },
  methods: {
    async refreshToken() {
      const token = localStorage.getItem('auth_token');
      try {
        const response = await axios.get('http://localhost:5000/refresh-connection', {
          params: { token }
        });
        if (response.data.IsValid) {
          localStorage.setItem('auth_token', response.data.Token);
        } else {
          localStorage.removeItem('auth_token');
          alert(response.data.message);
          window.location.href = '/';
        }
      } catch (error) {
        console.error('Error refreshing token:', error);
        localStorage.removeItem('auth_token');
        alert('Erreur lors de la mise à jour du token');
        window.location.href = '/';
      }
    },
    async fetchCryptos() {
      await this.refreshToken();
      try {
        const response = await axios.get('http://localhost:8081/api/cryptos');
        this.cryptos = response.data;
      } catch (error) {
        console.error('Error fetching cryptos:', error);
        alert('Erreur lors de la récupération des cryptomonnaies');
      }
    },
    async submitAnalysis() {
      await this.refreshToken();
      // Vérifier si les paramètres sont définis
      if (!this.dateMin || !this.dateMax) {
        alert("Veuillez définir une plage de dates.");
        return;
      }

      try {
        const response = await axios.post('http://localhost:8081/api/analyseCours', {
          typeAnalyse: this.typeAnalyse,
          selectedCryptos: this.selectedCryptos,
          dateMin: this.dateMin,
          dateMax: this.dateMax
        });
        this.result = response.data.result;
      } catch (error) {
        console.error('Error performing analysis:', error);
        alert('Erreur lors de l\'analyse des données');
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
