// This function will use the API to search for the user's current location and put it in the search bar
export function locateUser(setCityCallback: (cityName: string) => void): void {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            const lat = position.coords.latitude;
            const lon = position.coords.longitude;

            // Send the lat and lon to the backend to get weather info
            fetch(`http://localhost:8080/api/weather/location?lat=${lat}&lon=${lon}`)
                .then(response => response.text())  // data is returned as string
                .then(data => {
                    setCityCallback(data);
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
