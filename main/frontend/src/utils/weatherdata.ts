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