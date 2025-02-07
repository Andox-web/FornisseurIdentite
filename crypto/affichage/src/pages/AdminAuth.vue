<script>
import AdminLogin from 'src/pages/AdminLogin.vue';

export default {
  components: {
    AdminLogin
  },
  methods: {
  async goMenu(){
    this.$emit('login-success-admin');
  }
  },
};
</script>

<template>
  <div class="adminauth">
    <div class="adminauth-content">
      <AdminLogin @goToHomeAdmin="goMenu"/>
    </div>
  </div>
</template>

<style scoped>
.adminauth {
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

.adminauth-content {
  background-color: rgba(33, 43, 54, 0.8);
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.adminauth-content input,
.adminauth-content button {
  width: 100%;
  margin-bottom: 15px;
}
</style>
