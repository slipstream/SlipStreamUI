jQuery( function() { ( function( $$, $, undefined ) {

    var alertDefaultOptions = {
      type: "info",
      title: null,
      msg: null
    };

    function show(options) {

        var settings,
            $alertContainer,
            $alertElem;

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

        return true;
    }

    function showOfType(type, titleOrMsg, msg) {
        return show({
            type: type,
            title: msg ? titleOrMsg : null,
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


