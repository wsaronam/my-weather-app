let currentTemp = null; // for storing temps
let currentUnit = "imperial"; // our current default unit




// This function will use the API to search for a city's weather data
function searchForWeatherData(event) {
    event.preventDefault();

    const city = document.getElementById("cityName").value;
    if (city) {
        const apiUrl = `http://localhost:8080/api/weather/${encodeURIComponent(city)}`;

        fetch(apiUrl)
            .then(response => response.json())
            .then(data => {
                // put weather data in localStorage
                localStorage.setItem("weatherData", JSON.stringify(data));

                // open new tab for weather page
                window.open("weather.html", "_blank");
            })
            .catch(error => console.error("Error fetching weather data:", error));
    }
}


// This function will take the JSON data from the searchForWeatherData function and prepare it for display on HTML
function displayWeather() {
    // get weather data from localStorage
    const weatherData = JSON.parse(localStorage.getItem("weatherData"));
    currentTemp = weatherData.main.temp; // store temperature in case of later conversion
    console.log(weatherData);
    
    // no input detected check
    if (!weatherData) {
        document.body.innerHTML = "<h2>No weather data found.</h2>";
        return;
    }

    // invalid location inputted check
    try {
        if (weatherData.message.slice(0,3) == "404")
            document.body.innerHTML = "<h2>Invalid location inputted.</h2>";
            return;
    }
    catch { }


    // insert weather data into html
    document.getElementById("city-name").textContent = `${weatherData.name}, ${weatherData.sys.country}`;
    document.getElementById("temperature").textContent = `${weatherData.main.temp}F`;
    // document.getElementById("feels-like").textContent = `${weatherData.main.feels_like}F`;
    document.getElementById("condition").textContent = `${weatherData.weather[0].main} - ${weatherData.weather[0].description}`;
    document.getElementById("humidity").textContent = `${weatherData.main.humidity}%`;
    document.getElementById("wind-speed").textContent = `${weatherData.wind.speed} m/s`;
    document.getElementById("pressure").textContent = `${weatherData.main.pressure} hPa`;
}

// calls the displayWeather function when the page loads
if (window.location.pathname.endsWith("weather.html")) {
    window.onload = displayWeather;
}



// convert temperature when the user changes the unit
function convertTemps() {
    const unit = document.getElementById("tempUnit").value;
    let newTemp;

    if (unit === "metric" && currentUnit !== "metric") {
        // convert F to C
        newTemp = ((currentTemp - 32) * 5/9).toFixed(2);
        currentUnit = "metric";
        document.getElementById("temperature").innerText = `${newTemp}°C`;
    } 
    else if (unit === "imperial" && currentUnit !== "imperial") {
        // convert C to F
        newTemp = currentTemp;
        currentUnit = "imperial";
        document.getElementById("temperature").innerText = `${newTemp}°F`;
    }
}