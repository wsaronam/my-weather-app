<script setup lang="ts">
import HomeScreen from './components/HomeScreen.vue'

import TitleCard from './components/TitleCard.vue'
import TheWelcome from './components/examples/TheWelcome.vue'
</script>

<template>
  <header>
    <img alt="One Piece Log Pose" class="logo" src="https://static.wikia.nocookie.net/onepiece/images/e/ef/Paradise_Log_Pose.png" 
    width="125" height="125" />

    <div class="wrapper">
      <TitleCard msg="My Weather App" />
    </div>
  </header>

  <main>
    <HomeScreen />
    <TheWelcome />
  </main>
</template>

<style scoped>
header {
  line-height: 1.5;
}

.logo {
  display: block;
  margin: 0 auto 2rem;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  .logo {
    margin: 0 2rem 0 0;
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }
}
</style>
