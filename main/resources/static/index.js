function openWeatherPage(event) {
    event.preventDefault();

    const city = document.getElementById("cityName").value;
    if (city) {
        window.open(`/api/weather/${city}`, "_blank"); // opens in new tab
    }
}