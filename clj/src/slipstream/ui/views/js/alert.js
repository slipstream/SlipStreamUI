jQuery( function() { ( function( $$, $, undefined ) {

    // Auto dismiss alerts already present on page load
    $("#alert-container div[role=alert]:not(.hidden)").scheduleAlertDismiss();

    var alertDefaultOptions = {
        type: "info",
        container: "floating",
        autoDismiss: true,
        title: undefined,
        msg: undefined
    };

    function show(arg) {

        var settings,
            customOptions,
            $alertContainer,
            alertContainerIDPrefix = "#ss-alert-container-",
            $alertElem,
            $lastAlertElem;

        if ($.type(arg) === "string") {
            customOptions = {
                msg: arg
            };
        } else if ($.type(arg) === "object") {
            customOptions = arg;
        }

        /* merge alertDefaultOptions and arg, without modifying alertDefaultOptions */
        settings = $.extend({}, alertDefaultOptions, customOptions);

        $alertContainer = $(alertContainerIDPrefix + settings.container);

        if ( $alertContainer.foundNothing()) {
            throw "alert: No container '" + settings.container + "' found in page!";
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

        // Configure custom dismiss animation
        $alertElem
            .find("button.close")
                .removeAttr("data-dismiss")
                .click(function (elem) {
                    $alertElem.slideUp("fast");
                });

        $alertContainer.prepend($alertElem);
        $alertElem.slideDown("fast");

        if (settings.autoDismiss) {
            $alertElem.scheduleAlertDismiss();
        }

        return true;
    }

    function showOfType(container, type, titleOrMsg, msg) {
        return show({
            type: type,
            container: container || "floating",
            autoDismiss: container === "floating",
            title: msg ? titleOrMsg : undefined,
            msg: msg ? msg : titleOrMsg,
        });
    }

    $$.alert = {
        showError: function (titleOrMsg, msg) {
            return showOfType("floating", "error", titleOrMsg, msg);
        },
        showErrorFixed: function (titleOrMsg, msg) {
            return showOfType("fixed", "error", titleOrMsg, msg);
        },
        showWarning: function (titleOrMsg, msg) {
            return showOfType("floating", "warning", titleOrMsg, msg);
        },
        showWarningFixed: function (titleOrMsg, msg) {
            return showOfType("fixed", "warning", titleOrMsg, msg);
        },
        showSuccess: function (titleOrMsg, msg) {
            return showOfType("floating", "success", titleOrMsg, msg);
        },
        showSuccessFixed: function (titleOrMsg, msg) {
            return showOfType("fixed", "success", titleOrMsg, msg);
        },
        showInfo: function (titleOrMsg, msg) {
            return showOfType("floating", "info", titleOrMsg, msg);
        },
        showInfoFixed: function (titleOrMsg, msg) {
            return showOfType("fixed", "info", titleOrMsg, msg);
        }
    };

}( window.SlipStream = window.SlipStream || {}, jQuery ));});


