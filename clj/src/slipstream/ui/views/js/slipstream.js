// Pattern from:
// http://appendto.com/2010/10/how-good-c-habits-can-encourage-bad-javascript-habits-part-1/

// import form.js;
 // form.js;

jQuery( function() { ( function( $$, $, undefined ) {

    function ajax_request(method, url, data, callback, type) {
        if (jQuery.isFunction(data)) {
            callback = data;
            data = {};
        }
        return jQuery.ajax({
            type: method,
            url: url,
            data: data,
            success: callback,
            dataType: type
        });
    }

    // {
    //     contentType: "application/x-www-form-urlencoded; charset=UTF-8",
    //     data: $form.serialize(),
    //     // contentType: "application/json; charset=UTF-8",
    //     // data: JSON.stringify($form.serializeObject()),
    //     sucess: function (data, textStatus, jqXHR) {},
    //     error: function (jqXHR, textStatus, errorThrown) {},
    //     type: "GET",
    //     type: "POST",
    //     type: "PUT",
    //     type: "DELETE",
    //     type: null, // values: "GET", "POST", "PUT", "DELETE"
    //     url: "/"}

    function builder(method, url) {
        return {
            config: {
                type: method, // values: "GET", "POST", "PUT", "DELETE"
                url: url,
                sucess: [], // function (data, textStatus, jqXHR) {},
                error: [], //function (jqXHR, textStatus, errorThrown) {}

                // always: function (r) {
                //     console.log(arguments);
                //     // console.log("always - StatusCode: " + r.status);
                // },
                // done: function (r) {
                //     console.log(arguments);
                //     // console.log("done - StatusCode: " + r.status);
                // }
            },
            onSuccess: function (callback){
                // Callback when the request is performed sucessfully
                // Callback signature: function (data, textStatus, jqXHR) {}
                this.config.success = callback;
                return this;
            },
            onSuccessRedirectURL: function (url){
                this.config.success = function() {
                    // TODO: Which one if the correct way to redirect?
                    // window.location = url;
                    window.location.assign(url);
                };
                return this;
            },
            onError: function (callback){
                this.config.error = callback;
                return this;
            },
            onFail: function (callback){
                this.config.fail = callback;
                return this;
            },
            onErrorAlert: function (titleOrMsg, msg){
                var showErrorAlert = function () { $$.alert.showError(titleOrMsg, msg); };
                this.config.error = showErrorAlert;
                this.config.fail = showErrorAlert;
                return this;
            },
            onSuccessAlert: function (titleOrMsg, msg){
                var showSuccessAlert = function () { $$.alert.showSuccess(titleOrMsg, msg); };
                this.config.success = showSuccessAlert;
                return this;
            },
            dataType: function (dataType){
                this.config.dataType = dataType;
                return this;
            },
            send: function () {
                return jQuery.ajax(this.config);
            }
        };
    }

    $$.request = {
        put: function (url) {
            return builder("PUT", url);
        },
        delete: function (url) {
            return builder("DELETE", url);
        },
        post: function (url) {
            return builder("POST", url);
        }
        // ,
        // put_legacy: function(url, data, callback, type) {
        //         return ajax_request('PUT', url, data, callback, type);
        //     },
        // delete_legacy: function(url, data, callback, type) {
        //         return ajax_request('DELETE', url, data, callback, type);
        //     }
    };


    // Query URL params

    $$.urlQueryParam = {
        value: function (key) {
            try {
                return window.location.search.split(key+"=")[1].split("&")[0];
            } catch (e) {
                return null;
            }
        },
        value_first_try: function (key) {
            var query = window.location.search;
            if (!query) {
                return null;
            }
            var entries = query.substring(1,query.length).split("&");
            for (var index in entries){
                var keyVal = entries[index].split("=");
                if (keyVal[0] == key){
                    return keyVal[1];
                }
            }
        }
    };

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
