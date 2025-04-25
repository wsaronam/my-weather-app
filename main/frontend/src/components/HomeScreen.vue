<script setup lang="ts">
  import { ref } from "vue";
  import { useRouter } from 'vue-router';
  import { locateUser } from "../utils/geolocation";
  import { searchForWeatherData } from '../utils/weatherdata';

  const router = useRouter();
  const cityInput = ref("");

  function getLocation() {
    locateUser((cityName) => {
      console.log("City from location:", cityName);
      cityInput.value = cityName;
    });
  }

  async function handleFormSubmit() {
    const weatherData = await searchForWeatherData(cityInput.value);
    router.push('/weather');
  }
</script>




<template>
  <div>
    <h3>
      Enter a city name to find some basic weather information about it:
    </h3>
        <form @submit.prevent="handleFormSubmit">
            <input type="text" v-model="cityInput" id="cityName" class="city-input" placeholder="Enter city name" />
            <button type="submit" class="get-weather-button">Get Weather</button>
        </form>
        <button @click="getLocation" class="locate-me-button">Locate Me!</button>
  </div>
</template>




<style scoped>
h1 {
  font-weight: 500;
  font-size: 2.6rem;
  position: relative;
  top: -10px;
}

h3 {
  font-size: 1.2rem;
}

.greetings h1,
.greetings h3 {
  text-align: center;
}

@media (min-width: 1024px) {
  .greetings h1,
  .greetings h3 {
    text-align: left;
  }
}

.city-input {
  width: 100%;
  max-width: 400px;
  padding: 0.75rem 1rem;
  border: 1px solid #ccc;
  border-radius: 12px;
  font-size: 16px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  outline: none;
}

.city-input:focus {
  border-color: #3bf66a; 
  box-shadow: 0 0 0 3px rgba(23, 77, 1, 0.4);
}

.get-weather-button {
  padding: 0.6rem 1.2rem;
  margin: 0.5rem;
  background-color: #4caf50; 
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 1rem;
  cursor: pointer;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.get-weather-button:hover {
  background-color: #45a049; 
  transform: translateY(-1px);
}

.get-weather-button:active {
  transform: scale(0.98);
}

.locate-me-button {
  padding: 0.3rem 0.6rem;
  background-color: #10b981;
  border: none;
  border-radius: 12px;
  font-size: 1rem;
  cursor: pointer;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  margin-top: 10px;
}

.locate-me-button:hover {
  background-color: #059669;
}
</style>
