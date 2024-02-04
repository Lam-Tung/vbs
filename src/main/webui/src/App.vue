<script setup lang="ts">
import {RouterView} from 'vue-router'

const items = [
  { title: 'Calendar', icon: 'mdi-calendar', to: '/calendar' },
  { title: 'Vehicles', icon: 'mdi-car-multiple', to: '/vehicles' },
  { title: 'User', icon: 'mdi-account-multiple', to: '/user' },
]
</script>

<template>
  <v-app>
    <!-- Side Drawer -->
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
        ></v-list-item>
      </v-list>
    </v-navigation-drawer>

    <!-- Main Content Area -->
    <v-main>
      <v-container>
        <RouterView />
      </v-container>
    </v-main>
  </v-app>
</template>

<style scoped>
</style>