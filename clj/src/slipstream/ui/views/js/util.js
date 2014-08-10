jQuery( function() { ( function( $$, Util, $, undefined ) {

    // Inspired from: http://stackoverflow.com/a/1186309
    // If more complex form serialization is needed, see https://github.com/macek/jquery-serialize-object
    // TODO: Rather use extend? $.fn.extend({
    $.fn.serializeObject = function () {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function() {
            $$.Util.setOrPush(o, this.name, this.value);
        });
        return o;
    };

    // TODO: How to extend Objecto to have this function?
    // $.fn.setOrPush = function (key, value) {
    // Object.prototype.setOrPush = function (key, value) {
    Util.setOrPush = function (object, key, value) {
        if (object[key] !== undefined) {
            if (!object[key].push) {
                object[key] = [object[key]];
            }
            object[key].push(value || '');
        } else {
            object[key] = value || '';
        }
        return object;
    };

    Util.URLQueryParams = {
        // If the query param key string in not contained in any other key, this is faster:
        getValue: function (param) {
            try {
                return window.location.search.split(param+"=")[1].split("&")[0];
            } catch (e) {
                return undefined;
            }
        },
        getValuePrecise: function (param) {
            var query = window.location.search;
            if (!query) {
                return undefined;
            }
            var entries = query.substring(1,query.length).split("&");
            for (var index in entries){
                var keyVal = entries[index].split("=");
                if (keyVal[0] == param){
                    return keyVal[1];
                }
            }
        }
    };

}( window.SlipStream = window.SlipStream || {}, window.SlipStream.Util = {}, jQuery ));});
