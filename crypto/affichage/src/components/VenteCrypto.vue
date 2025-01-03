<template>
    <div class="sell-announcement">
      <!-- Titre de la page -->
      <h1>Créer une Annonce de Vente</h1>
  
      <!-- Formulaire de création d'annonce -->
      <form @submit.prevent="submitForm" class="announcement-form">
        <div class="form-group">
          <label for="cryptocurrency">Cryptomonnaie</label>
          <select v-model="form.cryptocurrency" id="cryptocurrency" class="input-field">
            <option disabled value="">Sélectionner une cryptomonnaie</option>
            <option v-for="(crypto, index) in cryptocurrencies" :key="index" :value="crypto">
              {{ crypto }}
            </option>
          </select>
        </div>
  
        <div class="form-group">
          <label for="quantity">Quantité à vendre</label>
          <input
            type="number"
            id="quantity"
            v-model="form.quantity"
            min="0.01"
            step="0.01"
            placeholder="Entrez la quantité"
            class="input-field"
          />
        </div>
  
        <div class="form-group">
          <label for="price">Prix par unité (en €)</label>
          <input
            type="number"
            id="price"
            v-model="form.price"
            min="0.01"
            step="0.01"
            placeholder="Entrez le prix"
            class="input-field"
          />
        </div>
  
        <button type="submit" class="submit-btn">Publier l'annonce</button>
      </form>
  
      <!-- Message de confirmation -->
      <div v-if="message" class="confirmation-message">{{ message }}</div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        // Données de base pour les cryptomonnaies
        cryptocurrencies: ['Bitcoin', 'Ethereum', 'Litecoin', 'Ripple', 'Cardano'],
        // Formulaire de vente
        form: {
          cryptocurrency: '',
          quantity: '',
          price: '',
        },
        message: '',
      };
    },
    methods: {
      // Soumettre le formulaire
      submitForm() {
        // Validation du formulaire
        if (!this.form.cryptocurrency || !this.form.quantity || !this.form.price) {
          this.message = 'Tous les champs doivent être remplis.';
          return;
        }
  
        // Logique pour publier l'annonce (peut inclure des appels API, etc.)
        this.message = `Annonce publiée avec succès ! Vous vendez ${this.form.quantity} ${this.form.cryptocurrency} à ${this.form.price} € chacun.`;
  
        // Réinitialiser le formulaire
        this.form.cryptocurrency = '';
        this.form.quantity = '';
        this.form.price = '';
      },
    },
  };
  </script>
  
  <style scoped>
  .sell-announcement {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 2rem;
    background: #1c1c1c;
    border-radius: 12px;
    width: 100%;
    height: 100%;
    min-height: 100vh;
    color: white;
    box-sizing: border-box;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
  }
  
  h1 {
    font-size: 2.2rem;
    font-weight: 600;
    margin-bottom: 20px;
    color: #f39c12;
  }
  
  .announcement-form {
    width: 100%;
    max-width: 600px;
    margin-top: 30px;
    background: #333;
    padding: 2rem;
    border-radius: 12px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
  }
  
  .form-group {
    margin-bottom: 15px;
  }
  
  label {
    display: block;
    font-size: 1rem;
    color: #ddd;
    margin-bottom: 5px;
  }
  
  .input-field {
    width: 100%;
    padding: 10px;
    font-size: 1rem;
    border: 1px solid #444;
    border-radius: 8px;
    background-color: #222;
    color: white;
  }
  
  .input-field:focus {
    outline: none;
    border-color: #f39c12;
  }
  
  .submit-btn {
    width: 100%;
    padding: 12px;
    font-size: 1.1rem;
    background-color: #3498db;
    border: none;
    color: white;
    border-radius: 8px;
    cursor: pointer;
    transition: background 0.3s;
  }
  
  .submit-btn:hover {
    background-color: #00cc6a;
  }
  
  .confirmation-message {
    margin-top: 20px;
    color: #2ecc71;
    font-size: 1.2rem;
  }
  
  @media (max-width: 768px) {
    .sell-announcement {
      padding: 1rem;
    }
  
    h1 {
      font-size: 1.8rem;
    }
  
    .announcement-form {
      padding: 1.5rem;
    }
  
    .form-group {
      margin-bottom: 12px;
    }
  
    .input-field {
      padding: 8px;
    }
  
    .submit-btn {
      padding: 10px;
      font-size: 1rem;
    }
  }
  
  @media (max-width: 480px) {
    h1 {
      font-size: 1.5rem;
      text-align: center;
    }
  
    .announcement-form {
      padding: 1.2rem;
    }
  
    .form-group {
      margin-bottom: 10px;
    }
  
    .input-field {
      padding: 7px;
      font-size: 0.9rem;
    }
  
    .submit-btn {
      padding: 10px;
      font-size: 1rem;
    }
  }
  </style>
  