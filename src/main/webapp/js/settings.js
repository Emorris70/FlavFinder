"use strict";

/**
 * Entry point for the settings page.
 *
 * @returns {void}
 * @author EmileM
 */
const initSettings = () => {
    handleBackButton();
    handleDeleteModal();
};

/**
 * Intercepts the back button and uses the browser history stack so the
 * user returns to their previous page. Falls back to /home if no history exists.
 *
 * @returns {void}
 */
const handleBackButton = () => {
    const btn = document.getElementById('settings-back-btn');
    if (!btn) return;

    btn.addEventListener('click', () => {
        if (history.length > 1) {
            history.back();
        } else {
            window.location.href = `${contextPath}/home`;
        }
    });
};

/**
 * Wires up the delete-account confirmation modal.
 *
 * Opens the modal when the delete button is clicked, and closes it
 * when the cancel button or the overlay backdrop is clicked.
 *
 * @returns {void}
 */
const handleDeleteModal = () => {
    const overlay    = document.getElementById('delete-modal');
    const openBtn    = document.getElementById('open-delete-modal');
    const cancelBtn  = document.getElementById('cancel-delete');

    if (!overlay || !openBtn || !cancelBtn) return;

    openBtn.addEventListener('click', () => overlay.classList.add('show'));
    cancelBtn.addEventListener('click', () => overlay.classList.remove('show'));

    overlay.addEventListener('click', e => {
        if (e.target === overlay) overlay.classList.remove('show');
    });
};

window.addEventListener('DOMContentLoaded', initSettings);
