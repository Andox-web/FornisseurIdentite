<template>
  <div id="app" class="app-container">
    <!-- Affichage du composant UserAuth si l'utilisateur n'est pas connecté -->
    <div v-if="!isLoggedIn" class="auth-container">
      <div class="left-panel">
        <img alt="Vue logo" class="logo" src="./assets/logo.png">
      </div>
      <div class="right-panel">
        <UserAuth @login-success="goToAccueil"></UserAuth>
      </div>
    </div>

    <!-- Affichage du menu et du contenu principal si l'utilisateur est connecté -->
    <div v-else class="authenticated-container">
      <div class="sidebar">
        <ul>
          <li :class="{ active: currentComponent === 'Dashboard' }" @click="selectComponent('Dashboard')">Dashboard</li>
          <li :class="{ active: currentComponent === 'Transactions' }" @click="selectComponent('Transactions')">Transactions</li>
          <li :class="{ active: currentComponent === 'Portefeuille' }" @click="selectComponent('Portefeuille')">Portefeuille</li>
          <li :class="{ active: currentComponent === 'Market' }" @click="selectComponent('Market')">Market</li>
        </ul>
        <div class="user-info">
          <p><strong>Nom de l'utilisateur :</strong> John Doe</p>
          <p><strong>Email :</strong> john.doe@example.com</p>
        </div>
      </div>

      <div class="main-content">
        <component :is="currentComponent" :key="currentComponent"></component>
      </div>
    </div>
  </div>
</template>

<script>
import Dashboard from './views/CryptoDashboard.vue'
import Transactions from './components/HistoricTransaction.vue'
import Portefeuille from './views/HomeView.vue'
import Market from './components/VenteCrypto.vue'
import UserAuth from './components/UserAuth.vue'

export default {
  name: 'App',
  data() {
    return {
      isLoggedIn: false,
      currentComponent: 'Dashboard'
    }
  },
  components: {
    Dashboard,
    Transactions,
    Portefeuille,
    Market,
    UserAuth
  },
  methods: {
    selectComponent(component) {
      this.currentComponent = component;
    },
    goToAccueil() {
      console.log("L'événement a été capté, je vais rediriger !");
      this.isLoggedIn = true;  // L'utilisateur est maintenant connecté
    }
  }
}
</script>

<style scoped>
/* Styles pour l'authentification */
.auth-container {
  display: flex;
  height: 100%;
  width: inherit;
}

.left-panel {
  flex: 1;
  background: #2c3e50;
  display: flex;
  justify-content: center;
  align-items: center;
  animation: fadeInLeft 1s ease-out;
}

.right-panel {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #34495e;
  animation: fadeInRight 1s ease-out;
}

.logo {
  width: 500px;
  transition: transform 0.3s ease-in-out;
}

.logo:hover {
  transform: scale(1.1);
}

@keyframes fadeInLeft {
  from {
    opacity: 0;
    transform: translateX(-100%);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes fadeInRight {
  from {
    opacity: 0;
    transform: translateX(100%);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

/* Styles pour le menu et le contenu principal après connexion */
.authenticated-container {
  display: flex;
  height: 100%;
  width: inherit;
}

.sidebar {
  width: 250px;
  background-color: #333;
  color: white;
  padding: 20px;
  overflow-y: auto;
  height: 100vh;
  display: flex;
  flex-direction: column;
  box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
}

.sidebar ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

.sidebar li {
  padding: 15px;
  cursor: pointer;
  text-align: left;
  transition: background-color 0.3s ease, padding-left 0.3s ease;
}

.sidebar li:hover {
  background-color: #444;
  padding-left: 20px;
}

.sidebar li.active {
  background-color: #555;
}

.user-info {
  margin-top: auto;
  padding: 20px;
  background-color: #444;
  border-radius: 5px;
  font-size: 14px;
  color: white;
}

.main-content {
  flex-grow: 1;
  overflow-y: auto;
  height: 100vh;
  background-color: #f4f4f4;
  box-sizing: border-box;
}

@media (max-width: 768px) {
  .sidebar {
    width: 200px;
  }

  .main-content {
    padding: 10px;
  }
}
</style>
