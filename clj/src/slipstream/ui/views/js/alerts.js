// Pattern from:
// http://appendto.com/2010/10/how-good-c-habits-can-encourage-bad-javascript-habits-part-1/

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

    function showPrebuilt(type, titleOrMsg, msg) {
        return show({
            type: type,
            title: msg ? titleOrMsg : null,
            msg: msg ? msg : titleOrMsg,
        });
    }

    $$.alert = {
        showError: function (titleOrMsg, msg) {
            return showPrebuilt("error", titleOrMsg, msg);
        },
        showWarning: function (titleOrMsg, msg) {
            return showPrebuilt("warning", titleOrMsg, msg);
        },
        showSuccess: function (titleOrMsg, msg) {
            return showPrebuilt("success", titleOrMsg, msg);
        },
        showInfo: function (titleOrMsg, msg) {
            return showPrebuilt("info", titleOrMsg, msg);
        }
    };

}( window.SlipStream = window.SlipStream || {}, jQuery ));});


