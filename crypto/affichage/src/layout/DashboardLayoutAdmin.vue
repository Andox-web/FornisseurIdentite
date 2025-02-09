<template>
  <div class="wrapper">
    <side-bar>
      <mobile-menu slot="content"></mobile-menu>
      <sidebar-link to="/admin/demande">
        <i class="nc-icon nc-single-copy-04"></i> 
        <p>Demandes</p>
      </sidebar-link>
      <sidebar-link to="/admin/commission">
        <i class="nc-icon nc-money-coins"></i> 
        <p>Commission</p>
      </sidebar-link>
      <sidebar-link to="/admin/commissionanalyse">
        <i class="nc-icon nc-chart-pie-36"></i> 
        <p>Analyse des Commissions</p>
      </sidebar-link>
      <sidebar-link to="/admin/wallets">
        <i class="nc-icon nc-credit-card"></i> 
        <p>Wallets</p>
      </sidebar-link>
      <sidebar-link to="/admin/analyse">
        <i class="nc-icon nc-chart-bar-32"></i> 
        <p>Analyse</p>
      </sidebar-link>
    </side-bar>
    <div class="main-panel">
      <top-navbar></top-navbar>

      <dashboard-content @click="toggleSidebar">

      </dashboard-content>

      <content-footer></content-footer>
    </div>
  </div>
</template>

<style lang="scss">
</style>

<script>
import TopNavbar from './TopNavbar.vue'
import ContentFooter from './ContentFooter.vue'
import DashboardContent from './Content.vue'
import MobileMenu from './MobileMenu.vue'

export default {
  components: {
    TopNavbar,
    ContentFooter,
    DashboardContent,
    MobileMenu
  },
  methods: {
    toggleSidebar () {
      if (this.$sidebar.showSidebar) {
        this.$sidebar.displaySidebar(false)
      }
    }
  }
}
</script>
