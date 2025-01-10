<template>
  <div class="wallet-manager">
    <h2>Gestion des portefeuilles</h2>
    <div class="tabs">
      <button 
        :class="{ active: activeTab === 'wallets' }" 
        @click="activeTab = 'wallets'"
      >
        Portefeuille
      </button>
      <button 
        :class="{ active: activeTab === 'funds' }" 
        @click="activeTab = 'funds'"
      >
        Fonds
      </button>
    </div>
    <div class="tab-content">
      <Wallets v-if="activeTab === 'wallets'" />
      <Funds v-else />
    </div>
  </div>
</template>

<script>
import Wallets from "@/components/WalletsManager.vue";
import Funds from "@/components/FundsManager.vue";

export default {
  components: {
    Wallets,
    Funds,
  },
  data() {
    return {
      activeTab: "wallets", // Tab active par défaut
    };
  },
};
</script>

<style scoped>
body {
  margin: 0;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
}

.wallet-manager {
  padding: 20px;
  background-color: #1e1e1e;
  color: #e4e4e4;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.6);
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
}

h2 {
  text-align: center;
  color: #00ff88;
}

.tabs {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.tabs button {
  padding: 10px 20px;
  margin: 0 10px;
  background-color: #333;
  color: #e4e4e4;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease-in-out;
}

.tabs button.active {
  background-color: #00ff88;
  color: #121212;
}

.tabs button:hover {
  background-color: #00ff88;
}

.tab-content {
  padding: 10px;
  width: 100%;
}
</style>
