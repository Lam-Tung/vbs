import {createRouter, createWebHistory} from 'vue-router'
import Calender from "../views/Calender.vue";


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
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/Vehicles.vue')
    }
  ]
})

export default router
