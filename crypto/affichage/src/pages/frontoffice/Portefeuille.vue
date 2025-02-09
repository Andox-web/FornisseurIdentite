<template>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div class="col-12">
          <card class="strpied-tabled-with-hover"
                body-classes="table-full-width table-responsive"
          >
            <template slot="header">
              <h4 class="card-title">Portefeuille crypto</h4>
              <p class="card-category"></p>
            </template>
            <l-table class="table-hover table-striped"
                     :columns="table1.columns"
                     :data="table1.data"
                     @button-clicked="handleButtonClick">
            </l-table>
          </card>
        </div>

        <div class="col-12">
          <card class="card-plain">
            <template slot="header">
              <h4 class="card-title">Portefeuille Fiat</h4>
              <p class="card-category">Vous pouvez les échanger contre des cryptos</p>
            </template>
            <div class="table-responsive">
              <l-table class="table-hover"
                       :columns="table2.columns"
                       :data="table2.data">
              </l-table>
            </div>
          </card>
        </div>

        <div class="col-12">
          <card class="card-body">
            <template slot="header">
              <h4 class="card-title">Demande de retrait/dépôt</h4>
            </template>
            <div class="form-group">
              <label for="transactionType">Type de transaction</label>
              <select v-model="transactionType" class="form-control">
                <option value="depot">Dépôt</option>
                <option value="retrait">Retrait</option>
              </select>
            </div>
            <div class="form-group">
              <label for="amount">Quantité</label>
              <input v-model="amount" type="number" class="form-control" placeholder="Entrez la quantité">
            </div>
            <button @click="submitRequest" class="btn btn-primary">Envoyer la demande</button>
          </card>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
  import LTable from 'src/components/Table.vue'
  import Card from 'src/components/Cards/Card.vue'

  const tableColumns = ['Id', 'crypto', 'prix', 'quantite']
  const tableColumnsFiat = ['Id', 'devis', 'quantite']

  export default {
    components: {
      LTable,
      Card
    },
    data() {
      return {
        table1: {
          columns: [...tableColumns],
          data: []
        },
        table2: {
          columns: [...tableColumnsFiat],
          data: []
        },
        transactionType: '',
        amount: ''
      };
    },
    methods: {
      async refreshToken() {
        const token = localStorage.getItem('auth_token');
        try {
          const response = await axios.get('http://localhost:5000/api/refresh-connection', {
            params: { token }
          });
          if (response.data.IsValid) {
            localStorage.setItem('auth_token', response.data.SessionId);
          } else {
            localStorage.removeItem('auth_token');
            alert(response.data.message);
            window.location.href = 'http://localhost:5000/';
          }
        } catch (error) {
          console.error('Error refreshing token:', error);
          localStorage.removeItem('auth_token');
          alert('Erreur lors de la mise à jour du token');
          window.location.href = 'http://localhost:5000/';
        }
      },
      async submitRequest() {
        await this.refreshToken();
        alert(`Demande de ${this.transactionType} de ${this.amount} envoyée.`);
      },

      async fetchPortefeuille() {
        await this.refreshToken();
        const token = localStorage.getItem('auth_token');
        if (!token) {
          alert("Token introuvable dans le localStorage");
          return;
        }

        try {
          const response = await fetch('http://localhost:8081/portefeuille', {
            method: 'GET',
            headers: {
              'Authorization': `Bearer ${token}`
            }
          });

          if (!response.ok) {
            throw new Error('Erreur de récupération des données');
          }

          const data = await response.json();

          // Affectation des données dans les tableaux
          this.table1.data = data.portefeuille_crypto.map(item => ({
            id: item.id,
            crypto: item.cryptomonnaie.nom,
            prix: `$${item.quantite.toFixed(2)}`,
            quantite: item.quantite
          }));

          this.table2.data = data.portefeuille_fiat.map(item => ({
            id: item.id,
            devis: item.utilisateur.nom,  // Ou autre attribut pertinent de l'utilisateur
            quantite: `$${item.quantite.toFixed(2)}`
          }));

        } catch (error) {
          console.error("Erreur lors de la récupération des données :", error);
        }
      }
    },
    mounted() {
      this.fetchPortefeuille();
    }
  }
</script>

<style>
</style>
