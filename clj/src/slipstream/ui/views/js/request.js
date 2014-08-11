jQuery( function() { ( function( $$, $, undefined ) {

    function builder(method, url) {
        return {
            intern: {
                serialization: "queryString",
                // serialization: "json"
                always: undefined // doesn't belong to the ajax settings but on the returned Promise object
            },
            settings: {
                type: method, // values: "GET", "POST", "PUT", "DELETE"
                url: url,
                data: undefined,
                contentType: undefined, // jQuery defaults to "application/x-www-form-urlencoded; charset=UTF-8"
                success: undefined, // function (data, textStatus, jqXHR) {}
                error: undefined    //function (jqXHR, textStatus, errorThrown) {}
            },
            url: function (url) {
                this.settings.url = url;
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

                $$.Util.setOrPush(this.settings, "success", callback);
                // this.settings.success = callback;
                return this;
            },
            onSuccessRedirectURL: function (url){
                this.onSuccess(function () {
                    // TODO: Which one if the correct way to redirect?
                    // window.location = url;
                    window.location.assign(url);
                });
                return this;
            },
            onSuccessFollowRedirectInURL: function (){
                this.onSuccess(function () {
                    // TODO: Which one if the correct way to redirect?
                    // window.location = url;
                    var redirectURL = $$.Util.URLQueryParams.getValue("redirectURL"),
                        rootURL = "/";
                    window.location.assign(redirectURL || rootURL);
                });
                return this;
            },
            onSuccessFollowRedirectInResponseHeader: function (){
                this.onSuccess(function (data, textStatus, jqXHR) {
                    // TODO: Which one if the correct way to redirect?
                    // window.location = url;
                    console.log(jqXHR.getResponseHeader("Location"));
                    // window.location.assign(url);
                });
                return this;
            },
            onSuccessAlert: function (titleOrMsg, msg){
                var showSuccessAlert = function () { $$.Alert.showSuccess(titleOrMsg, msg); };
                this.onSuccess(showSuccessAlert);
                // this.settings.success = showSuccessAlert;
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
                $$.Util.setOrPush(this.settings, "error", callback);
                // this.settings.error = callback;
                return this;
            },
            onErrorAlert: function (titleOrMsg, msg){
                var showErrorAlert = function () { $$.Alert.showError(titleOrMsg, msg); };
                this.onError(showErrorAlert);
                return this;
            },
            onErrorStatusCodeAlert: function (statusCode, titleOrMsg, msg){
                var callback = function (jqXHR, textStatus, errorThrown) {
                    if (statusCode == jqXHR.status) {
                        $$.Alert.showError(titleOrMsg, msg);
                    }
                };
                this.onError(callback);
                return this;
            },
            always: function (callback){
                // An alternative construct to the complete callback
                // option, the .always() method replaces the deprecated
                // .complete() method.

                // Callback signature: jqXHR.always(function( data|jqXHR, textStatus, jqXHR|errorThrown ) { });
                this.intern.always = callback;
                return this;
            },
            dataObject: function (object) {
                this.settings.data = object;
                return this;
            },
            // setDataObjectAsJSON: function (object) {
            //     this.settings.data = JSON.stringify(object);
            //     this.settings.contentType = "application/json; charset=UTF-8";
            // },
            // setDataObjectAsQueryString: function (object) {
            //     this.settings.data = $.param(object);
            //     this.settings.contentType = "application/x-www-form-urlencoded; charset=UTF-8";
            // },
            serialization: function (serialization) {
                this.intern.serialization = serialization;
                return this;
            },
            send: function () {
                switch (this.intern.serialization) {
                case "json":
                    this.settings.contentType = "application/json; charset=UTF-8";
                    if (this.settings.data) {
                        this.settings.data = JSON.stringify(this.settings.data);
                    }
                    break;
                case "queryString":
                    // jQuery would handle this case per default in the same way,
                    // but we do it explicitely to remove black magic.
                    this.settings.contentType = "application/x-www-form-urlencoded; charset=UTF-8";
                    if (this.settings.data) {
                        this.settings.data = $.param(this.settings.data);
                    }
                    break;
                default:
                    throw new Error( "Serialization type '" +
                        this.intern.serialization +
                        "' is not supported." +
                        " Try 'json' or 'queryString'.");
                }
                return jQuery.ajax(this.settings);
            },
            useToSubmitForm: function (sel) {
                var request = this,
                    $form = $("form" + sel),
                    url = $form.attr("action");
                // StatusCode 0: No internet connection.
                request.onErrorStatusCodeAlert(0, "Something strange out there",
                    "Sorry, but we're having trouble connecting to SlipStream. This problem is" +
                     "usually the result of a broken Internet connection. You can try" +
                     "refreshing this page and doing the request again.")
                    // .serialization("json")
                    .url(url);
                $form.off("submit");
                $form.submit(function (event) {
                    if ($form.data("submitted") === true) {
                        // Previously submitted - don't submit again
                        return false;
                    }
                    // Mark it so that the next submit can be ignored
                    $form.data("submitted", true);
                    event.preventDefault();
                    request.dataObject($(this).serializeObject())
                        .send()
                        .always(request.intern.always)
                        .always(function () {
                            // Mark it so that the next submit can be performed
                            $form.data("submitted", false);
                        });
                    return false;
                });
            }
        };
    }

    $$.Request = {
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
