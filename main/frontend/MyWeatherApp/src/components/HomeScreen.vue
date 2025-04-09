<script setup lang="ts">
</script>

<template>
  <div>
    <h3>
      Enter a city name to find some basic weather information about it:
    </h3>
    <form onsubmit="">
            <input type="text" id="cityName" placeholder="Enter city name" required>
            <button type="submit">Get Weather</button>
        </form>
        <button onclick="">Locate Me!</button>
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
</style>
