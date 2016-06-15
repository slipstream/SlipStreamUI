jQuery( function() { ( function( $$, $, undefined ) {

    // Examples:
    //
    // SlipStream.request.get("/user/you").send()
    //
    // request = SlipStream.request
    //                     .get()
    //                     .onErrorStatusCode(404, function(){
    //                       alert("not there :(")
    //                     })
    //                     .onErrorStatusCode(403, function(){
    //                       alert("hum... you aren't supposed to go there...")
    //                     });
    // request.url("/user/you").send();
    // request.url("/user/super").send();
    // request.url("/user/not-there").send();

    var statusCodeMessages = {

            // Only needed for 4xx and 5xx errors to display proper UI alerts,
            // since sometimes the 'errorThrown' of the AJAX response is not coherent
            // with the status code ('jqXHR.status').

            400: "Bad Request",
            401: "Unauthorized",
            402: "Payment Required",
            403: "Forbidden",
            404: "Not Found",
            405: "Method Not Allowed",
            406: "Not Acceptable",
            407: "Proxy Authentication Required",
            408: "Request Timeout",
            409: "Conflict",
            410: "Gone",
            411: "Length Required",
            412: "Precondition Failed",
            413: "Payload Too Large",
            414: "Request-URI Too Long",
            415: "Unsupported Media Type",
            416: "Requested Range Not Satisfiable",
            417: "Expectation Failed",
            418: "I'm a teapot",
            419: "Authentication Timeout",
            422: "Unprocessable Entity",
            423: "Locked",
            424: "Failed Dependency",
            426: "Upgrade Required",
            428: "Precondition Required",
            429: "Too Many Requests",
            431: "Request Header Fields Too Large",

            500: "Internal Server Error",
            501: "Not Implemented",
            502: "Bad Gateway",
            503: "Service Unavailable",
            504: "Gateway Timeout",
            505: "HTTP Version Not Supported",
            506: "Variant Also Negotiates",
            507: "Insufficient Storage",
            508: "Loop Detected",
            511: "Network Authentication Required",
            520: "Unknown Error"
        };

    function getMessageForStatusCode(statusCode) {
        return statusCodeMessages[statusCode] || "[Unknown status code " + statusCode + "]";
    }

    function startFeedbackForRequestInProgress(withLoadingScreen) {
        if (withLoadingScreen) {
            $("#ss-loading-screen").showLoadingScreen();
        } else {
            // Replace the header icon with the rotating refresh icon.
            $(".ss-header-title-icon .glyphicon").addClass("ss-icon-loading");
        }
    }

    function stopFeedbackForRequestInProgress() {
        // Always stop the feedback of the request in progress,
        // independently of the withLoadingScreen value.
        $("#ss-loading-screen").hideLoadingScreen();
        // Remove the rotating refresh icon from header.
        $(".ss-header-title-icon .glyphicon").removeClass("ss-icon-loading");
    }

    function builder(method, url) {
        return {
            intern: {
                withLoadingScreen: true,            // See .withLoadingScreen() fn below
                serialization: undefined,           // See .serialization() fn below
                onDataTypeParseError: undefined,    // See .onDataTypeParseErrorAlert() fn below
                always: undefined,                  // See .always() fn below
                errorStatusCodeCallbacks: {},       // See .onErrorStatusCode() fn below
                errorStatusCodeAlerts: {},          // See .onErrorStatusCodeAlert() fn below
                validation: undefined               // See .validation() fn below
            },
            settings: {
                type: method,     // values: "GET", "POST", "PUT", "DELETE"
                url: url,
                async: true,            // See .async(enable) fn below
                data: undefined,
                dataType: undefined,    // See .dataType() fn below
                contentType: undefined, // See .serialization() fn below
                success: undefined,     // See .onSuccess() fn below
                error: undefined        // See .onError() fn below
            },
            always: function (callback){
                // An alternative construct to the complete callback
                // option, the .always() method replaces the
                // deprecated .complete() method. In response to a
                // successful request, the function's arguments are
                // the same as those of .done(): data, textStatus, and
                // the jqXHR object. For failed requests the arguments
                // are the same as those of .fail(): the jqXHR object,
                // textStatus, and errorThrown. Refer to
                // deferred.always() for implementation details.

                // NB: Callback fn which doesn't belong to the ajax settings but
                // on the returned Promise object.

                // Callback signature: jqXHR.always(function( data|jqXHR, textStatus, jqXHR|errorThrown ) { });
                $$.util.object.setOrPush(this.intern, "always", callback);
                return this;
            },
            async: function (enable) {
                if ($.type(enable) === "boolean") {
                    this.settings.async = enable;
                } else {
                    this.settings.async = true;
                }
                return this;
            },
            data: function (dataArg) {
                this.settings.originalData = dataArg;
                return this;
            },
            dataType: function (type) {
                // The type of data that you're expecting back from the server. If
                // none is specified, jQuery will try to infer it based on the MIME
                // type of the response
                // 'type' must be one among: 'xml', 'json', 'script', or 'html'.
                this.settings.dataType = type;
                return this;
            },
            onSuccess: function (callback){
                // A function to be called if the request succeeds.
                // The function gets passed three arguments: The data
                // returned from the server, formatted according to
                // the dataType parameter; a string describing the
                // status; and the jqXHR object. If several callback
                // are passed, they will be called in turn.

                // Callback signature: function (data, textStatus, jqXHR) {}

                $$.util.object.setOrPush(this.settings, "success", callback);
                return this;
            },
            onSuccessReloadPageWithoutQueryParamsInURL: function (){
                this.onSuccess(function () {
                    $$.util.url.redirectToCurrentURLBase();
                });
                return this;
            },
            onSuccessRedirectTo: function (url){
                this.onSuccess(function () {
                    $$.util.url.redirectTo(url);
                });
                return this;
            },
            onSuccessFollowRedirectInURL: function (){
                this.onSuccess(function () {
                    var redirectURL = $$.util.urlQueryParams.getValue("redirectURL"),
                        rootURL = "/";
                    $$.util.url.redirectTo(redirectURL || rootURL);
                });
                return this;
            },
            onSuccessFollowRedirectInResponseHeader: function (){
                this.onSuccess(function (data, textStatus, jqXHR) {
                    try {
                        var locationHeader = jqXHR.getResponseHeader("Location");
                        if (! locationHeader) {
                            throw "No Location Header found in response of request: " +
                                    this.type + " '" + this.url + "' => Redirecting to '/' instead";
                        }
                        $$.util.url.redirectTo(locationHeader);
                    }
                    catch (e) {
                        console.error(e);
                        $$.util.url.redirectTo("/");
                    }
                });
                return this;
            },
            onSuccessAlert: function (titleOrMsg, msg){
                var showSuccessAlert = function () { $$.alert.showSuccess(titleOrMsg, msg); };
                this.onSuccess(showSuccessAlert);
                return this;
            },
            onError: function (callback){
                // A function to be called if the request fails. The
                // function receives three arguments: The jqXHR
                // object, a string describing the type of error that
                // occurred and an optional exception object, if one
                // occurred. Possible values for the second argument
                // (besides null) are "timeout", "error", "abort", and
                // "parsererror". When an HTTP error occurs,
                // errorThrown receives the textual portion of the
                // HTTP status, such as "Not Found" or "Internal
                // Server Error."

                // Callback signature: function (jqXHR, textStatus, errorThrown) {}
                $$.util.object.setOrPush(this.settings, "error", callback);
                return this;
            },
            onErrorAlert: function (titleOrMsg, msg){
                var showErrorAlert = function () { $$.alert.showError(titleOrMsg, msg); };
                this.onError(showErrorAlert);
                return this;
            },
            onErrorAlertFixed: function (titleOrMsg, msg){
                var showErrorAlertFixed = function () { $$.alert.showErrorFixed(titleOrMsg, msg); };
                this.onError(showErrorAlertFixed);
                return this;
            },
            onErrorStatusCode: function (statusCode, callback){
                $$.util.object.setOrPush(this.intern.errorStatusCodeCallbacks, statusCode, callback);
                return this;
            },
            onErrorStatusCodeAlert: function (statusCode, titleOrMsg, msg){
                this.intern.errorStatusCodeAlerts[statusCode] = [titleOrMsg, msg];
                return this;
            },
            onDataTypeParseErrorAlert: function (titleOrMsg, msg){
                // When a dataType is set, a default error alert is configured
                // when sending the request (see below).
                // onDataTypeParseErrorAlert(titleOrMsg, msg) overrides the default
                // alert.
                this.intern.onDataTypeParseError = function (jqXHR, textStatus, errorThrown) {
                    if (textStatus === "parseerror") {
                        $$.alert.showError(titleOrMsg, msg);
                    }
                };
                return this;
            },
            preventDefaultErrorHandling: function () {
                console.log("preventing DefaultErrorHandling");
                this.intern.preventDefaultErrorHandling = true;
                return this;
            },
            serialization: function (serialization) {
                // The 'serialization' is used on the .send() fn below to set the
                // 'contentType' and to serialize the data object accordingly
                // jQuery defaults to "application/x-www-form-urlencoded; charset=UTF-8"
                // 'serialization' must be one among: 'json' or 'queryString'.
                this.intern.serialization = serialization;
                return this;
            },
            send: function () {
                var request = this;
                if (this.intern.validation && this.intern.validation.call(request) === false) {
                    // Ensure this.intern.always fn (or fns) are called
                    // if ($.isFunction(this.intern.always) || $.type(this.intern.always) === "array") {
                    if (this.intern.always) {
                        this.intern.always.call(request);
                    }
                    return false;
                }
                switch (this.intern.serialization) {
                case "json":
                    this.settings.contentType = "application/json; charset=UTF-8";
                    if (this.settings.originalData) {
                        this.settings.data = JSON.stringify(this.settings.originalData);
                    }
                    break;
                case undefined:
                    // jQuery would handle this case per default in the same way,
                    // setting 'contentType' to "application/x-www-form-urlencoded; charset=UTF-8"
                    // but we do it explicitely to remove black magic.
                case "queryString":
                    this.settings.contentType = "application/x-www-form-urlencoded; charset=UTF-8";
                    switch ($.type(this.settings.originalData)) {
                        case "object":
                            this.settings.data = $.param(this.settings.originalData);
                            break;
                        case "string":
                        case "undefined":
                            break;
                        default:
                            this.settings.data = this.settings.originalData.toString();
                    }
                    break;
                default:
                    throw new Error( "Serialization type '" +
                        this.intern.serialization +
                        "' is not supported." +
                        " Try 'json' or 'queryString'.");
                }

                // StatusCode 0: No internet connection.
                this.onErrorStatusCodeAlert(0, "Something strange out there",
                    "We're having troubles connecting to SlipStream. This problem is " +
                     "usually the result of a broken Internet connection. You can try " +
                     "refreshing this page and doing the request again.");

                if (!this.intern.defaultErrorHandlerAlreadySetup) {
                    var errorStatusCodeCallbacks = this.intern.errorStatusCodeCallbacks,
                        errorStatusCodeAlerts    = this.intern.errorStatusCodeAlerts;
                    this.onError( function(jqXHR, textStatus, errorThrown){
                        if (textStatus != "error") {
                            return;
                        }
                        var statusCodeCallback = errorStatusCodeCallbacks[jqXHR.status];
                        if ( $$.util.isSomething.callable(statusCodeCallback) ) {
                            statusCodeCallback.call(this, jqXHR.status, jqXHR, textStatus, errorThrown);
                        }
                        var statusCodeAlertTitleAndMsg = errorStatusCodeAlerts[jqXHR.status];

                        if (statusCodeAlertTitleAndMsg !== undefined) {
                            $$.alert.showError.apply(this, statusCodeAlertTitleAndMsg);
                        } else if(request.intern.preventDefaultErrorHandling !== true) {
                            var responseDetail = jqXHR.responseJSON && (
                                                    jqXHR.responseJSON.detail || (
                                                        jqXHR.responseJSON.error &&
                                                            jqXHR.responseJSON.error.detail
                                                    )
                                                );
                            if (responseDetail === undefined) {
                                // If the response is not a JSON, it might be a complete valid HTML error page.
                                try {
                                    // In that case, we try retrieve the details from the header subtitle.
                                    responseDetail = $(jqXHR.responseText).find(".ss-header-subtitle").text();
                                } catch (e) {
                                    // If this fails, we just use the raw response text.
                                    responseDetail = jqXHR.responseText;
                                }
                            }
                            $$.alert.showError(
                                responseDetail,
                                "<code>Error " + jqXHR.status + " - <span title='\"errorThrown\" message in AJAX reply was: " + errorThrown + "'>" + getMessageForStatusCode(jqXHR.status) + "</code>"
                                );
                        }
                    });
                    this.intern.defaultErrorHandlerAlreadySetup = true;
                }

                if (this.settings.dataType) {
                    var dataType = this.settings.dataType;
                    this.onError(
                        this.intern.onDataTypeParseError ||
                        function (jqXHR, textStatus, errorThrown) {
                            if (textStatus === "parsererror") {
                                $$.alert.showError(
                                    "AJAX Request Error",
                                    "Unable to parse the data received from the server as '" + dataType + "'."
                                );
                            }
                        });
                }

                if ( ! this.withLoadingScreenAlreadyAdded ) {
                    $$.util.object.setOrPush(this.settings, "beforeSend", startFeedbackForRequestInProgress.partial(this.intern.withLoadingScreen));
                    this.withLoadingScreenAlreadyAdded = true;
                }

                var ajaxRequest = jQuery.ajax(this.settings)
                                        .always(this.intern.always,
                                                stopFeedbackForRequestInProgress);
                return ajaxRequest;
            },
            url: function (url) {
                this.settings.url = url;
                return this;
            },
            useToSubmitForm: function (sel, preSubmitCallback) {
                var request = this,
                    $form = $(sel);
                $form.enableLiveInputValidation();
                request
                    // .serialization("json") // NOTE: Uncomment to send a JSON to the server
                    // .dataType("json")      // NOTE: Uncomment to request the server a JSON reply
                    // .dataType("xml")       // NOTE: Uncomment to request the server a XML reply
                    // .dataType("html")      // NOTE: Uncomment to request the server a HTML reply
                    .always(function () {
                        // Unflag the form as submitted after the request is done
                        // so that the next submit can be performed
                        $form.data("submitted", false);
                    })
                    .validation(function(){
                        var allowRequest = $form.isValidForm();
                        if (! allowRequest) {
                            $$.alert.showError($form.getGenericFormAlertMsg("error"));
                        }
                        return allowRequest;
                    });
                $form.off("submit");

                $form.submit(function (event) {
                    if ($form.data("submitted") === true) {
                        // Previously submitted - don't submit again
                        return false;
                    }
                    // Mark it so that the next submit can be ignored
                    $form.data("submitted", true);
                    event.preventDefault();

                    // The preSubmitCallback(request, $form) can be used to customise
                    // the request and the form before it's submitted.
                    if ( $.isFunction(preSubmitCallback) ) {
                        preSubmitCallback(request, $form);
                    }

                    $$.util.leavingConfirmation.reset();

                    request
                        .url(request.settings.url || $form.attr("action"))
                        .data($(this).serializeObject())
                        .send();
                    return false;
                });
            },
            validation: function (callback) {
                // Return true if the request might be send and false to stop it.
                // This request object will be passed as the 'this' argument to
                // the validation callback fn.
                // In contrast to other callbacks of this request object (like
                // onSuccess... or onError...) not all validation callbacks might
                // be executed. They are executed in the order they were added to
                // the request object and the first returning false breaks the
                // validation chain and stops the request from being send.
                // It is expected that a validation callback stopping a request
                // properly informs the user, for example with an $$.alert.showError() call.
                // Note that this.intern.always fn (or fns) will be called anyway.
                $$.util.object.setOrPush(this.intern, "validation", callback);
                return this;
            },
            withLoadingScreen: function (enable) {
                if ($.type(enable) === "boolean") {
                    this.intern.withLoadingScreen = enable;
                } else {
                    this.intern.withLoadingScreen = true;
                }
                return this;
            }
        };
    }

    $$.request = {
        get: function (url) {
            return builder("GET", url);
        },
        put: function (url) {
            return builder("PUT", url);
        },
        delete: function (url) {
            return builder("DELETE", url);
        },
        post: function (url) {
            return builder("POST", url);
        }
    };

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
