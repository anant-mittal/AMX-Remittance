$(function() {
    $(".top-menu-item").click(function() {
        window.location.href = $(this).find("a").attr("href");
    });
});
