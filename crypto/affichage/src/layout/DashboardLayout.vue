<template>
  <div class="wrapper">
    <side-bar>
      <mobile-menu slot="content"></mobile-menu>
      <sidebar-link to="/user/user">
        <i class="nc-icon nc-circle-09"></i>
        <p>User Profile</p>
      </sidebar-link>
      <sidebar-link to="/user/overview">
        <i class="nc-icon nc-chart-bar-32"></i>
        <p>Dashboard crypto</p>
      </sidebar-link>
      <sidebar-link to="/user/portefeuille">
        <i class="nc-icon nc-bank"></i>
        <p>Portefeuille</p>
      </sidebar-link>
      <sidebar-link to="/user/annonce">
        <i class="nc-icon nc-notes"></i>
        <p>Annonces</p>
      </sidebar-link>
      <sidebar-link to="/user/historic">
        <i class="nc-icon nc-paper-2"></i>
        <p>Historiques des transaction</p>
      </sidebar-link>
      <sidebar-link to="/user/vente">
        <i class="nc-icon nc-money-coins"></i>
        <p>Vendre</p>
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
