jQuery( function() { ( function( $$, $, undefined ) {

    // Auto dismiss alters already present on page load
    $("#alert-container div[role=alert]:not(.hidden)").each(function(){
        var $alertElem = $(this);
        setTimeout(function(){
            $alertElem.hide("slow", function() {
                $alertElem.remove();
            });
        }, 5000);
    })

    var alertDefaultOptions = {
      type: "info",
      title: undefined,
      msg: undefined
    };

    function show(options) {

        var settings,
            $alertContainer,
            $alertElem,
            $lastAlertElem;

        $alertContainer = $("#alert-container");

        if ( ! $alertContainer.length) {
            return false;
        }

        if ($.type(options) === "string") {
            settings = {
                type: "info",
                msg: options
            };
        } else {
            /* merge alertDefaultOptions and options, without modifying alertDefaultOptions */
            settings = $.extend({}, alertDefaultOptions, options);
        }

        $alertElem = $("#alert-" + settings.type)
            .clone()
            .removeClass("hidden") // Bootstrap class
            .hide()
            .removeAttr("id");

        $alertElem
            .find(".alert-msg")
            .html(settings.msg);

        if (settings.title) {
            $alertElem.find(".alert-title").html(settings.title);
        }

        $alertContainer.prepend($alertElem);
        $alertElem.show("fast");

        setTimeout(function(){
            $alertElem.hide("slow", function() {
                $alertElem.remove();
            });
        }, 5000);

        return true;
    }

    function showOfType(type, titleOrMsg, msg) {
        return show({
            type: type,
            title: msg ? titleOrMsg : undefined,
            msg: msg ? msg : titleOrMsg,
        });
    }

    $$.alert = {
        showError: function (titleOrMsg, msg) {
            return showOfType("error", titleOrMsg, msg);
        },
        showWarning: function (titleOrMsg, msg) {
            return showOfType("warning", titleOrMsg, msg);
        },
        showSuccess: function (titleOrMsg, msg) {
            return showOfType("success", titleOrMsg, msg);
        },
        showInfo: function (titleOrMsg, msg) {
            return showOfType("info", titleOrMsg, msg);
        }
    };

}( window.SlipStream = window.SlipStream || {}, jQuery ));});


