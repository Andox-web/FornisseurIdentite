<template>
  <div class="cours-crypto">
    <h2>Cours des Cryptomonnaies</h2>
    <div class="dropdown-container">
      <label for="crypto-select">Choisir une cryptomonnaie :</label>
      <select id="crypto-select" v-model="selectedCrypto" @change="updateChart">
        <option v-for="crypto in cryptos" :key="crypto.name" :value="crypto.name">
          {{ crypto.name }}
        </option>
      </select>
    </div>
    <canvas id="cryptoChart"></canvas>
  </div>
</template>

<script>
import { Chart, registerables } from "chart.js";

// Enregistrement des composants nécessaires
Chart.register(...registerables);

export default {
  data() {
    return {
      cryptos: [
        {
          name: "Bitcoin",
          prices: [50000, 52000, 54000, 53000, 54000],
        },
        {
          name: "Ethereum",
          prices: [3000, 3100, 3200, 3150, 3300],
        },
        {
          name: "Ripple",
          prices: [0.8, 0.85, 0.9, 0.88, 0.92],
        },
      ],
      selectedCrypto: "Bitcoin", // Par défaut
      chart: null,
    };
  },
  mounted() {
    this.renderChart();
  },
  methods: {
    renderChart() {
      const ctx = document.getElementById("cryptoChart").getContext("2d");
      const selectedCryptoData = this.cryptos.find(
        (crypto) => crypto.name === this.selectedCrypto
      );

      // Création du graphique
      this.chart = new Chart(ctx, {
        type: "line",
        data: {
          labels: ["Jan", "Feb", "Mar", "Apr", "May"],
          datasets: [
            {
              label: `Prix ${this.selectedCrypto}`,
              data: selectedCryptoData.prices,
              borderColor: "#ffcc00",
              borderWidth: 2,
              backgroundColor: "rgba(255, 204, 0, 0.1)",
              tension: 0.4,
              pointRadius: 0,
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              display: false,
            },
            tooltip: {
              mode: "index",
              intersect: false,
              backgroundColor: "#2e2e3d",
              titleColor: "#fff",
              bodyColor: "#ccc",
            },
          },
          scales: {
            x: {
              grid: {
                color: "#3a3a4d",
              },
              ticks: {
                color: "#ccc",
              },
            },
            y: {
              grid: {
                color: "#3a3a4d",
              },
              ticks: {
                color: "#ccc",
              },
            },
          },
          interaction: {
            mode: "nearest",
            intersect: false,
          },
        },
      });
    },
    updateChart() {
      const selectedCryptoData = this.cryptos.find(
        (crypto) => crypto.name === this.selectedCrypto
      );

      // Si un graphique existe déjà, on le détruit pour éviter les conflits
      if (this.chart) {
        this.chart.destroy();
      }

      // Recréer le graphique avec les nouvelles données
      this.renderChart();
    },
  },
};
</script>

<style scoped>
.cours-crypto {
  padding: 1rem;
  background-color: #1e1e2f;
  color: white;
  border-radius: 8px;
  position: relative;
  height: 500px; /* Ajusté pour inclure le menu déroulant */
}

h2 {
  text-align: center;
  font-size: 1.5rem;
}

canvas {
  max-width: 100%;
  height: 350px; /* Espace réservé pour le graphique */
  margin: auto;
  display: block;
}

.dropdown-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 1rem;
}

label {
  margin-right: 10px;
  font-size: 1rem;
  color: #ccc;
}

select {
  padding: 5px 10px;
  font-size: 1rem;
  border-radius: 5px;
  border: none;
  background-color: #2e2e3d;
  color: #fff;
}
</style>
