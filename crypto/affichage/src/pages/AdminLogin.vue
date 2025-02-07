<template>
  <div class="login">
    <h2>Connexion Admin</h2>
    <form v-if="!pinRequired" @submit.prevent="login">
      <div class="input-group">
        <label for="email">Email</label>
        <input v-model="email" type="email" id="email" required placeholder="Votre adresse email" />
      </div>
      <div class="input-group">
        <label for="password">Mot de passe</label>
        <input v-model="password" type="password" id="password" required placeholder="Votre mot de passe" />
      </div>
      <button type="submit" :disabled="loading">
        {{ loading ? "Connexion..." : "Se connecter" }}
      </button>
    </form>
  
    <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
  </div>
</template>

<script>
export default {
/* eslint-disable no-unused-vars */
data() {
  return {
    email: '',
    password: '',
    pinRequired: false,
    loading: false,
    errorMessage: '',
  };
},
/* eslint-enable no-unused-vars */

  methods: {
    async login() {
      this.loading = true;
      this.errorMessage = '';

      const loginrequest = {
        Email: this.email,
        MotDePasse: this.password,
      };

      try {
        const response = await fetch('http://localhost:5000/login', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(loginrequest),
        });

        const data = await response.json();

        if (!response.ok) {
          // Gérer les erreurs retournées par l'API
          this.errorMessage = data.message || 'Une erreur est survenue.';
          return;
        }

        // Exemple : redirection vers une page
        this.$emit('goToHomeAdmin');

        this.$emit('login-success-admin');
      } catch (error) {
        this.errorMessage;
      } finally {
        this.loading = false;
      }
    }
  }
};


</script>

<style scoped>
/* Global reset for consistent styling */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Arial', sans-serif;
  background-color: #121212; /* Dark background for a crypto feel */
  color: #e4e4e4; /* Light text for contrast */
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.login {
  background-color: #1e1e1e; /* Slightly lighter dark background */
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.6);
  width: 100%;
  max-width: 400px;
}

h2 {
  text-align: center;
  font-size: 24px;
  margin-bottom: 20px;
  color: #00ff88; /* Crypto green accent */
}

.input-group {
  margin-bottom: 20px;
}

.input-group label {
  display: block;
  font-size: 14px;
  color: #bbb;
  margin-bottom: 5px;
}

.input-group input {
  width: 100%;
  padding: 12px;
  background-color: #333; /* Dark input background */
  color: #e4e4e4;
  border: 2px solid #444;
  border-radius: 4px;
  font-size: 16px;
  transition: border-color 0.3s ease-in-out;
}

.input-group input:focus {
  border-color: #00ff88; /* Green border on focus */
  outline: none;
}

button {
  width: 100%;
  padding: 12px;
  background-color: #00ff88; /* Crypto green */
  border: none;
  border-radius: 4px;
  color: white;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s ease-in-out;
}

button:hover {
  background-color: #00cc6a; /* Darker green on hover */
}

button:focus {
  outline: none;
}

@media (max-width: 400px) {
  .login {
    padding: 20px;
    width: 90%;
  }

  h2 {
    font-size: 20px;
  }

  .input-group input, button {
    padding: 10px;
    font-size: 14px;
  }
}
</style>
