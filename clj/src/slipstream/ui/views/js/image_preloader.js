jQuery( function() { ( function( $$, $, undefined ) {

    // This file requires to load the following library in the corresponding HTML file:
    //   <script src="external/imagesloaded/js/imagesloaded.min.js"></script>
    // Source: https://github.com/desandro/imagesloaded

    // Put image as centered justified background image only when and if it's loaded.
    $('img.ss-image-preloader').imagesLoaded()
        .progress( function( instance, image ) {
            if (image.isLoaded) {
                var img = image.img;
                var img_src = img.src;
                $(img).parent().css("background-image", "url(" + img_src +")");
            }
    });
}( window.SlipStream = window.SlipStream || {}, jQuery ));});
