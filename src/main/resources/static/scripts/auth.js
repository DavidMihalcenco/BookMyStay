(function ($) {

    $(document).ready(function () {
        authUser();
        window.location.href = "/";
    });

    function authUser() {
        const token = $("#token").text();
        $("#token").remove();
        const refresh = $('#refresh').text();
        $('#refresh').remove();

        sessionStorage.setItem('token', token);
        sessionStorage.setItem('refresh', refresh);
    }
})(jQuery);