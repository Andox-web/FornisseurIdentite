<template>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div class="col-12">
          <card class="strpied-tabled-with-hover"
                body-classes="table-full-width table-responsive"
          >
            <template slot="header">
              <h4 class="card-title">Portefeuille des utilisateurs</h4>
              <p class="card-category">Vue d'ensemble des achats, ventes et valeurs du portefeuille</p>
              <div>
                <label for="maxDateTime">Date et Heure Max :</label>
                <input type="datetime-local" id="maxDateTime" v-model="maxDateTime" />
                <button @click="applyFilter">Valider</button>
              </div>
            </template>

            <!-- Tableau des données -->
            <table class="table table-hover table-striped">
              <thead>
                <tr>
                  <th>Utilisateur</th>
                  <th>Total Achat</th>
                  <th>Total Vente</th>
                  <th>Valeur portefeuille</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(user, index) in filteredData" :key="index">
                  <td>{{ user.utilisateur }}</td>
                  <td>{{ user.totalAchat }}</td>
                  <td>{{ user.totalVente }}</td>
                  <td>{{ user.valeurPortefeuille }}</td>
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
  import LTable from 'src/components/Table.vue'
  import Card from 'src/components/Cards/Card.vue'

  const tableColumns = ['Utilisateur', 'Total Achat', 'Total Vente', 'Valeur portefeuille']

  export default {
    components: {
      LTable,
      Card
    },
    data () {
      return {
        table1: {
          columns: [...tableColumns],
          data: [] // Initialisation des données comme vide
        },
        maxDateTime: '',
        allData: [
          {
            utilisateur: 'User 1',
            totalAchat: '$100.00',
            totalVente: '$50.00',
            valeurPortefeuille: '$500.00',
            date: '2025-02-07T05:40:55' // Date au format ISO
          },
          {
            utilisateur: 'User 2',
            totalAchat: '$200.00',
            totalVente: '$75.00',
            valeurPortefeuille: '$700.00',
            date: '2025-02-06T10:30:00'
          },
          {
            utilisateur: 'User 3',
            totalAchat: '$150.00',
            totalVente: '$90.00',
            valeurPortefeuille: '$600.00',
            date: '2025-02-05T15:20:00'
          }
        ], // Données fixes
        filteredData: [] // Données filtrées
      }
    },
    created() {
      // Initialisation : afficher toutes les données par défaut
      this.filteredData = [...this.allData];
    },
    methods: {
      applyFilter() {
        if (!this.maxDateTime) {
          // Si aucune date n'est sélectionnée, afficher toutes les données
          this.filteredData = [...this.allData];
          return;
        }

        const maxDate = new Date(this.maxDateTime);
        this.filteredData = this.allData.filter(user => {
          const userDate = new Date(user.date);
          return userDate <= maxDate;
        });
      }
    }
  }
</script>

<style>
</style>
