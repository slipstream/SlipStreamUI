jQuery( function() { ( function( $$, util, $, undefined ) {

    // String object prototype extensions

    $.extend(String.prototype, {
        trimFromLastIndexOf: function(str) {
            var lastIndexOfStr = this.lastIndexOf(str);
            if (lastIndexOfStr === -1) {
                return this.toString();
            } else {
                return this.substring(0, lastIndexOfStr);
            }
        },

        trimLastURLSegment: function() {
            return this.trimFromLastIndexOf("/");
        },

        trimUpToFirstIndexOf: function(str) {
            // Remove all chars from 'this' up to and including 'str'.
            var firstIndexOfStr = this.indexOf(str);
            if (firstIndexOfStr === -1) {
                return this.toString();
            } else {
                return this.substring(firstIndexOfStr + 1);
            }
        },

        trimPrefix: function(prefix) {
            // Remove 'prefix' string from the begining of 'this' string.
            var firstIndexOfStr = this.indexOf(prefix);
            if (firstIndexOfStr !== 0) {
                return this.toString();
            } else {
                return this.substring(prefix.length, this.length);
            }
        },

        removeLeadingSlash: function() {
            return this.trimPrefix("/")
        },

        trimSuffix: function(suffix) {
            // Remove 'suffix' string from the end of 'this' string.
            var lastIndexOfStr = this.lastIndexOf(suffix)
                newLength = this.length - suffix.length;
            if (lastIndexOfStr !== newLength) {
                return this.toString();
            } else {
                return this.substring(0, newLength);
            }
        }
    });


    // jQuery extensions

    $.fn.extend({
        // Toggle disabled status of buttons, inputs and anchors
        // Inspired from: http://stackoverflow.com/a/16788240
        disable: function(state) {
            return this.each(function() {
                var $this = $(this);
                if($this.is('input, button'))
                  this.disabled = state;
                else
                  $this.toggleClass('disabled', state);
            });
        },
        enable: function(state) {
            this.disable(!state);
        },

        // Inspired from: http://stackoverflow.com/a/1186309
        // If more complex form serialization is needed, see https://github.com/macek/jquery-serialize-object
        serializeObject: function () {
            var o = {};
            var a = this.serializeArray();
            $.each(a, function() {
                $$.util.setOrPush(o, this.name, this.value);
            });
            return o;
        },

        onAltEnterPress: function (callback) {
            if ($.isFunction(callback)) {
                $(this).keypress(function (e) {
                    if (e.altKey && e.keyCode === 13) {
                        e.preventDefault();
                        callback();
                      }
                });
            }
            return;
        }

    });

    // TODO: How to extend Object to have this function?
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
            return path.trimLastURLSegment();
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
        isViewName: function (viewName) {
            return this.getViewName() === viewName;
        }
    };

}( window.SlipStream = window.SlipStream || {}, window.SlipStream.util = {}, jQuery ));});
