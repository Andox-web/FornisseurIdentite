<template>
    <div class="content">
      <div class="container-fluid">
        <div class="row">
          <div class="col-12">
            <card class="strpied-tabled-with-hover"
                  body-classes="table-full-width table-responsive"
            >
              <template slot="header">
                <h4 class="card-title">Historique de vos transactions</h4>
                <p class="card-category">liste de vos transactions</p>
              </template>
              <l-table class="table-hover table-striped"
                       :columns="table1.columns"
                       :data="table1.data"
                       @button-clicked="handleButtonClick">
              </l-table>
            </card>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
    import LTable from 'src/components/Table.vue'
    import Card from 'src/components/Cards/Card.vue'
  
    const tableColumns = ['Id', 'crypto', 'type', 'montant', 'Utilisateur']
  
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
          }
        }
      },
      created() {
        this.fetchTransactions();
      },
      methods: {
        fetchTransactions() {
          // Utilisation de la méthode fetch pour appeler l'API
          fetch('http://localhost:8081/transactions')  // Remplacez par l'URL réelle de votre API
            .then(response => response.json())
            .then(data => {
              // Traitement des données reçues
              const transactions = data.rehetra.map(transaction => {
                // Déterminer le type en fonction de la présence de vendeur ou acheteur
                const type = transaction.vendeur ? 'vente' : 'achat';
                return {
                  id: transaction.id,
                  crypto: transaction.cryptomonnaie.nom,
                  type: type,
                  montant: `$${(transaction.quantitecrypto * transaction.annonceVente.prix).toFixed(2)}`, // Calcul du montant
                  utilisateur: transaction.vendeur ? transaction.vendeur.nom : transaction.acheteur.nom
                }
              });
  
              // Mettre à jour la table avec les nouvelles données
              this.table1.data = transactions;
            })
            .catch(error => {
              console.error('Erreur lors de la récupération des transactions:', error);
            });
        },
        handleButtonClick() {
          // Traitement du clic sur un bouton dans la table si nécessaire
        }
      }
    }
  </script>
  
  <style>
  </style>
  