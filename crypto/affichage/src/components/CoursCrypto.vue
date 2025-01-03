<template>
    <div class="cours-crypto">
      <h2>Cours des Cryptomonnaies</h2>
      <canvas id="cryptoChart"></canvas>
    </div>
  </template>
  
  <script>
  import { Chart, registerables } from 'chart.js';

  
  // Enregistrement des composants nécessaires
  Chart.register(...registerables);
  
  export default {
    mounted() {
      // Dessiner le graphique après le montage du composant
      this.renderChart();
    },
    methods: {
      renderChart() {
        const ctx = document.getElementById('cryptoChart').getContext('2d');
        new Chart(ctx, {
          type: 'line', // Type de graphique
          data: {
            labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May'], // Labels pour l'axe des X
            datasets: [
              {
                label: 'Prix Bitcoin',
                data: [50000, 52000, 54000, 53000, 54000], // Données du graphique
                borderColor: 'rgb(255, 99, 132)', // Couleur de la ligne
                backgroundColor: 'rgba(255, 99, 132, 0.2)', // Couleur de fond (transparente)
                fill: false, // Ne pas remplir sous la ligne
              },
            ],
          },
          options: {
            responsive: true,
            scales: {
              x: {
                beginAtZero: true, // Début de l'axe X à zéro
              },
              y: {
                beginAtZero: true, // Début de l'axe Y à zéro
              },
            },
          },
        });
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
  }
  
  h2 {
    text-align: center;
    font-size: 1.5rem;
  }
  
  canvas {
    max-width: 100%;
    height: 300px;
    margin: auto;
    display: block;
  }
  </style>
  