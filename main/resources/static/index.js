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
    weatherCondition = weatherData.weather[0].main; // store weather condition to modify background image
    weatherIcon = weatherData.weather[0].icon;
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

    

    // calls function to change weather icon
    setWeatherIcon(weatherIcon);

    // calls function to change page background based on weather condition
    setWeatherBackground(weatherCondition);

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

    // style the background image 
    // WIP: MOVE THIS TO CSS FILE LATER
    document.body.style.backgroundImage = imageUrl;
    document.body.style.backgroundSize = "cover";
    document.body.style.backgroundPosition = "center";
    document.body.style.backgroundRepeat = "no-repeat";
    document.body.style.backgroundAttachment = "fixed";
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