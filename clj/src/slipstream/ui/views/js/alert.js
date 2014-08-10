jQuery( function() { ( function( $$, $, undefined ) {

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

        $lastAlertElem = $("#alert-container div.alert").first();
        if ( $alertElem.html() == $lastAlertElem.html() ){
            // Highlight the last alert instead of adding the same again.
            // Happy to learn a nicer way to highlight it ;)
            $lastAlertElem.fadeTo(80, 0.3,
                function (){
                    $lastAlertElem.fadeTo(120, 1);
                });
            return true;
        }

        $alertContainer.prepend($alertElem);
        $alertElem.show("fast");

        return true;
    }

    function showOfType(type, titleOrMsg, msg) {
        return show({
            type: type,
            title: msg ? titleOrMsg : undefined,
            msg: msg ? msg : titleOrMsg,
        });
    }

    $$.Alert = {
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


