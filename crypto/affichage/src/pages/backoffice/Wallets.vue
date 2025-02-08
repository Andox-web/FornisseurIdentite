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
                <label for="startDateTime">Date et Heure Début :</label>
                <input type="datetime-local" id="startDateTime" v-model="startDateTime" />
                <label for="endDateTime">Date et Heure Fin :</label>
                <input type="datetime-local" id="endDateTime" v-model="endDateTime" />
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
        startDateTime: '',
        endDateTime: '',
        allData: [], // Données initialisées comme vide
        filteredData: [] // Données filtrées
      }
    },
    created() {
      this.fetchAllData();
    },
    methods: {
      async fetchAllData() {
        try {
          const response = await fetch('http://localhost:8081/portefeuilleanalyse');
          const data = await response.json();
          this.allData = data;
          this.filteredData = [...this.allData]; // Set filteredData to allData initially
        } catch (error) {
          console.error('Erreur lors de la récupération des données:', error);
        }
      },
      async applyFilter() {
        if ((this.startDateTime && !this.endDateTime) || (!this.startDateTime && this.endDateTime)) {
          console.error('Les champs Date et Heure Début et Fin doivent être soit tous les deux remplis, soit tous les deux vides.');
          return;
        }
        try {
          let url = 'http://localhost:8081/portefeuilleanalyse';
          if (this.startDateTime && this.endDateTime) {
        url += `?start=${this.startDateTime}&end=${this.endDateTime}`;
          }
          const response = await fetch(url);
          const data = await response.json();
          this.filteredData = data;
        } catch (error) {
          console.error('Erreur lors de l\'application du filtre:', error);
        }
      }
    }
  }
</script>

<style>
</style>
