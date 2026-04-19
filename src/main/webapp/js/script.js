"use strict";
// TODO Plan on changing the file name to something more descriptive.
/**
 * Initializes the application.
 *
 * @returns {void}
 * @author EmileM
 */
const initApp = () => {
    handleLocationPopup();
    handleDropdown();
    handleCurrentLocation();
    handleRestaurantCard();
}

/**
 * Handles the pop-up functionality for the location selection.
 * @returns {void}
 */
const handleLocationPopup = () => {

    const locationBtn = document.getElementById("location-toggle-btn");
    const popupElement = document.getElementById("pop-up");
    const currentLocation = document.querySelector('.current-location-c');
    const customLocation = document.querySelector('.custom-location-c');
    const closeBtn = document.querySelector('.close');
    const currentRadio = currentLocation.querySelector('input[type="radio"]');

    /**
     * Checks if the required elements exist before proceeding.
     */
    if (!locationBtn || !popupElement || !currentLocation || !customLocation || !closeBtn) return;

    // Auto-open if the server rendered an error into the popup, then dismiss after 5s
    const errorMsg = popupElement.querySelector('.errorMsg');
    if (errorMsg) {
        popupElement.classList.add("show");
        setTimeout(() => errorMsg.remove(), 5000);
    }

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
     * Closes the pop-up when the user clicks outside it.
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

/**
 * Handles the dropdown functionality.
 * @returns {void}
 */
const handleDropdown = () => {
    const dropdown = document.querySelector('.dropdown');
    const dropdownContent = document.querySelector('.user-dropdown-content');

    /**
     * Checks if the dropdown and dropdownContent exist before proceeding.
     */
    if (!dropdown || !dropdownContent) return;

    /**
     * Toggles the visibility of the dropdown content.
     * @returns {void}
     */
    dropdown.addEventListener('click', () => {
        dropdownContent.classList.toggle('drop-down-content');

    });

    closeOnClickOutside(dropdownContent, 'drop-down-content', dropdown);
}

/**
 * Closes the dropdown content when the user clicks outside of it.
 *
 * @param {HTMLElement} targetElement - The element to monitor.
 * @param {string} attribute - The CSS class to remove on outside click.
 * @param {HTMLElement|null} excludeElement - Element to exclude from outside-click detection (e.g. toggle button).
 * @returns {void}
 */
const closeOnClickOutside =
    (targetElement, attribute, excludeElement = null) => {

    if (!targetElement || !attribute) return;

    window.addEventListener('click', event => {
        // Check if the user clicked inside the target element or the excluded element
        //  Click inside the dropdown (links, icons, text) -> stays open, interaction works normally.
        const clickedInsideTarget = targetElement.contains(event.target);

        // Click the toggle button -> button does its job of opening/closing via handleDropdown(),
        // closeOnClickOutside() doesn't interfere with it.
        const clickedExcluded = excludeElement && excludeElement.contains(event.target);

        // If the user clicks outside the target element, remove the attribute
        if (!clickedInsideTarget && !clickedExcluded) {
            targetElement.classList.remove(attribute);
        }
    });
}

/**
 * Handles the current location functionality.
 *
 * @returns {void}
 * @author EmileM
 */
const handleCurrentLocation = () => {
    const currentLocation = document.querySelector('.current-location-c');

    if (!currentLocation) return;

    currentLocation.addEventListener('click', () => {

        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(position => {
                const lat = position.coords.latitude;
                const lon = position.coords.longitude;

                // Send to LocationServlet via GET
                window.location.href = `${contextPath}/location?lat=${lat}&lon=${lon}`;
            }, error => {
                console.error('Geolocation error:', error);
            });
        }
    });
}

/**
 * Handles the restaurant card functionality.
 * Prevent default and stop propagation to prevent the card from being clicked.
 *
 * @returns {void}
 */
const handleRestaurantCard = () => {
    document.querySelectorAll('.fav-btn').forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            // TODO: toggle saved state when that feature is built
            this.classList.toggle('saved');
        });
    });
}

window.addEventListener("DOMContentLoaded", initApp);