<template>
    <div>
      <div class="crypto-container">
        <div class="card">
          <div class="card-header">
            <h4>Demande de transaction</h4>
            <p>Liste des demandes</p>
          </div>
          <div class="card-body">
            <table class="crypto-table">
              <thead>
                <tr>
                  <th v-for="col in tableColumns" :key="col">{{ col }}</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="row in tableData" :key="row.id" @click="openUserHistory(row)">
                  <td>
                    <img :src="row.avatar" class="user-avatar" />
                    {{ row.user }}
                  </td>
                  <td>{{ row.quantite }}</td>
                  <td>{{ row.date }}</td>
                  <td>{{ row.type }}</td>
                  <td>
                    <button class="action-btn accept" @click.stop="Accepter(row)">Accepter</button>
                    <button class="action-btn refuse" @click.stop="Refuser(row)">Refuser</button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
  
      <!-- Modal for displaying user history -->
      <div v-if="showModal" class="modal-overlay" @click="closeModal">
        <div class="modal-content" @click.stop>
          <h4>Historique de {{ selectedUser.user }}</h4>
          <table class="crypto-table">
            <thead>
              <tr>
                <th v-for="col in tableColumns" :key="col">{{ col }}</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="history in selectedUser.history" :key="history.id">
                <td>
                  <img :src="history.avatar" class="user-avatar" />
                  {{ history.user }}
                </td>
                <td>{{ history.quantite }}</td>
                <td>{{ history.date }}</td>
                <td>{{ history.type }}</td>
              </tr>
            </tbody>
          </table>
          <button @click="closeModal">Fermer</button>
        </div>
      </div>
    </div>
  </template>
  
  
  <script>
  export default {
    data() {
      return {
        tableColumns: ['User', 'Quantité', 'Date', 'Type'],
        tableData: [
          { id: 1, user: 'Zotah', quantite: '$36,738', date: '2024-11-4', type: 'Depot', avatar: 'avatars/zotah.jpg' },
          { id: 2, user: 'Manoa', quantite: '$23,789', date: '2024-02-14', type: 'Retrait', avatar: 'avatars/manoa.jpg' },
          { id: 3, user: 'Ando', quantite: '$56,142', date: '2024-10-15', type: 'Depot', avatar: 'avatars/ando.jpg' },
          { id: 4, user: 'Kajy', quantite: '$38,735', date: '2024-12-4', type: 'Retrait', avatar: 'avatars/kajy.jpg' },
          { id: 5, user: 'DADATOA', quantite: '$63,542', date: '2024-03-16', type: 'Depot', avatar: 'avatars/dadatoa.jpg' }
        ],
        showModal: false,
        selectedUser: {}
      };
    },
    methods: {
      Accepter(row) {
        console.log('Accepter ', row);
      },
      Refuser(row) {
        console.log('Refuser ', row);
      },
      openUserHistory(row) {
        // Set the selected user and their transaction history
        this.selectedUser = {
          user: row.user,
          history: [
            { id: 1, user: row.user, quantite: row.quantite, date: row.date, type: row.type, avatar: row.avatar },
            // Add more history records for the selected user
          ]
        };
        this.showModal = true;
      },
      closeModal() {
        this.showModal = false;
      }
    }
  };
  </script>
  
  <style scoped>
  .crypto-container {
    padding: 20px;
    background: #0a0a0a;
    color: #fff;
    display: flex;
    justify-content: center;
  }
  
  .card {
    background: #1a1a1a;
    border-radius: 10px;
    padding: 20px;
    width: 100%;
    max-width: 800px;
  }
  
  .card-header h4 {
    color: #ffd700;
    margin-bottom: 5px;
  }
  
  .card-header p {
    color: #a9a9a9;
  }
  
  .crypto-table {
    width: 100%;
    border-collapse: collapse;
  }
  
  .crypto-table th, .crypto-table td {
    padding: 10px;
    text-align: left;
    border-bottom: 1px solid #333;
  }
  
  .crypto-table tbody tr:hover {
    background-color: #333;
  }
  
  .user-avatar {
    width: 30px;
    height: 30px;
    border-radius: 50%;
    margin-right: 10px;
    vertical-align: middle;
  }
  
  .action-btn {
    border: none;
    padding: 8px 12px;
    cursor: pointer;
    color: #fff;
    border-radius: 4px;
    margin-right: 10px;
  }
  
  .accept {
    background: #28a745;
  }
  
  .accept:hover {
    background: #218838;
  }
  
  .refuse {
    background: #dc3545;
  }
  
  .refuse:hover {
    background: #c82333;
  }
  
  /* Modal Styles */
  .modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.7);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
  }
  
  .modal-content {
    background: #1a1a1a;
    padding: 20px;
    border-radius: 10px;
    width: 80%;
    max-width: 600px;
    color: #fff;
  }
  
  .modal-content button {
    margin-top: 10px;
    padding: 10px;
    background: #28a745;
    border: none;
    color: #fff;
    border-radius: 5px;
  }
  
  .modal-content button:hover {
    background: #218838;
  }
  </style>
  