<script setup lang="ts">

import {inject} from "vue";
import {VehicleServiceKey} from "@/injection-keys";

const vehicleService = inject(VehicleServiceKey);
</script>

<template>
  hello from vehicles!
</template>

<style scoped>

</style>