<template>
  <div v-if="!isLoggedIn" class="auth-container">
     <div class="left-panel">
       <AdminAut @login-success-admin="goToAccueilAdmin"></AdminAut>
     </div>
     <div class="right-panel">
       <UserAuth @login-success="goToAccueil"></UserAuth>
     </div>
   </div>

   <!-- Affichage du menu et du contenu principal si l'utilisateur est connecté -->
   <div v-else-if="userRole" class="authenticated-container" style="flex-direction: column;">
     <div :class="{'nav-open': $sidebar.showSidebar}">
       <notifications></notifications>
       <router-view></router-view>
     </div>
   </div>
 
</template>

<script>
import UserAuth from 'src/pages/UserAuth.vue';
import AdminAut from 'src/pages/AdminAuth.vue';
 export default {
   data() {
   return {
     isLoggedIn: false,
     userRole: 'non'
     }
   },
   components: {
     UserAuth,
     AdminAut},
   methods: {
   goToAccueil(role) {
     this.isLoggedIn = true;  // L'utilisateur est maintenant connecté
     this.$router.push('/user/overview');
   },

   goToAccueilAdmin() {
     this.isLoggedIn = true;  // L'utilisateur est maintenant connecté
     this.$router.push('/admin/demande');
   }
   }
 }
</script>
<style lang="scss">
 .vue-notifyjs.notifications{
   .list-move {
     transition: transform 0.3s, opacity 0.4s;
   }
   .list-item {
     display: inline-block;
     margin-right: 10px;

   }
   .list-enter-active {
     transition: transform 0.2s ease-in, opacity 0.4s ease-in;
   }
   .list-leave-active {
     transition: transform 1s ease-out, opacity 0.4s ease-out;
   }

   .list-enter {
     opacity: 0;
     transform: scale(1.1);

   }
   .list-leave-to {
     opacity: 0;
     transform: scale(1.2, 0.7);
   }
 }

 .auth-container {
 display: flex;
 height: 584px;
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
 animation: zoomInRotate 1s ease-out; /* Animation au chargement */
 transition: transform 0.4s ease-in-out, box-shadow 0.4s ease-in-out;

 &:hover {
   transform: scale(1.2) rotate(3deg); /* Effet au survol */
   box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
 }

 &:active {
   transform: scale(0.95) rotate(-2deg); /* Effet au clic */
 }
}

/* Animation de chargement */
@keyframes zoomInRotate {
 from {
   opacity: 0;
   transform: scale(0.5) rotate(-10deg);
 }
 to {
   opacity: 1;
   transform: scale(1) rotate(0);
 }
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
