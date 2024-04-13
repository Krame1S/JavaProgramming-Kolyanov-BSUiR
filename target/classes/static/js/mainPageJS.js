document.addEventListener('DOMContentLoaded', function() {
    var historyButton = document.querySelector('.history-button');

    historyButton.addEventListener('click', function(event) {
        event.preventDefault();
        window.location.href = '/all-checks';
    });
});
