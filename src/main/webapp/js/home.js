"use strict";

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
    wireFavButtons(document);
    handleCategoryPills();
    handleSidebar();
    updateDistances(document);
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

    if (!locationBtn || !popupElement || !currentLocation || !customLocation || !closeBtn) return;

    const currentRadio = currentLocation.querySelector('input[type="radio"]');

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
 * Wires save/unsave behaviour onto every .fav-btn inside a given root element.
 * POST /save?placeId=X to save; DELETE /save?placeId=X to unsave.
 *
 * @param {ParentNode} root - Element to scope the query to (document or a grid).
 * @returns {void}
 */
const wireFavButtons = (root) => {
    root.querySelectorAll('.fav-btn').forEach(btn => {
        btn.addEventListener('click', async function(e) {
            e.preventDefault();
            e.stopPropagation();

            const placeId = this.dataset.placeId;
            if (!placeId) return;

            const isSaved = this.classList.contains('saved');
            const method  = isSaved ? 'DELETE' : 'POST';

            try {
                const res = await fetch(`${contextPath}/save?placeId=${encodeURIComponent(placeId)}`, { method });
                if (res.ok) {
                    this.classList.toggle('saved');
                } else {
                    console.error('Save toggle failed:', res.status);
                }
            } catch (err) {
                console.error('Save request error:', err);
            }
        });
    });
}

/**
 * Handles category pill filtering with live server-side fetching.
 *
 * On init, the server-rendered nearby content is cloned so "All" can always
 * restore the original state without a page reload.
 *
 * Each pill click does two things:
 *   1. Active state - strips `.active` from every pill and applies it to the
 *      clicked one to keep the UI in sync.
 *   2. Content update:
 *        - "All": swaps the nearby section back to the cached server render.
 *        - Any other pill: shows a loading placeholder, fetches
 *          /api/restaurants?category=<pill> and replaces the nearby grid with
 *          the new results. A "no results" message is shown if the API returns
 *          an empty array or the user hasn't set a location (204).
 *
 * @returns {void}
 */
const handleCategoryPills = () => {
    const pills = document.querySelectorAll('.pill');
    const nearbySection = document.getElementById('nearby-section');

    if (!pills.length || !nearbySection) return;

    // Snapshot the server-rendered content so "All" can restore it exactly
    const originalContent = nearbySection
        .querySelector('.cards-grid, .no-results')
        ?.cloneNode(true);

    pills.forEach(pill => {
        pill.addEventListener('click', async () => {
            pills.forEach(p => p.classList.remove('active'));
            pill.classList.add('active');

            const selected = pill.textContent.trim();

            if (selected.toLowerCase() === 'all') {
                if (originalContent) replaceNearbyContent(nearbySection, originalContent.cloneNode(true));
                return;
            }

            // Show a placeholder while the request is in flight
            replaceNearbyContent(nearbySection, buildStatusNode('p', 'no-results', 'Loading...'));

            try {
                const res = await fetch(`${contextPath}/api/restaurants?category=${encodeURIComponent(selected)}`);

                if (res.status === 204) {
                    replaceNearbyContent(nearbySection, buildStatusNode('p', 'no-results', 'Set a location to discover nearby restaurants.'));
                    return;
                }
                if (!res.ok) throw new Error(`HTTP ${res.status}`);

                const restaurants = await res.json();

                if (!restaurants.length) {
                    replaceNearbyContent(nearbySection, buildStatusNode('p', 'no-results', `No ${selected} restaurants found near you.`));
                    return;
                }

                renderCards(nearbySection, restaurants);
            } catch (e) {
                console.error('Category fetch failed:', e);
                replaceNearbyContent(nearbySection, buildStatusNode('p', 'no-results', 'Something went wrong. Please try again.'));
            }
        });
    });
}

/**
 * Replaces the current content node (.cards-grid, .no-results, or any prior
 * swap target) inside the nearby section with a new node.
 *
 * @param {HTMLElement} section - The nearby section container.
 * @param {HTMLElement} newNode - The node to insert.
 * @returns {void}
 */
