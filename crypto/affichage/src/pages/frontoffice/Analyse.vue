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
                <option value="1er_quartile">1er quartile</option>
                <option value="max">Max</option>
                <option value="min">Min</option>
                <option value="moyenne">Moyenne</option>
                <option value="ecart_type">Écart-type</option>
              </select>
            </div>
            
            <!-- Case à cocher pour la crypto, basé sur un tableau de données -->
            <div class="form-group">
              <label for="crypto">Cryptomonnaie</label>
              <div v-for="cryptoOption in cryptos" :key="cryptoOption.value" class="checkbox-inline">
                <label>
                  <input type="checkbox" v-model="selectedCryptos" :value="cryptoOption.value"> {{ cryptoOption.name }}
                </label>
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
            <button @click="submitAnalysis" class="btn btn-primary">Valider</button>

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

export default {
  components: {
    Card
  },
  data() {
    return {
      typeAnalyse: '',  // Type d'analyse sélectionné
      selectedCryptos: [], // Liste des cryptos sélectionnées
      cryptos: [ // Tableau de données pour les cryptomonnaies
        { name: 'Bitcoin (BTC)', value: 'bitcoin' },
        { name: 'Ethereum (ETH)', value: 'ethereum' },
        { name: 'Tether (USDT)', value: 'usdt' },
        { name: 'Litecoin (LTC)', value: 'litecoin' },
        { name: 'Ripple (XRP)', value: 'ripple' },
        // Ajoutez d'autres cryptos selon les besoins
      ],
      dateMin: '',      // Date min
      dateMax: '',      // Date max
      result: null,     // Résultat de l'analyse
    };
  },
  methods: {
    submitAnalysis() {
      // Exemple de calcul pour l'analyse. Vous pouvez remplacer ceci par des calculs réels.
      if (this.selectedCryptos.length === 0 || !this.dateMin || !this.dateMax) {
        alert("Veuillez sélectionner au moins une cryptomonnaie et définir une plage de dates.");
        return;
      }

      // Logique d'analyse simple : ici, on suppose que chaque type d'analyse donne un résultat fictif
      switch (this.typeAnalyse) {
        case '1er_quartile':
          this.result = 'Calcul du 1er quartile : 2000 USD';
          break;
        case 'max':
          this.result = 'Max trouvé : 5000 USD';
          break;
        case 'min':
          this.result = 'Min trouvé : 100 USD';
          break;
        case 'moyenne':
          this.result = 'Moyenne : 2500 USD';
          break;
        case 'ecart_type':
          this.result = 'Écart-type : 1500 USD';
          break;
        default:
          this.result = 'Aucun type d\'analyse sélectionné';
          break;
      }
    }
  }
};
</script>

<style scoped>
/* Ajoutez des styles personnalisés si nécessaire */
</style>
