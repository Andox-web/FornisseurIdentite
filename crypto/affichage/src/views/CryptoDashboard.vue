<template>
  <div class="crypto-dashboard">
    <!-- Conteneur des boutons de commutation -->
    <div class="button-container">
      <button
        :class="{'active': isShowingCours}"
        @click="toggleView('cours')"
        class="toggle-button"
      >
        Voir Cours Crypto
      </button>
      <button
        :class="{'active': !isShowingCours}"
        @click="toggleView('annonces')"
        class="toggle-button"
      >
        Voir Annonces
      </button>
    </div>

    <!-- Affichage du contenu en fonction de l'état -->
    <div v-if="isShowingCours" class="panel">
      <CoursCrypto />
    </div>
    <div v-else class="panel">
      <AnnoncesCrypto />
    </div>
  </div>
</template>

<script>
import CoursCrypto from '@/components/CoursCrypto.vue';
import AnnoncesCrypto from '@/components/AnnonceCrypto.vue';

export default {
  components: {
    CoursCrypto,
    AnnoncesCrypto,
  },
  data() {
    return {
      isShowingCours: true, // Par défaut, afficher CoursCrypto
    };
  },
  methods: {
    toggleView(view) {
      this.isShowingCours = view === 'cours';
    },
  },
};
</script>

<style scoped>
.crypto-dashboard {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  padding: 2rem;
  background: #222;
  width: 100%;
  height: 100vh;
  overflow-y: auto; /* Permet le défilement vertical */
}

.button-container {
  width: 100%;
  display: flex;
  justify-content: center;
  gap: 20px; /* Espace entre les boutons */
  margin-bottom: 20px;
  margin-top: 35px;
}

.toggle-button {
  padding: 10px 30px;
  background: #444;
  border: 2px solid transparent;
  color: white;
  font-size: 1.1rem;
  font-weight: 600;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
}

.toggle-button:hover {
  background: #00ff88;
  border-color: #00ff88;
}

.toggle-button.active {
  background: #00ff88;
  border-color: #00ff88;
}

.panel {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #333;
  padding: 2rem;
  border-radius: 8px;
  transition: opacity 0.5s ease-in-out, transform 0.3s ease;
  min-height: 100%; /* Assure que le contenu prend toute la hauteur disponible */
}

.panel-enter-active, .panel-leave-active {
  transition: opacity 0.5s, transform 0.5s;
}

.panel-enter, .panel-leave-to {
  opacity: 0;
  transform: scale(0.95);
}
</style>
