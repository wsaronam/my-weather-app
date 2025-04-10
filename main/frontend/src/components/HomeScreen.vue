<template>
  <div>
    <h3>
      Enter a city name to find some basic weather information about it:
    </h3>
        <form>
            <input type="text" v-model="cityInput" id="cityName" placeholder="Enter city name" />
            <button type="submit">Get Weather</button>
        </form>
        <button @click="getLocation">Locate Me!</button>
  </div>
</template>


<script setup lang="ts">
  import { ref } from "vue";
  import { locateUser } from "@/utils/geolocation";

  const cityInput = ref("");

  function getLocation() {
    locateUser((cityName) => {
      console.log("City from location:", cityName);
      cityInput.value = cityName;
    });
  }
</script>


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
</style>
