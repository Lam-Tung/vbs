<script setup lang="ts">
import SideDrawer from "./components/SideDrawer.vue";
</script>

<template>
  <v-app>
    <!-- Side Drawer -->
    <SideDrawer />

    <!-- Main Content Area -->
    <router-view />

  </v-app>
</template>

<style scoped>
</style>