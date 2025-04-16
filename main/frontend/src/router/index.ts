import { createRouter, createWebHistory } from 'vue-router'
import HomeScreen from '../components/HomeScreen.vue'
import WeatherScreen from '../components/WeatherScreen.vue'

const routes = [
  { path: '/', 
    name: 'Home', 
    component: HomeScreen 
  },
  { path: '/weather', 
    name: 'Weather', 
    component: WeatherScreen 
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router