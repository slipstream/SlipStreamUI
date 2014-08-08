// Pattern from:
// http://appendto.com/2010/10/how-good-c-habits-can-encourage-bad-javascript-habits-part-1/

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

    function builder(method, url) {
        return {
            config: {
                type: method,
                url: url
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
                this.config.success = callback; // Arguments: respBody, status, resp
                return this;
            },
            onSuccessRedirectURL: function (url){
                this.config.success = function() {
                    window.location = url;
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
        put_legacy: function(url, data, callback, type) {
                return ajax_request('PUT', url, data, callback, type);
            },
        delete_legacy: function(url, data, callback, type) {
                return ajax_request('DELETE', url, data, callback, type);
            }
    };

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
