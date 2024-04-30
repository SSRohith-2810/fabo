document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('submitBtn').addEventListener('click', function (event) {
        event.preventDefault();

        var email = document.getElementById('email-input').value;
        var password = document.getElementById('password-input').value;
        var confirmPassword = document.getElementById('confirm-password-input').value;

        // Clear existing error messages
        document.getElementById('email-error').innerHTML = '';
        document.getElementById('password-error').innerHTML = '';
        document.getElementById('confirm-password-error').innerHTML = '';

        // Validate email
        if (email.trim() === '') {
            document.getElementById('email-error').innerHTML = 'Email cannot be empty';
            return;
        }

        // Validate password
        if (password.trim() === '') {
            document.getElementById('password-error').innerHTML = 'Password cannot be empty';
            return;
        }

        // Validate confirm password
        if (confirmPassword.trim() === '') {
            document.getElementById('confirm-password-error').innerHTML = 'Confirm Password cannot be empty';
            return;
        }

        // Check if the password matches the confirm password
        if (password !== confirmPassword) {
            document.getElementById('confirm-password-error').innerHTML = 'Password and Confirm Password do not match';
            return;
        }

        // Proceed with form submission or any other action
    });
});