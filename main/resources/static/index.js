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
                // put weather datas in localStorage
                localStorage.setItem("weatherData", JSON.stringify(data));

                // open new tab for weather page
                window.open("weather.html", "_blank");
            })
            .catch(error => console.error("Error fetching weather data:", error));
    }
}


// This function will use the API to search for the user's current location and put it in the search bar
function locateUser() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            const lat = position.coords.latitude;
            const lon = position.coords.longitude;

            // Send the lat and lon to the backend to get weather info
            fetch(`http://localhost:8080/api/weather/location?lat=${lat}&lon=${lon}`)
                .then(response => response.text())  // data is returned as string
                .then(data => {
                    document.getElementById("cityName").value = data;
                })
                .catch(error => {
                    console.error("Error fetching weather data:", error);
                });
        }, 
        function(error) {
            console.error("Error getting location:", error);
        });
    }

    else {
        console.log("Geolocation is not supported by this browser.");
    }
}


// This function will take the JSON data from the searchForWeatherData function and prepare it for display on HTML
// This function will also take the Five-Day forecast and prepare it for display on HTML
//     Maybe we should separate this into a separate function
function displayWeather() {
    // get weather data from localStorage
    const weatherData = JSON.parse(localStorage.getItem("weatherData"));
    const currentWeather = weatherData["currentWeather"];
    currentTemp = currentWeather.main.temp; // store temperature in case of later conversion
    weatherCondition = currentWeather.weather[0].main; // store weather condition to modify background image
    weatherIcon = currentWeather.weather[0].icon;
    
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

    

    // calls function to change weather icon
    setWeatherIcon(weatherIcon);

    // calls function to change page background based on weather condition
    setWeatherBackground(weatherCondition);

    // insert weather data into html
    document.getElementById("city-name").textContent = `${currentWeather.name}, ${currentWeather.sys.country}`;
    document.getElementById("temperature").textContent = `${currentWeather.main.temp}F`;
    // document.getElementById("feels-like").textContent = `${weatherData.main.feels_like}F`;
    document.getElementById("condition").textContent = `${currentWeather.weather[0].main} - ${currentWeather.weather[0].description}`;
    document.getElementById("humidity").textContent = `${currentWeather.main.humidity}%`;
    document.getElementById("wind-speed").textContent = `${currentWeather.wind.speed} m/s`;
    document.getElementById("pressure").textContent = `${currentWeather.main.pressure} hPa`;



    const fiveDayForecast = weatherData["fiveDayForecast"].list;
    const forecastContainer = document.getElementById("forecast");

    const dailyForecasts = {}; // plan to store a mid-day forecast every day for the 5 day forecast

    fiveDayForecast.forEach((entry) => {
        const date = entry.dt_txt.split(" ")[0]; // extract dates in this format (YYYY-MM-DD)
        if (!dailyForecasts[date] && entry.dt_txt.includes("12:00:00")) {
            dailyForecasts[date] = entry; // mid-day forecast
        }
    });

    Object.values(dailyForecasts).forEach((forecast) => {
        const temp = forecast.main.temp;
        const iconCode = forecast.weather[0].icon;
        const date = new Date(forecast.dt * 1000).toLocaleDateString("en-US", { weekday: "long" });

        // build the HTML for the 5-day forecast
        //     maybe we should move this to the HTML later
        const forecastHTML = `
            <div class="forecast-item">
                <p><strong>${date}</strong></p>
                <img src="https://openweathermap.org/img/wn/${iconCode}@2x.png" alt="Weather Icon">
                <p>${temp}°F</p>
            </div>
        `;

        forecastContainer.innerHTML += forecastHTML;
    });
}

// calls the displayWeather function when the page loads
if (window.location.pathname.endsWith("weather.html")) {
    window.onload = displayWeather;
}



// updates weather data background based on temperature condition found
function setWeatherBackground(condition) {
    let imageUrl;

    // currently using unsplash.com for free use images
    switch (condition) {
        case "Clear":
            imageUrl = "url('https://plus.unsplash.com/premium_photo-1727730047398-49766e915c1d?q=80&w=2712&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D')";
            break;
        case "Clouds":
            imageUrl = "url('https://plus.unsplash.com/premium_photo-1667689956673-8737a299aa8c?q=80&w=2576&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D')";
            break;
        case "Rain":
            imageUrl = "url('https://images.unsplash.com/photo-1607500098489-1991d1fbab7a?q=80&w=2670&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D')";
            break;
        case "Snow":
            imageUrl = "url('https://plus.unsplash.com/premium_photo-1669325007869-d3b17d2d31e6?q=80&w=2575&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D')";
            break;
        case "Thunderstorm":
            imageUrl = "url('https://plus.unsplash.com/premium_photo-1673278171340-99b4dbf0418f?q=80&w=2572&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D')";
            break;
        default:
            imageUrl = "url('https://plus.unsplash.com/premium_photo-1701646600064-3365efea1ba8?q=80&w=2664&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D')"; // default "fallback" image
    }

    // style the background
    document.body.style.backgroundImage = imageUrl;
    document.body.style.backgroundSize = "cover"; 
    document.body.style.backgroundPosition = "center"; 
    document.body.style.backgroundRepeat = "no-repeat"; 
    document.body.style.transition = "background 0.5s ease-in-out";
}

// updates weather data icon using openweathermap's icons
function setWeatherIcon(weatherIcon) {
    const weatherIconUrl = `https://openweathermap.org/img/wn/${weatherIcon}@2x.png`;
    document.getElementById("weather-icon").src = weatherIconUrl;
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