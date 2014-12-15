jQuery( function() { ( function( $$, $, undefined ) {

    function builder(method, url) {
        return {
            intern: {
                serialization: undefined,           // See .serialization() fn below
                onDataTypeParseError: undefined,    // See .onDataTypeParseErrorAlert() fn below
                always: undefined,                  // See .always() fn below
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
                this.settings.data = dataArg;
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
                    if (this.settings.data) {
                        this.settings.data = JSON.stringify(this.settings.data);
                    }
                    break;
                case undefined:
                    // jQuery would handle this case per default in the same way,
                    // setting 'contentType' to "application/x-www-form-urlencoded; charset=UTF-8"
                    // but we do it explicitely to remove black magic.
                case "queryString":
                    this.settings.contentType = "application/x-www-form-urlencoded; charset=UTF-8";
                    if (this.settings.data) {
                        this.settings.data = $.param(this.settings.data);
                    }
                    break;
                case "literalString":
                    this.settings.contentType = "application/x-www-form-urlencoded; charset=UTF-8";
                    if (this.settings.data) {
                        this.settings.data = this.settings.data.toString();
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
                    var errorStatusCodeAlerts = this.intern.errorStatusCodeAlerts;
                    this.onError( function(jqXHR, textStatus, errorThrown){
                        if (textStatus != "error") {
                            return;
                        }
                        var statusCodeAlertTitleAndMsg = errorStatusCodeAlerts[jqXHR.status];
                        if (statusCodeAlertTitleAndMsg === undefined) {
                            var responseDetail = jqXHR.responseJSON && jqXHR.responseJSON.detail;
                            if (responseDetail === undefined) {
                                // If the response is not a JSON, it is a complete valid HTML error page.
                                // In that case, we retrieve the details from the header subtitle.
                                responseDetail = $(jqXHR.responseText).find(".ss-header-subtitle").text();
                            }
                            $$.alert.showError(
                                "Error " + jqXHR.status + " - " + errorThrown,
                                responseDetail);
                        } else {
                            $$.alert.showError.apply(this, statusCodeAlertTitleAndMsg);
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
                var ajaxRequest = jQuery.ajax(this.settings)
                                        .always(this.intern.always);
                return ajaxRequest;
            },
            url: function (url) {
                this.settings.url = url;
                return this;
            },
            useToSubmitForm: function (sel, preSubmitCallback) {
                var request = this,
                    $form = $("form" + sel);
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
                            $form.showGenericErrorFormAlert();
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
