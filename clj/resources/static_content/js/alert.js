jQuery( function() { ( function( $$, $, undefined ) {

    var visibleAlertSel = "div.alert:visible";

    function dismiss($alertElem) {
        $alertElem
            .filter(visibleAlertSel)
                .slideUp("fast", function() {
                    $alertElem.remove();
                });
    }

    function scheduleAlertDismiss($alertElem) {
        // Wait longer for longer messages.
        var minDelay = 5000,
            delayMillisPerChar = 100, // empirical value
            msg = $alertElem.find(".alert-msg").text();
            delay = Math.max(minDelay, delayMillisPerChar * msg.countNonWhitespaceChars());
        $alertElem.scheduleAlertDismiss(delay);
    }

    function configureCustomDismissAnimation($alertElem) {
        $alertElem
            .find("button.close")
                .removeAttr("data-dismiss")
                .click(function (elem) {
                    dismiss($alertElem);
                });
    }

    // Configure alerts already present on page load

    $("#ss-alert-container-floating div[role=alert]:not(.hidden)").each(function (index, alertElem) {
        var $alertElem = $(alertElem);
        scheduleAlertDismiss($alertElem);
        configureCustomDismissAnimation($alertElem);
    });

    $("#ss-alert-container-fixed div[role=alert]:not(.hidden)").each(function (index, alertElem) {
        var $alertElem = $(alertElem);
        configureCustomDismissAnimation($alertElem);
    });

    var alertDefaultOptions = {
        type: "info",
        container: "floating",
        autoDismiss: true,
        title: undefined,
        msg: undefined
    };

    function findVisibleAlerts($alertContainer) {
        // If $alertContainer is given, the alerts are searched within it.
        // If not, all visible alerts in the body.
        return ($alertContainer instanceof jQuery) ? $alertContainer.find(visibleAlertSel) : $(visibleAlertSel);
    }

    function find($alertElem, $alertContainer) {
        return findVisibleAlerts($alertContainer).filter($alertElem);
    }

    function findByTitle(title, $alertContainer) {
        return findVisibleAlerts($alertContainer).filter(
            function(){
                return $(this).find(".alert-title").text() === title;
            });
    }

    function findByHTML(alertHTML, $alertContainer) {
        return findVisibleAlerts($alertContainer).filter(
            function(){
                return $(this).html() === alertHTML;
            });
    }

    function exactExistentAlert($alertElem, $alertContainer) {
        return findByHTML($alertElem.html(), $alertContainer).first();
    }

    function existentAlertSameTitle(title, $alertContainer) {
        return findByTitle(title, $alertContainer).first();
    }

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

        if (settings.title && existentAlertSameTitle(settings.title, $alertContainer).foundAny()) {
            $alertElem = existentAlertSameTitle(settings.title, $alertContainer);
        } else {
            $alertElem = $("#alert-" + settings.type)
                                .clone()
                                    .removeClass("hidden") // Bootstrap class
                                    .hide()
                                    .removeAttr("id")
                                    .find("button.close")
                                        .removeAttr("data-dismiss")
                                        .end();
        }

        $alertElem
            .find(".alert-msg")
            .html(settings.msg);

        if (settings.title) {
            $alertElem.find(".alert-title").html(settings.title);
        }

        if (! settings.isDismissible) {
            $alertElem
                .find("button.close")
                    .remove();
        }

        var $exactExistentAlert = exactExistentAlert($alertElem, $alertContainer);

        if ($exactExistentAlert.foundAny()){
            // The exact same alert (i.e. by HTML) already exists in this container.
            // Nothing to do.
            return $exactExistentAlert;
        }

        configureCustomDismissAnimation($alertElem);

        $alertContainer.prepend($alertElem);
        $alertElem.slideDown("fast");

        if (settings.container === "fixed") {
            $alertContainer.reveal();
        }

        if (settings.autoDismiss) {
            scheduleAlertDismiss($alertElem);
        }

        return $alertElem;
    }

    function showOfType(container, type, titleOrMsg, msg) {
        return show({
            type: type,
            container: container || "floating",
            autoDismiss: container === "floating",
            isDismissible: container === "floating",
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
        },
        dismissByTitle: function (title) {
            dismiss(findByTitle(title));
        }
    };

}( window.SlipStream = window.SlipStream || {}, jQuery ));});


