$(function() {
    // active
    $('a, input[type="button"], input[type="submit"], button')
        .bind('touchstart', function() {
            $(this).addClass('active');
        }).bind( 'touchend', function() {
            $(this).removeClass('active');
        });

});
