"use strict";

/**
 * Initializes the signup page.
 * @returns {void}
 */
const init = () => {
    handlePasswordValidation();
}

/**
 * Handles password validation.
 * @returns {void}
 */
const handlePasswordValidation = () => {
    const passwordInput  = document.getElementById('password');
    const requirementsEl = document.querySelector('.pass-requirements');
    if (!passwordInput || !requirementsEl) return;

    const rules = [
        { id: 'req-length',    test: v => v.length >= 8 },
        { id: 'req-uppercase', test: v => /[A-Z]/.test(v) },
        { id: 'req-lowercase', test: v => /[a-z]/.test(v) },
        { id: 'req-number',    test: v => /[0-9]/.test(v) },
        { id: 'req-special',   test: v => /[^A-Za-z0-9]/.test(v) },
    ];

    // Expand on focus
    passwordInput.addEventListener('focus', () => {
        requirementsEl.classList.add('expanded');
    });

    // Collapse on blur only if the field is still empty
    passwordInput.addEventListener('blur', () => {
        if (passwordInput.value === '') {
            requirementsEl.classList.remove('expanded');
        }
    });

    // Validate each rule on every keystroke
    passwordInput.addEventListener('input', () => {
        const val = passwordInput.value;
        rules.forEach(({ id, test }) => {
            const el = document.getElementById(id);
            if (el) el.classList.toggle('req-met', test(val));
        });
    });
}

window.addEventListener('DOMContentLoaded', init);