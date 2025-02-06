<template>
  <div class="signup">
    <h2>Inscription</h2>
    <form @submit.prevent="register">
      <div class="input-group">
        <label for="name">Nom</label>
        <input v-model="name" type="text" id="name" required placeholder="Votre nom" />
      </div>
      <div class="input-group">
        <label for="email">Email</label>
        <input v-model="email" type="email" id="email" required placeholder="Votre email" />
      </div>
      <div class="input-group">
        <label for="password">Mot de passe</label>
        <input v-model="password" type="password" id="password" required placeholder="Mot de passe" />
      </div>
      <button type="submit">S'inscrire</button>
    </form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      name: '',
      email: '',
      password: '',
    };
  },
  methods: {
    async register() {
      // Données à envoyer à l'API
      const utilisateur = {
        Nom: this.name,
        Email: this.email,
        MotDePasse: this.password,
      };

      try {
        // Envoi de la requête POST à l'API .NET
        const response = await fetch('http://localhost:5000/inscription', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(utilisateur),
        });

        // Vérification du statut de la réponse
        if (response.ok) {
          const responseData = await response.json();
          alert("Inscription réussie. Un email de validation a été envoyé."); // Afficher la réponse du serveur
        } else {
          const errorData = await response.json();
          alert('Erreur : ' + errorData); // Afficher un message d'erreur
        }
      } catch (error) {
        console.log(error.message); // Corrected error logging
      }
    },
  },
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
  background-color: #121212; /* Dark background */
  color: #e4e4e4; /* Light text for contrast */
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.signup {
  background-color: #1e1e1e; /* Dark background for the form */
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
  .signup {
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
