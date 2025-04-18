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
    const dailyForecasts: Record<string, any> = {};
    rawData.fiveDayForecast.list.forEach((entry: any) => {

      // convert our local time to utc time to match api data
      const localTimestamp = entry.dt + timezoneOffset;
      const localDate = new Date(localTimestamp * 1000);
      console.log(localDate);
      const dateStr = localDate.toISOString().split('T')[0];
      console.log(dateStr);
      const hour = localDate.getHours();
      const noonDiff = Math.abs(hour - 12);
      console.log(hour);
      console.log(noonDiff);



      const [date, time] = entry.dt_txt.split(' ');
      const existingEntry = dailyForecasts[dateStr];
      console.log(existingEntry);
      
      // if entry does not exist, we take the data first 
      if (!existingEntry) {
        dailyForecasts[dateStr] = entry;
      }
      // replace entry if the time is closer to 12PM
      else {
        const existingHour = new Date((existingEntry.dt + timezoneOffset) * 1000).getHours();
        const existingNoonDiff = Math.abs(existingHour - 12);
        
        if (noonDiff < existingNoonDiff) {
          dailyForecasts[dateStr] = entry;
        }
      }

      // if (!dailyForecasts[date] && time == '12:00:00') {
      //   console.log(entry);
      //   dailyForecasts[date] = entry;
      // }

    });

    // build objects array for use in WeatherScreen
    fiveDayForecast.value = Object.values(dailyForecasts).map((entry: any) => {
      const localTimestamp = entry.dt + timezoneOffset;
      const localDate = new Date(localTimestamp * 1000);

      return {
        day: localDate.toLocaleDateString('en-US', { weekday: 'long' }),
        temp: entry.main.temp,
        icon: entry.weather[0].icon,
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