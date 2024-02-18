<script setup lang="ts">
import {VehicleService} from "@/services/vehicle-service";
import {provide} from "vue";
import {VehicleServiceKey} from "@/injection-keys";
import SideDrawer from "@/components/SideDrawer.vue";

const vehicleService = new VehicleService();
provide(VehicleServiceKey, vehicleService);
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