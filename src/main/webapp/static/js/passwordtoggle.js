/* ======================================
   PASSWORD TOGGLE
====================================== */
const toggleBtn = document.getElementById("togglePassword");
const passwordField = document.getElementById("password");

if (toggleBtn && passwordField) {
    toggleBtn.addEventListener("click", function () {

        const icon = this.querySelector("i");

        if (passwordField.type === "password") {
            passwordField.type = "text";
            icon.classList.replace("fa-eye", "fa-eye-slash");
        } else {
            passwordField.type = "password";
            icon.classList.replace("fa-eye-slash", "fa-eye");
        }

    });
}


/* ======================================
   PASSWORD STRENGTH (8–12 RULE)
====================================== */

const bar = document.getElementById("passwordStrengthBar");

if (passwordField && bar) {

    // default state
    bar.className = "progress-bar strength-weak";
    bar.style.width = "25%";

    passwordField.addEventListener("input", function () {

        const value = this.value;

        // ✅ UPDATED RULES
        const hasLength = value.length >= 8 && value.length <= 16;
        const hasUpper = /[A-Z]/.test(value);
        const hasNumber = /[0-9]/.test(value);
        const hasSpecial = /[@$!%*?&]/.test(value);

        let strength = 0;

        if (hasLength) strength++;
        if (hasUpper) strength++;
        if (hasNumber) strength++;
        if (hasSpecial) strength++;

        // progress width
        bar.style.width = (strength * 25) + "%";

        // reset classes
        bar.className = "progress-bar";

        /* ================= FINAL LOGIC ================= */

        if (hasLength && hasUpper && hasNumber && hasSpecial) {

            // ✅ VALID → GREEN
            bar.classList.add("strength-strong");

        } else {

            // ❌ INVALID → RED
            bar.classList.add("strength-weak");
        }

    });
}