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
  const fiveDayForecast = ref<Array<any>>([]);

  function processWeatherData(rawData: any) {
    const current = rawData.currentWeather;
    const timezoneOffset = rawData.fiveDayForecast.city.timezone;  // used later to adjust times
    
    cityName.value = `${current.name}, ${current.sys.country}`;
    temperature.value = current.main.temp;
    condition.value = `${current.weather[0].main} - ${current.weather[0].description}`;
    humidity.value = current.main.humidity;
    windSpeed.value = current.wind.speed;
    pressure.value = current.main.pressure;


    // build dict of date - data pairs (one entry per day)
    const dailyForecasts: Record<string, { max: any, min: any }> = {};
    rawData.fiveDayForecast.list.forEach((entry: any) => {

      // convert our local time to utc time to match api data
      const localTimestamp = entry.dt + timezoneOffset;
      const localDate = new Date(localTimestamp * 1000);
      const dateStr = localDate.toISOString().split('T')[0];

      // get the highest and lowest temperatures of the day
      if (!dailyForecasts[dateStr]) {
        dailyForecasts[dateStr] = {max: entry, min: entry};
      }
      else {
        if (entry.main.temp > dailyForecasts[dateStr].max.main.temp) {  // temp found higher than max
          dailyForecasts[dateStr].max = entry;
        }
        if (entry.main.temp < dailyForecasts[dateStr].min.main.temp) {  // temp found lower than min
          dailyForecasts[dateStr].min = entry;
        }
      }

    });

    // build objects array for use in WeatherScreen
    fiveDayForecast.value = Object.entries(dailyForecasts).slice(0, 5).map(([dateStr, {max,min}]) => {
      const localTimestamp = (max.dt + timezoneOffset) * 1000;
      const localDate = new Date(localTimestamp);

      return {
        day: localDate.toLocaleDateString('en-US', { weekday: 'long' }),
        high: max.main.temp,
        low: min.main.temp,
        icon: max.weather[0].icon,
      }
      
    });


    weatherData.value = rawData;
  }

  return {
    cityName,
    temperature,
    condition,
    humidity,
    windSpeed,
    pressure,
    fiveDayForecast,
    processWeatherData,
  };
}


// This function will convert temperatures to other units (imperial, metric, kelvin)
export function changeTempUnits(temp: number, toUnit: string) {
  if (toUnit === 'imperial') return `${Math.round(temp)} °F`;
  if (toUnit === 'metric') return `${Math.round((temp - 32) * 5 / 9)} °C`;
  if (toUnit === 'kelvin') return `${Math.round((temp - 32) * 5 / 9 + 273.15)} K`;
  return temp;
}