const replaceNearbyContent = (section, newNode) => {
    const existing = section.querySelector('.cards-grid, .no-results');
    existing ? existing.replaceWith(newNode) : section.appendChild(newNode);
}

/**
 * Creates a simple single-element status node (loading placeholder, error,
 * or empty-state message).
 *
 * @param {string} tag       - HTML tag name (e.g. 'p').
 * @param {string} className - CSS class to apply.
 * @param {string} text      - Text content to display.
 * @returns {HTMLElement}
 */
const buildStatusNode = (tag, className, text) => {
    const el = document.createElement(tag);
    el.className = className;
    el.textContent = text;
    return el;
}

/**
 * Builds the restaurant card grid from a JSON array returned by
 * /api/restaurants and inserts it into the nearby section.
 *
 * After insertion, fav buttons on the new cards are wired up so they can
 * toggle the saved state without interfering with card navigation.
 *
 * @param {HTMLElement} section     - The nearby section container.
 * @param {Object[]}    restaurants - Array of BusinessItem objects from the API.
 * @returns {void}
 */
const renderCards = (section, restaurants) => {
    const grid = document.createElement('div');
    grid.className = 'cards-grid';
    restaurants.forEach(r => grid.appendChild(buildCard(r)));
    replaceNearbyContent(section, grid);
    wireFavButtons(grid);
    updateDistances(grid);
}

/**
 * Builds a single restaurant card DOM element from a BusinessItem JSON object.
 *
 * Mirrors the card structure in home.jsp so fetched results look identical to
 * the server-rendered ones. Field names use the API's snake_case keys as
 * Jackson serializes them (e.g. place_id, photos_sample, opening_status).
 *
 * @param {Object} r - A BusinessItem object from the /api/restaurants response.
 * @returns {HTMLAnchorElement} The fully constructed card element.
 */
