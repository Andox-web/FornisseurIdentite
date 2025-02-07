<template>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div class="col-12">
          <card class="strpied-tabled-with-hover"
                body-classes="table-full-width table-responsive"
          >
            <template slot="header">
              <h4 class="card-title">Achat/vente cryptomonnaie</h4>
              <p class="card-category">Liste des cryptos </p>
            </template>
            <table class="table table-hover table-striped">
              <thead>
                <tr>
                  <th>Crypto</th>
                  <th>Prix</th>
                  <th>Quantité</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in tableData" :key="item.id">
                  <td>{{ item.nom }}</td>
                  <td>{{ item.prix }}</td>
                  <td>
                    <input type="number"
                           v-model="item.quantite"
                           min="0"
                           :placeholder="'Quantité'"
                           class="form-control" />
                  </td>
                  <td>
                    <button @click="acheter(item)" class="btn btn-primary">Acheter</button>
                    <button @click="vendre(item)" class="btn btn-danger">Vendre</button>
                  </td>
                </tr>
              </tbody>
            </table>
          </card>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import Card from 'src/components/Cards/Card.vue'

  export default {
    components: {
      Card
    },
    data() {
      return {
        tableData: []
      };
    },
    mounted() {
      this.getCryptos();
    },
    methods: {
      // Fonction pour récupérer les cryptomonnaies depuis l'API
      getCryptos() {
        fetch('http://localhost:8081/cryptos')
          .then(response => response.json())
          .then(data => {
            this.tableData = data.map(crypto => ({
              id: crypto.id,
              nom: crypto.nom,
              prix: '$100', // Exemple de prix pour chaque crypto (à adapter selon les données)
              quantite: 0
            }));
          })
          .catch(error => console.error('Erreur lors de la récupération des cryptos:', error));
      },

      // Fonction d'achat d'une crypto
      acheter(item) {
        if (item.quantite > 0) {
          const token = localStorage.getItem('auth_token'); // Récupérer le token depuis localStorage
          fetch('http://localhost:8081/achat', {
            method: 'POST',
            headers: {
              'Authorization': `Bearer ${token}`,
              'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: new URLSearchParams({
              crypto_id: item.id,
              quantite: item.quantite
            })
          })
            .then(response => response.json())
            .then(data => {
              if (data.ok) {
                alert(`Achat de ${item.quantite} ${item.nom} réussi.`);
              } else {
                alert('Erreur lors de l\'achat.');
              }
            })
            .catch(error => console.error('Erreur d\'achat:', error));
        } else {
          alert('Veuillez entrer une quantité valide.');
        }
      },

      // Fonction de vente d'une crypto
      vendre(item) {
        if (item.quantite > 0) {
          const token = localStorage.getItem('auth_token'); // Récupérer le token depuis localStorage
          fetch('http://localhost:8081/vente', {
            method: 'POST',
            headers: {
              'Authorization': `Bearer ${token}`,
              'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: new URLSearchParams({
              crypto_id: item.id,
              quantite: item.quantite
            })
          })
            .then(response => response.json())
            .then(data => {
              if (data.ok) {
                alert(`Vente de ${item.quantite} ${item.nom} réussie.`);
              } else {
                alert('Erreur lors de la vente.');
              }
            })
            .catch(error => console.error('Erreur de vente:', error));
        } else {
          alert('Veuillez entrer une quantité valide.');
        }
      }
    }
  };
</script>

<style>
</style>
