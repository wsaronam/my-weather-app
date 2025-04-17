import { ref } from 'vue';




// This function will use the API to search for a city's weather data
export async function searchForWeatherData(city: string): Promise<any> {
    if (city.trim() !== '') {
      const apiUrl = `http://localhost:8080/api/weather/${encodeURIComponent(city)}`;
  
      try {
        const response = await fetch(apiUrl);
        const data = await response.json();
  
        localStorage.setItem('weatherData', JSON.stringify(data));
        return data;
      } 
      catch (error) {
        console.error('Error fetching weather data:', error);
      }
    }
}


// This function will take the JSON data from the searchForWeatherData function and prepare it for display on HTML
// This function will also take the Five-Day forecast and prepare it for display on HTML
//     Maybe we should separate this into a separate function
export function displayWeather() {
  const weatherData = ref<any>(null);
  const cityName = ref('');
  const temperature = ref('');
  const condition = ref('');
  const humidity = ref('');
  const windSpeed = ref('');
  const pressure = ref('');

  function processWeatherData(rawData: any) {
    const current = rawData.currentWeather;
    cityName.value = `${current.name}, ${current.sys.country}`;
    temperature.value = current.main.temp;
    condition.value = `${current.weather[0].main} - ${current.weather[0].description}`;
    humidity.value = current.main.humidity;
    windSpeed.value = current.wind.speed;
    pressure.value = current.main.pressure;

    weatherData.value = rawData;
  }

  return {
    cityName,
    temperature,
    condition,
    humidity,
    windSpeed,
    pressure,
    processWeatherData,
  };
}