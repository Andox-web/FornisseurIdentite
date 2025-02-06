<template>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-12">
          <div class="crypto-selector">
            <label for="cryptoSelect">Sélectionner une crypto :</label>
            <select v-model="selectedCrypto" id="cryptoSelect">
              <option v-for="crypto in cryptos" :key="crypto" :value="crypto">
                {{ crypto }}
              </option>
            </select>
          </div>

          <chart-card 
            :chart-data="filteredChartData"
            :chart-options="lineChart.options"
            :responsive-options="lineChart.responsiveOptions"
          >
            <template slot="header">
              <h4 class="card-title">{{ selectedCrypto }} Chart</h4>
              <p class="card-category">24 Hours performance</p>
            </template>
            <template slot="footer">
              <div class="legend">
                <i class="fa fa-circle text-info"></i> Bitcoin
                <i class="fa fa-circle text-danger"></i> Ethereum
                <i class="fa fa-circle text-warning"></i> BNB
              </div>
              <hr>
              <div class="stats">
                <i class="fa fa-history"></i> Updated 3 minutes ago
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
                <i class="fa fa-history"></i> Updated 3 minutes ago
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

export default {
  components: {
    LTable,
    ChartCard
  },
  data() {
    return {
      selectedCrypto: "Bitcoin",
      cryptos: ["Bitcoin", "Ethereum", "BNB", "Solana"],
      lineChart: {
        data: {
          labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul'],
          series: {
            Bitcoin: [5, 10, 15, 20, 700, 30, 35],
            Ethereum: [30, 600, 90, 120, 150, 180, 210],
            BNB: [2, 4, 6, 8, 10, 12, 104],
            Solana: [7, 40, 21, 500, 35, 240, 49]
          }
        },
        options: {
          low: 0,
          high: 800,
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
        responsiveOptions: [
          [
            'screen and (max-width: 640px)',
            {
              axisX: {
                labelInterpolationFnc(value) {
                  return value[0];
                }
              }
            }
          ]
        ]
      },
      tableData: {
        data: [
          { nom: 'Bitcoin', prix: 10505 },
          { nom: 'Ethereum', prix: 10405 },
          { nom: 'BNB', prix: 5007 },
          { nom: 'Solana', prix: 7087 }
        ]
      }
    };
  },
  computed: {
    filteredChartData() {
      return {
        labels: this.lineChart.data.labels,
        series: [this.lineChart.data.series[this.selectedCrypto]]
      };
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
