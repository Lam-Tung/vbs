import {createRouter, createWebHistory} from 'vue-router'
import Calender from "@/views/Calender.vue";
import Vehicles from "@/views/Vehicles.vue";
import Users from "@/views/Users.vue";


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'calendar',
      component: Calender
    },
    {
      path: '/vehicles',
      name: 'vehicles',
      component: Vehicles
    },
    {
      path: '/users',
      name: 'users',
      component: Users
    }
  ]
})

export default router
