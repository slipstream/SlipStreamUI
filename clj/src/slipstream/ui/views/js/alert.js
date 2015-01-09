jQuery( function() { ( function( $$, $, undefined ) {

    function scheduleAlertDismiss($alertElem) {
        // Wait longer for longer messages.
        var minDelay = 5000,
            delayMillisPerChar = 100, // empirical value
            msg = $alertElem.find(".alert-msg").text();
            delay = Math.max(minDelay, delayMillisPerChar * msg.countNonWhitespaceChars());
        $alertElem.scheduleAlertDismiss(delay);
    }

    // Auto dismiss alerts already present on page load
    $("#ss-alert-container-floating div[role=alert]:not(.hidden)").each(function (index, alertElem) {
        scheduleAlertDismiss($(alertElem));
    });

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
                                .removeAttr("id")
                                .find("button.close")
                                    .removeAttr("data-dismiss")
                                    .end();

        $alertElem
            .find(".alert-msg")
            .html(settings.msg);

        if (settings.title) {
            $alertElem.find(".alert-title").html(settings.title);
        }

        if ( $alertElem.html() === $alertContainer.find("div.alert").first().html() ){
            // The exact same alert is the most recent one.
            // Nothing to do.
            return true;
        }

        // Configure custom dismiss animation
        $alertElem
            .find("button.close")
                .removeAttr("data-dismiss")
                .click(function (elem) {
                    $alertElem.slideUp("fast", function() {
                        $alertElem.remove();
                    });
                });

        $alertContainer.prepend($alertElem);
        $alertElem.slideDown("fast");

        if (settings.container === "fixed") {
            $alertContainer.reveal();
        }

        if (settings.autoDismiss) {
            scheduleAlertDismiss($alertElem);
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


