<template>
    <div class="content">
      <div class="container-fluid">
        <div class="row">
          <div class="col-12">
            <card class="card-body">
              <template slot="header">
                <h4 class="card-title">Modifier les commissions</h4>
              </template>
  
              <!-- Commission d'achat -->
              <div class="form-group">
                <label for="commissionAchat">Commission d'achat</label>
                <div class="battery-container">
                  <div class="battery-icon">
                    <div 
                      class="battery-level-achat" 
                      :style="{ height: commissionAchat + '%' }">
                      <span>{{ commissionAchat }}%</span>
                    </div>
                  </div>
                  <span class="battery-percentage">{{ commissionAchat }}%</span>
                </div>
                <input type="range" v-model="commissionAchat" min="0" max="100" step="1" class="form-control-range" />
                <button @click="submitCommission('achat')" class="btn btn-primary mt-2">Modifier</button>
              </div>
  
              <!-- Commission de vente -->
              <div class="form-group">
                <label for="commissionVente">Commission de vente</label>
                <div class="battery-container">
                  <div class="battery-icon">
                    <div 
                      class="battery-level-vente" 
                      :style="{ height: commissionVente + '%'}">
                      <span>{{ commissionVente }}%</span>
                    </div>
                  </div>
                  <span class="battery-percentage" >{{ commissionVente }}%</span>
                </div>
                <input type="range" v-model="commissionVente" min="0" max="100" step="1" class="form-control-range" />
                <button @click="submitCommission('vente')" class="btn btn-primary mt-2">Modifier</button>
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
        commissionAchat: 30,  // Commission d'achat initiale (en %)
        commissionVente: 20,  // Commission de vente initiale (en %)
      };
    },
    methods: {
      async refreshToken() {
        const token = localStorage.getItem('auth_token');
        try {
          const response = await fetch(`http://localhost:5000/refresh-connection?token=${token}`);
          const data = await response.json();
          if (data.IsValid) {
            localStorage.setItem('auth_token', data.Token);
          } else {
            localStorage.removeItem('auth_token');
            alert(data.message);
            window.location.href = '/';
          }
        } catch (error) {
          console.error('Error refreshing token:', error);
          localStorage.removeItem('auth_token');
          alert('Erreur lors de la mise à jour du token');
          window.location.href = '/';
        }
      },
      async fetchCurrentCommissions() {
        await this.refreshToken();
        try {
          const response = await fetch('http://localhost:8081/api/getCurrentCommissions');
          const data = await response.json();
          this.commissionAchat = data.achatCommission;
          this.commissionVente = data.venteCommission;
        } catch (error) {
          console.error('Error fetching current commissions:', error);
          alert('Erreur lors de la récupération des commissions actuelles');
        }
      },
      async submitCommission(type) {
        await this.refreshToken();
        let commission = 0;
  
        if (type === 'achat') {
          commission = this.commissionAchat;
        } else if (type === 'vente') {
          commission = this.commissionVente;
        }
  
        try {
          await fetch('http://localhost:8081/api/updateCommission', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify({
              achatCommission: type === 'achat' ? commission : this.commissionAchat,
              venteCommission: type === 'vente' ? commission : this.commissionVente
            })
          });
          alert(`${type === 'achat' ? 'Commission d\'achat' : 'Commission de vente'} modifiée à ${commission}%`);
        } catch (error) {
          console.error('Error updating commission:', error);
          alert('Erreur lors de la mise à jour de la commission');
        }
      }
    },
    mounted() {
      this.fetchCurrentCommissions();
    }
  };
  </script>
  
  <style scoped>
  /* Styles généraux */
  .content {
    padding: 20px;
  }
  
  /* Disposition horizontale de la batterie et du pourcentage */
  .battery-container {
    display: flex;
    align-items: center;
    justify-content: flex-start;
  }
  
  .battery-icon {
    width: 50px;
    height: 150px;
    border: 2px solid #ccc;
    border-radius: 10px;
    position: relative;
    margin-right: 10px; /* Espacement entre la batterie et le texte */
  }
  
  .battery-level-achat {
    background-color: #4caf50;
    width: 100%;
    border-radius: 10px;
    display: flex;
    justify-content: center;
    align-items: center;
    color: white;
    font-size: 14px;
    position: absolute;
    bottom: 0;
    transition: height 0.3s ease;
  }
  
  .battery-level-vente {
    background-color: #e10000;
    width: 100%;
    border-radius: 10px;
    display: flex;
    justify-content: center;
    align-items: center;
    color: white;
    font-size: 14px;
    position: absolute;
    bottom: 0;
    transition: height 0.3s ease;
  }
  
  /* Texte du pourcentage */
  .battery-percentage {
    font-size: 14px;
    font-weight: bold;
  }
  
  /* Espacement et positionnement */
  button {
    display: block;
    margin-top: 10px;
  }
  
  /* Personnalisation de l'apparence des boutons */
  .btn-primary {
    background-color: #007bff;
    border-color: #007bff;
  }
  
  .btn-primary:hover {
    background-color: #0056b3;
    border-color: #004085;
  }
  
  /* Curseur de plage */
  .form-control-range {
    margin-top: 10px;
  }
  </style>
