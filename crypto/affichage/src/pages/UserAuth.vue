<script>
import Signup from 'src/pages/UserSignup.vue';
import Login from 'src/pages/UserLogin.vue';

export default {
  data() {
    return {
      currentPage: 'login', // Définit la page affichée par défaut
    };
  },
  components: {
    Signup,
    Login,
  },
  methods: {
    switchPage(page) {
      this.currentPage = page;
    },
  async goMenu(){
    this.$emit('login-success');
  }
  },
};
</script>

<template>
  <div class="auth">
    <div class="auth-header">
      <button @click="switchPage('login')" :class="{ active: currentPage === 'login' }">Connexion</button>
      <button @click="switchPage('signup')" :class="{ active: currentPage === 'signup' }">Inscription</button>
    </div>
    <div class="auth-content">
      <Login @goToHome="goMenu" v-if="currentPage === 'login'" />
      <Signup v-if="currentPage === 'signup'" />
    </div>
  </div>
</template>

<style scoped>
.auth {
  max-width: 380px;
  width: 100%;
  text-align: center;
  padding: 40px;
  background-color: rgba(33, 43, 54, 0.85);
  border-radius: 15px;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.4);
  backdrop-filter: blur(10px);
  transition: all 0.3s ease-in-out;
}

/* .auth:hover {
  transform: scale(1.05);
} */

.auth-header {
  display: flex;
  justify-content: space-around;
  margin-bottom: 20px;
}

button {
  padding: 12px 24px;
  font-size: 16px;
  border: none;
  background-color: #2c3e50;
  color: #fff;
  border-radius: 8px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
}

button:before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 300%;
  height: 300%;
  background-color: #3838392b;
  transition: all 0.5s ease;
  transform: translate(-50%, -50%);
  border-radius: 50%;
  z-index: 0;
}

button:hover:before {
  width: 0;
  height: 0;
}

button:hover {
  color: #fff;
  font-weight: bold;
}

button.active {
  border: 2px solid #007bff;
  font-weight: bold;
  color: #007bff;
}

.auth-content {
  background-color: rgba(33, 43, 54, 0.8);
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.auth-content input,
.auth-content button {
  width: 100%;
  margin-bottom: 15px;
}
</style>
