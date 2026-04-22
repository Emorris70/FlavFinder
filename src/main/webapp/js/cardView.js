"use strict";

/**
 * Entry point for the restaurant detail (card view) page.
 * Wires up the hours accordion and highlights today's hours row.
 *
 * @returns {void}
 */
const initCardView = () => {
    handleHoursAccordion();
    highlightToday();
};

/**
 * Toggles the working-hours accordion open and closed.
 *
 * Reads and flips the {@code aria-expanded} attribute on the toggle button,
 * and mirrors that state on the body element via the {@code hidden} property
 * so the section is accessible and animates correctly.
 *
 * @returns {void}
 */
const handleHoursAccordion = () => {
    const toggle = document.getElementById('cv-hours-toggle');
    const body   = document.getElementById('cv-hours-body');

    if (!toggle || !body) return;

    toggle.addEventListener('click', () => {
        const isExpanded = toggle.getAttribute('aria-expanded') === 'true';
        toggle.setAttribute('aria-expanded', String(!isExpanded));
        body.hidden = isExpanded;
    });
};

/**
 * Marks today's row inside the hours accordion with the {@code cv-today}
 * class so it renders in the brand red accent colour.
 *
 * Compares the full English day name (e.g. "Monday") against each row's
 * {@code data-day} attribute, case-insensitively.
 *
 * @returns {void}
 */
const highlightToday = () => {
    const days  = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
    const today = days[new Date().getDay()].toLowerCase();

    document.querySelectorAll('.cv-hours-entry').forEach(row => {
        if (row.dataset.day?.toLowerCase() === today) {
            row.classList.add('cv-today');
        }
    });
};

window.addEventListener('DOMContentLoaded', initCardView);
