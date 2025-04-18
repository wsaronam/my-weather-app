<script setup lang="ts">
  import { ref, onMounted } from "vue";
  import { useRouter } from 'vue-router';
  import { displayWeather } from "../utils/weatherdata";

  const router = useRouter();

  const {
    cityName,
    temperature,
    condition,
    humidity,
    windSpeed,
    pressure,
    fiveDayForecast,
    processWeatherData,
  } = displayWeather();


  onMounted(() => {
    const jsonData = localStorage.getItem('weatherData');
    if (!jsonData) {
      return;
    }

    const data = JSON.parse(jsonData);
    if (data.message?.startsWith('404')) {
      return;
    }
    
    processWeatherData(data);
  });


  function goBackHome() {
    router.push('/');
  }

</script>




<template>
  <div>
    <h1>{{ cityName }}</h1>
    <div>
      <p>{{ temperature }} °F</p>
      <p>{{ condition }}</p>
      <p>Humidity: {{ humidity }}%</p>
      <p>Wind: {{ windSpeed }} m/s</p>
      <p>Pressure: {{ pressure }} hPa</p>
    </div><br>
    <div id="forecast">
      <div v-for="(item) in fiveDayForecast" class="forecast-item">
        <p><strong>{{ item.day }}</strong></p>
        <img :src="`https://openweathermap.org/img/wn/${item.icon}@2x.png`" alt="Weather Icon" />
        <p>{{ item.temp }}°F</p>
      </div>
    </div>
    <button @click="goBackHome" class="back-button">Back to Search</button>
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

.back-button {
  margin-top: 20px;
  padding: 10px 16px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.back-button:hover {
  background-color: #45a049;
}
</style>
