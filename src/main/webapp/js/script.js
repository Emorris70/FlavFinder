/**
 * Initializes the application.
 * @returns {void}
 * @author EmileM
 */
const init = () => {
    console.log("Hello World!!");
    popUp();
}

/**
 * Handles the pop-up functionality.
 */
const popUp = () => {
    const locationBtn = document.getElementById("location-toggle-btn");
    const dropElement = document.getElementById("pop-up");
    const currentLocation = document.querySelector('.current-location-c');
    const customLocation = document.querySelector('.custom-location-c');
    const closeBtn = document.querySelector('.close');
    const currentRadio = currentLocation.querySelector('input[type="radio"]');

    // displays the pop-up
    locationBtn.addEventListener("click", () => {
        console.log("clicked");
        dropElement.classList.toggle("show");
    });

    /**
     * Applies styling to the current-location and removes it from custom-location. lastly,
     * indicates that the radio button is active.
     */
    currentLocation.addEventListener('click', () => {
        currentLocation.classList.add('selected');
        customLocation.classList.remove('selected');
        currentRadio.checked = true;
    });


    /**
     * Applies styling to the custom-location and removes it from the current-location. And
     * indicates that the radio button is not active.
     */
    customLocation.addEventListener('click', () => {
        customLocation.classList.add('selected');
        currentLocation.classList.remove('selected');
        currentRadio.checked = false;
    });

    /**
     * Closes the pop-up.
     */
    closeBtn.addEventListener('click', () => {
        dropElement.classList.remove('show');
        console.log("clicked");
    });

    /**
     * Closes the pop-up when the user clicks outside of it.
     */
    window.addEventListener('click', event => {
        if (event.target === dropElement) {
            dropElement.classList.remove('show');
        }
    })

}
window.addEventListener("DOMContentLoaded", init);