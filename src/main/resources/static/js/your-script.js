document.addEventListener('DOMContentLoaded', function() {
    // Select the back button by its class name
    var backButton = document.querySelector('.back-button');

    // Add a click event listener to the back button
    backButton.addEventListener('click', function(event) {
        // Prevent the default action of the button
        event.preventDefault();
    });
});
