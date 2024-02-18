<script setup lang="ts">
import {useRouter} from "vue-router";

const items = [
  { title: 'Calendar', icon: 'mdi-calendar', to: '/' },
  { title: 'Vehicles', icon: 'mdi-car-multiple', to: '/vehicles' },
  { title: 'User', icon: 'mdi-account-multiple', to: '/users' },
]

const router = useRouter();

const navigateTo = (to: string) => {
  router.push(to);
};
</script>

<template>
  <v-navigation-drawer
      expand-on-hover
      rail
  >
    <v-list>
      <v-list-item
          v-for="item in items"
          :key="item.title"
          :prepend-icon="item.icon"
          :title="item.title"
          @click="navigateTo(item.to)"
      />
    </v-list>
  </v-navigation-drawer>
</template>

<style scoped>

</style>