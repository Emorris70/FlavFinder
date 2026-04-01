"use strict";

/**
 * Initializes the application.
 * @returns {void}
 * @author EmileM
 */
const initApp = () => {
    handleLocationPopup();
}

/**
 * Handles the pop-up functionality.
 */
const handleLocationPopup = () => {

    const locationBtn = document.getElementById("location-toggle-btn");
    const popupElement = document.getElementById("pop-up");
    const currentLocation = document.querySelector('.current-location-c');
    const customLocation = document.querySelector('.custom-location-c');
    const closeBtn = document.querySelector('.close');
    const currentRadio = currentLocation.querySelector('input[type="radio"]');

    /**
     * Opens the pop-up.
     *
     * @returns {void}
     */
    locationBtn.addEventListener("click", () => {
        popupElement.classList.toggle("show");
    });

    /**
     * Applies styling to the current-location and removes it from custom-location. lastly,
     * indicates that the radio button is active.
     *
     * @returns {void}
     */
    currentLocation.addEventListener('click', () => {
        currentLocation.classList.add('selected');
        customLocation.classList.remove('selected');
        currentRadio.checked = true;
    });


    /**
     * Applies styling to the custom-location and removes it from the current-location. And
     * indicates that the radio button is not active.
     *
     * @returns {void}
     */
    customLocation.addEventListener('click', () => {
        customLocation.classList.add('selected');
        currentLocation.classList.remove('selected');
        currentRadio.checked = false;
    });

    /**
     * Closes the pop-up.
     *
     * @returns {void}
     */
    closeBtn.addEventListener('click', () => {
        popupElement.classList.remove('show');
    });

    /**
     * Closes the pop-up when the user clicks outside of it.
     *
     * @returns {void}
     * @param {Event} event - The event object.
     */
    window.addEventListener('click', event => {
        if (event.target === popupElement) {
            popupElement.classList.remove('show');
        }
    });

}
window.addEventListener("DOMContentLoaded", initApp);