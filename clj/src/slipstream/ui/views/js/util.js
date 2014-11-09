jQuery( function() { ( function( $$, util, $, undefined ) {

    // Inspired from: http://stackoverflow.com/a/1186309
    // If more complex form serialization is needed, see https://github.com/macek/jquery-serialize-object
    // TODO: Rather use extend? $.fn.extend({
    $.fn.serializeObject = function () {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function() {
            $$.util.setOrPush(o, this.name, this.value);
        });
        return o;
    };

    // TODO: How to extend Objecto to have this function?
    // $.fn.setOrPush = function (key, value) {
    // Object.prototype.setOrPush = function (key, value) {
    util.setOrPush = function (object, key, value) {
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

    util.url = {
        getCurrentURLBase: function () {
            var path = window.location.pathname; // URL without query params
            var indexOfNewStr = path.lastIndexOf("/new");
            if (indexOfNewStr != -1) {
                path = path.substring(0, indexOfNewStr);
            }
            if (path === "/module") {
                return "/";
            }
            return path;
        },
        getParentResourceURL: function () {
            var path = window.location.pathname; // URL without query params
            return path.substring(0, path.lastIndexOf("/"));
        },
        redirectTo: function (url) {
            // TODO: Which one if the correct way to redirect?
            // window.location = url;
            window.location.assign(url);
        },
        redirectToCurrentURLBase: function () {
            this.redirectTo(this.getCurrentURLBase());
        },
        redirectToParentResourceURL: function () {
            this.redirectTo(this.getParentResourceURL());
        }
    };

    util.urlQueryParams = {
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

    util.form = {
        addHiddenField: function ($form, fieldName, fieldValue) {
            // Clean up hidden field with the same name before adding it.
            $form.children("input:hidden[name=" + fieldName + "]").remove();
            $("<input>")
                .attr("type", "hidden")
                .attr("name", fieldName)
                .attr("value", fieldValue)
                .appendTo($form);
        },
        setField: function ($form, fieldName, fieldValue) {
            // If the field doesn't exists already in the form, this is a no-op.
            $form.find("input[name=" + fieldName + "]").val(fieldValue);
        }
    };

    util.meta = {
        getMetaValue: function (name) {
            return $("meta[name=" + name + "]").attr("content");
        },
        getPageType: function () {
            return this.getMetaValue("ss-page-type");
        },
        isPageType: function (pageType) {
            return this.getPageType() === pageType;
        },
        getUserType: function () {
            return this.getMetaValue("ss-user-type");
        },
        isSuperUserLoggegIn: function () {
            return this.getUserType() === "super";
        },
        getViewName: function () {
            return this.getMetaValue("ss-view-name");
        },
        isView: function (viewName) {
            return this.getViewName() === viewName;
        }
    };

}( window.SlipStream = window.SlipStream || {}, window.SlipStream.util = {}, jQuery ));});
