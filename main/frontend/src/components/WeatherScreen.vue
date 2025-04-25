<script setup lang="ts">
  import { ref, onMounted } from "vue";
  import { useRouter } from 'vue-router';
  import { displayWeather, changeTempUnits } from "../utils/weatherdata";

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

  const unit = ref('imperial');


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

  function getConvertedTemp(temp: number) {
    return changeTempUnits(temp, unit.value);
  }

</script>




<template>
  <div>
    <h1>{{ cityName }}</h1>
    <select v-model="unit" class="dropdown-menu">
      <option value="imperial">Fahrenheit (°F)</option>
      <option value="metric">Celsius (°C)</option>
      <option value="kelvin">Kelvin (K)</option>
    </select><br>
    <div>
      <p>{{ getConvertedTemp(Number(temperature)) }}</p>
      <p>{{ condition }}</p>
      <p>Humidity: {{ humidity }}%</p>
      <p>Wind: {{ windSpeed }} m/s</p>
      <p>Pressure: {{ pressure }} hPa</p>
    </div><br>
    <div id="forecast" class="forecast-container">
      <div v-for="(item) in fiveDayForecast" class="forecast-item">
        <p><strong>{{ item.day }}</strong></p>
        <img :src="`https://openweathermap.org/img/wn/${item.icon}@2x.png`" alt="Weather Icon" />
        <p>High: {{ getConvertedTemp(Number(item.high)) }}</p>
        <p>Low: {{ getConvertedTemp(Number(item.low)) }}</p>
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

.dropdown-menu {
  padding: 0.75rem 1rem;
  margin-bottom: 10px;
  border-radius: 12px;
  border: 1px solid #ccc;
  font-size: 16px;
  background-color: #f9fafb;
  color: #111827;
  box-shadow: 0 2px 6px black;
  cursor: pointer;
}

.dropdown-menu:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px darkblue;
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
  transform: translateY(-1px);
}

.forecast-container {
  display: flex;
  flex-direction: row;         
  justify-content: space-around;  
  flex-wrap: wrap;             
  gap: 1rem;                   
}

</style>
