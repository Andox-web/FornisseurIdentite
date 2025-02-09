<template>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-12">
          <div class="crypto-selector">
            <label for="cryptoSelect">Sélectionner une crypto :</label>
            <select v-model="selectedCrypto" id="cryptoSelect">
              <option v-for="crypto in cryptos" :key="crypto.id" :value="crypto.id">
                {{ crypto.nom }}
              </option>
            </select>
          </div>

          <chart-card 
            :chart-data="filteredChartData"
            :chart-options="lineChart.options"
            :responsive-options="lineChart.responsiveOptions"
          >
            <template slot="header">
              <h4 class="card-title" v-if="selectedCrypto">{{ selectedCrypto }} Chart</h4>
              <p class="card-category">24 Hours performance</p>
            </template>
            <template slot="footer">
              <!-- <div class="legend">
                <i class="fa fa-circle text-info"></i> Bitcoin
                <i class="fa fa-circle text-danger"></i> Ethereum
                <i class="fa fa-circle text-warning"></i> BNB
              </div> -->
              <hr>
              <div class="stats">
                <i class="fa fa-history"></i> Updated 10 seconds ago
              </div>
            </template>
          </chart-card>
        </div>
      </div>

      <div class="row">
        <div class="col-md-12">
          <card>
            <template slot="header">
              <h5 class="title">Prix crypto actuel</h5>
              <p class="category">Mise à jour toutes les 10s</p>
            </template>
            <l-table :data="tableData.data" :columns="tableData.columns">
              <template slot-scope="{ row }">
                <td>{{ row.nom }}</td>
                <td>{{ row.prix }} $</td>
              </template>
            </l-table>
            <div class="footer">
              <hr>
              <div class="stats">
                <i class="fa fa-history"></i> Updated 10 seconds ago
              </div>
            </div>
          </card>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ChartCard from 'src/components/Cards/ChartCard.vue';
import LTable from 'src/components/Table.vue';
import Card from '../../components/Cards/Card.vue';
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

export default {
  data() {
    return {
      selectedCrypto: "Bitcoin",
      cryptos: ["Bitcoin", "Ethereum", "BNB", "Solana"],
      tableData: {
        data: [] // On initialise avec un tableau vide
      },
      stompClient: null
    };
  },
  methods: {
    connectWebSocket() {
      const socket = new SockJS("/crypto-websocket");
      this.stompClient = new Client({
        webSocketFactory: () => socket,
        reconnectDelay: 5000,
        debug: (msg) => console.log(msg)
      });

      this.stompClient.onConnect = () => {
        console.log("Connecté au WebSocket");

        // Écouter les mises à jour de crypto
        this.stompClient.subscribe("/topic/crypto", (message) => {
          const updatedData = JSON.parse(message.body);
          console.log("Mise à jour des cryptos reçue :", updatedData);

          // Mettre à jour l'affichage des données
          this.tableData.data = updatedData;
        });
      };

      this.stompClient.activate();
    }
  },
  mounted() {
    this.connectWebSocket();
  },
  components: {
    LTable,
    ChartCard,
    Card
  },
  data() {
    return {
      selectedCrypto: 1,
      cryptos: [],
      lineChart: {
        data: {
          labels: [],
          series: {
            test: [10000, 12000, 13000, 14000, 15000, 16000, 17000, 18000]
            // 2: [20000, 30000, 40000, 50000, 60000, 70000, 80000, 90000],
            // 3: [23000, 24000, 25000, 26000, 27000, 28000, 29000, 210000]
          }
        },
        options: {
          low: 0,
          high: 80000,
          showArea: false,
          height: '245px',
          axisX: {
            showGrid: false
          },
          lineSmooth: true,
          showLine: true,
          showPoint: true,
          fullWidth: true,
          chartPadding: {
            right: 50
          }
        },
      },
      tableData: {
        data: [],
        columns: ['Nom', 'Prix']
      },
      interval: null
    };
  },
  computed: {
    filteredChartData() {
      return {
        labels: this.lineChart.data.labels,
        series: [this.lineChart.data.series.test || []]
      };
    }
  },
  watch: {
    async selectedCrypto(newVal) {
      if (newVal) {
        await this.fetchCours();
      }
    }
  },

  methods: {
    async fetchCryptos() {
      try {
        const response = await fetch('http://localhost:8081/cryptos');
        const data = await response.json();
        this.cryptos = data.map(c => c);
      } catch (error) {
        console.error("Erreur lors de la récupération des cryptos", error);
      }
    },
    async fetchCours() {
      if (!this.selectedCrypto) return;
      try {
        const selected = this.selectedCrypto;
        console.log(selected);
        if (!selected) return;
        console.log("fetching cours .");

        const response = await fetch(`http://localhost:8081/cryptos/cours?id=${selected}`);
        
        const data = await response.json();
        console.log(data);
        // this.lineChart.data.series = data.map(d => d.valeur);
        // this.lineChart.data.series = [data.map(d => d.valeur)];
        this.lineChart.data.series.test = data.map(d => d.valeur);

      } catch (error) {
        console.error("Erreur lors de la récupération du cours", error);
      }
    },
    async fetchPrix() {
      try {
        const response = await fetch('http://localhost:8081/cryptos/prix');
        const data = await response.json();
        this.tableData.data = data.map(item => ({
          nom: item.cryptomonnaie.nom,
          prix: item.valeur
        }));
      } catch (error) {
        console.error("Erreur lors de la récupération des prix", error);
      }
    },
    startAutoRefresh() {
      this.interval = setInterval( async() => {
       await this.fetchCours();
       await this.fetchPrix();
      }, 10000);
    }
  },
  mounted() {
    this.fetchCryptos().then(async () => {
      await this.fetchCours();
      await this.fetchPrix();
      await this.startAutoRefresh();
    });
  },
  beforeDestroy() {
    if (this.interval) {
      clearInterval(this.interval);
    }
  }
};
</script>


<style>
.crypto-selector {
  margin-bottom: 15px;
}

.crypto-selector label {
  margin-right: 10px;
  font-weight: bold;
}

.crypto-selector select {
  padding: 5px;
  font-size: 14px;
  border-radius: 4px;
  border: 1px solid #ccc;
}
</style>
