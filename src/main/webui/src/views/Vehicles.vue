<script setup lang="ts">

import {inject, onMounted, ref} from "vue";
import {VehicleServiceKey} from "@/injection-keys";
import type {VehicleService} from "@/services/vehicle-service";

const vehicleService: VehicleService = inject(VehicleServiceKey);

const vehicles = ref<VehicleDTO[]>([]);

onMounted(async () => {
  vehicles.value = await vehicleService?.getVehicles();
});
</script>

<template>
  hello from vehicles!
  <div v-if="vehicles.length > 0"> <h2>List of Vehicles</h2>
    <ul>
      <li v-for="vehicle in vehicles" :key="vehicle.id">
        <p>ID: {{ vehicle.id }}</p>
        <p>Name: {{ vehicle.name }}</p>
        <p>License Plate: {{ vehicle.licensePlate }}</p>
        <p>Manufacturer: {{ vehicle.manufacturer }}</p>
        <p>Model: {{ vehicle.model }}</p>
      </li>
    </ul>
  </div>
  <p v-else>No vehicles found.</p>
</template>

<style scoped>

</style>