const buildCard = (r) => {
    // Escape a value for safe injection into innerHTML attribute strings
    const esc = v => v != null ? String(v)
        .replace(/&/g,'&amp;')
        .replace(/</g,'&lt;')
        .replace(/>/g,'&gt;')
        .replace(/"/g,'&quot;') : '';

    const isOpen   = r.opening_status?.toLowerCase().includes('open');
    const isClosed = r.opening_status?.toLowerCase().includes('close');
    const statusClass = isOpen ? 'status-open' : isClosed ? 'status-closed' : '';
    const statusText  = isOpen ? 'Open' : isClosed ? 'Closed' : '--';

    const photoUrl = r.photos_sample?.length
        ? r.photos_sample[0].photo_url
        : `${contextPath}/images/near-me.png`;

    const card = document.createElement('a');
    card.href = `${contextPath}/restaurant?placeId=${encodeURIComponent(r.place_id)}`;
    card.className = 'restaurant-card card-grid';

    card.innerHTML = `
        <div class="card-img-wrap">
            <img src="${esc(photoUrl)}" alt="${esc(r.name)}" class="card-img" referrerpolicy="no-referrer">
            <button class="fav-btn" data-place-id="${esc(r.place_id)}">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960">
                    <path d="m480-120-58-52q-101-91-167-157T150-447.5Q111-500 95.5-544T80-634q0-94 63-157t157-63q52 0 99 22t81 62q34-40 81-62t99-22q94 0 157 63t63 157q0 46-15.5 90T810-447.5Q771-395 705-329T538-172l-58 52Z"/>
                </svg>
            </button>
        </div>
        <div class="card-body">
            <div class="card-title-row">
                <span class="card-name">${esc(r.name)}</span>
                <span class="card-price">${esc(r.price_level)}</span>
            </div>
            ${r.rating > 0 ? `
            <div class="card-rating">
                <svg class="star-icon" viewBox="0 0 24 24">
                    <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
                </svg>
                <span class="rating-val">${esc(String(r.rating))}</span>
                <span class="review-count">(${esc(String(r.review_count))})</span>
                <span class="card-separator">·</span>
                <span class="card-type">${esc(r.type)}</span>
            </div>` : ''}
            <div class="card-footer-row">
                <svg class="loc-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 -960 960 960">
                    <path d="M480-301q99-80 149.5-154T680-594q0-90-56-148t-144-58q-88 0-144 58t-56 148q0 65 50.5 139T480-301Zm0 101Q339-304 269.5-402T200-594q0-125 78-205.5T480-880q124 0 202 80.5T760-594q0 94-69.5 192T480-200Zm0-320q33 0 56.5-23.5T560-600q0-33-23.5-56.5T480-680q-33 0-56.5 23.5T400-600q0 33 23.5 56.5T480-520Zm0-80Z"/>
                </svg>
                <span class="card-distance" data-lat="${esc(String(r.latitude))}" data-lon="${esc(String(r.longitude))}">--</span>
                <span class="open-status ${statusClass}">${statusText}</span>
            </div>
        </div>`;

    return card;
}

/**
 * Handles the mobile sidebar (hamburger menu).
 *
 * Wires up three entry points that open the sidebar:
 *   - Hamburger button in the header
 * And two that close it:
 *   - The × close button inside the sidebar
 *   - Clicking the dimmed overlay behind the sidebar
 *
 * Opening adds `.open` to both the sidebar and overlay so CSS transitions
 * handle the slide-in and fade-in respectively. Closing reverses that.
 *
 * The sidebar location button mirrors the main header location button —
 * clicking it closes the sidebar then triggers the location popup so the
 * user lands in the right flow without any visible interruption.
 *
 * @returns {void}
 */
const handleSidebar = () => {
    const hamburger   = document.getElementById('hamburger');
    const sidebar     = document.getElementById('sidebar');
    const overlay     = document.getElementById('sidebar-overlay');
    const closeBtn    = document.getElementById('sidebar-close');
    const sidebarLocBtn = document.getElementById('sidebar-location-btn');

    if (!hamburger || !sidebar || !overlay) return;

    const openSidebar  = () => { sidebar.classList.add('open');    overlay.classList.add('open'); };
    const closeSidebar = () => { sidebar.classList.remove('open'); overlay.classList.remove('open'); };

    hamburger.addEventListener('click', openSidebar);
    closeBtn?.addEventListener('click', closeSidebar);
    overlay.addEventListener('click', closeSidebar);

    // Mirror the header location button, close the sidebar first, then open the popup
    sidebarLocBtn?.addEventListener('click', () => {
        closeSidebar();
        document.getElementById('location-toggle-btn')?.click();
    });
}

/**
 * Calculates the great-circle distance between two coordinates using the
 * Haversine formula.
 *
 * @param {number} lat1 - Latitude of point 1 in decimal degrees.
 * @param {number} lon1 - Longitude of point 1 in decimal degrees.
 * @param {number} lat2 - Latitude of point 2 in decimal degrees.
 * @param {number} lon2 - Longitude of point 2 in decimal degrees.
 * @returns {number} Distance in miles.
 */
const calcDistance = (lat1, lon1, lat2, lon2) => {
    const R = 3958.8;
    const toRad = deg => deg * Math.PI / 180;
    const dLat = toRad(lat2 - lat1);
    const dLon = toRad(lon2 - lon1);
    const a = Math.sin(dLat / 2) ** 2 +
              Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
              Math.sin(dLon / 2) ** 2;
    return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
}

/**
 * Populates every .card-distance element within a given root with a
 * human-readable distance from the user's current location.
 *
 * Skips silently if userLat/userLon are not set (no location in session)
 * or if a card is missing valid coordinate data attributes.
 *
 * @param {ParentNode} root - Element to scope the query to (document or a grid).
 * @returns {void}
 */
const updateDistances = (root) => {
    if (userLat == null || userLon == null) return;

    root.querySelectorAll('.card-distance').forEach(el => {
        const lat = parseFloat(el.dataset.lat);
        const lon = parseFloat(el.dataset.lon);
        if (isNaN(lat) || isNaN(lon)) return;

        const miles = calcDistance(userLat, userLon, lat, lon);
        el.textContent = miles < 0.1 ? '< 0.1 mi' : miles.toFixed(1) + ' mi';
    });
}

window.addEventListener("DOMContentLoaded", initApp);