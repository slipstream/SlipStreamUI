jQuery( function() { ( function( $$, util, $, undefined ) {


    util.isSomething = {
        implementingMethod: function (something, methodName) {
            try {
                return $.type(something[methodName]) === "function";
            }
            catch (e) {
                return false;
            }
        },
        callable: function (something) {
            return util.isSomething.implementingMethod(something, "call");
        }
    };

    // String prototype extensions

    $.extend(String.prototype, {

        contains: function(str) {
            return (this.match(str) == str);
        },

        startsWith: function(str) {
            return (this.match("^" + str) == str);
        },

        endsWith: function(str) {
            return (this.match(str + "$") == str);
        },

        trimToMaxLength: function(maxLength, trimmingIndicatorStr) {
            var trimWith = trimmingIndicatorStr === undefined ? "..." : trimmingIndicatorStr;
            if ( trimWith.length >= maxLength ) {
                throw "maxLength (" + maxLength + ") must be greater than the length of the trimmingIndicatorStr ('" + trimWith + "')";
            } else if ( this.length > maxLength ) {
                return this.substring(0, maxLength - trimWith.length).trimRight() + trimWith;
            } else {
                return this.toString();
            }
        },

        trimFromFirstIndexOf: function(str) {
            var firstIndexOfStr = this.indexOf(str);
            if (firstIndexOfStr === -1) {
                return this.toString();
            } else {
                return this.substring(0, firstIndexOfStr);
            }
        },

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

        trimUpToLastIndexOf: function(str) {
            // Remove all chars from 'this' up to and including 'str'.
            var lastIndexOfStr = this.lastIndexOf(str);
            if (lastIndexOfStr === this.length) {
                return this.toString();
            } else {
                return this.substring(lastIndexOfStr + 1, this.length);
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

        ensurePrefix: function(prefix) {
            // Ensure that 'this' string begins with the 'prefix' string.
            var firstIndexOfStr = this.indexOf(prefix);
            if (firstIndexOfStr === 0) {
                return this.toString();
            } else {
                return prefix + this;
            }
        },

        prefixWith: function(prefix) {
            return prefix + this;
        },

        removeLeadingSlash: function() {
            return this.trimPrefix("/");
        },

        trimSuffix: function(suffix) {
            // Remove 'suffix' string from the end of 'this' string.
            var lastIndexOfStr = this.lastIndexOf(suffix),
                newLength = this.length - suffix.length;
            if (lastIndexOfStr !== newLength) {
                return this.toString();
            } else {
                return this.substring(0, newLength);
            }
        },

        suffixWith: function(suffix) {
            return this + suffix;
        },

        ensureSuffix: function(suffix) {
            // Ensure that 'this' string ends with the 'suffix' string.
            var lastIndexOfStr = this.lastIndexOf(suffix);
            if (lastIndexOfStr === this.length - suffix.length) {
                return this.toString();
            } else {
                return this + suffix;
            }
        },

        mightBeAnEmailAddress: function() {
            // As mentioned in http://stackoverflow.com/a/202528 the RFC of the
            // format of email address is so complex, that the only real way to
            // validate it is to send it an email ;)
            // However we can perform a basic validation to catch basic things:
            return this.match(".+\\@.+") ? true : false;
        },

        incrementFirstInteger: function() {
            return this.replace(/\d+/, function(match){
                return parseInt(match, 10) + 1;
            });
        },

        incrementLastInteger: function() {
            var lastIntMatch = this.match(/(?:.+?(\d+))+.*/),
                lastInt,
                increasedInt,
                lastIndexOfInt;
            if (lastIntMatch) {
                lastInt = lastIntMatch[1];
                increasedInt = parseInt(lastInt, 10) + 1;
                lastIndexOfInt = this.lastIndexOf(lastInt);
                return this.substring(0, lastIndexOfInt) + increasedInt + this.substring(lastIndexOfInt + lastInt.length);
            }
            return this.toString();
        },

        ensureSingleQuoted: function() {
            if (this.length === 0) {
                return "''";
            }
            return this.ensurePrefix("'").ensureSuffix("'");
        },

        isSingleQuoted: function() {
            return this.match(/^'.*'$/) ? true : false;
        },

        isDoubleQuoted: function() {
            return this.match(/^".*"$/) ? true : false;
        },

        isQuoted: function() {
            return this.isSingleQuoted() || this.isDoubleQuoted();
        },

        asSel: function() {
            // Helper function to use a CSS class as a jQuery selector.
            return "." + this;
        },

        isRootURL: function() {
            var re = new RegExp("^" + window.location.origin + "/?$");
            return re.test(this);
        },

        countNonWhitespaceChars: function() {
            return this.replace(/\W/g, "").length;
        },

        asInt: function(pattern) {
            // By default, parseInt() will stop parsing the string
            // before the first non-digit char. That means that both
            // "1234" and "1234abc" will be parsed as 1234. A regexp
            // pattern can be provided to extract the int from
            // somewhere else.
            if ( $.type(pattern) === "regexp" ) {
                var matched = this.match(pattern);
                if ( ! matched ) {
                    return NaN;
                }
                if ( matched.length !== 2 ) {
                    throw "One (and only one) capturing group is expected in pattern.";
                }
                return matched[1].asInt();
            }
            return parseInt(this, 10);
        },

        asCommaSeparatedListOfUniqueTags: function() {
            return this
                    .trim()
                    .split(/[^\w-]+/) // tags can only contain [a-zA-Z0-9-]
                    .sort()
                    .unique()
                    .filter($$.util.string.notEmpty)
                    .join(", ");
        },

        width: function self(font) {
            // Inspired from: http://stackoverflow.com/a/21015393
            if ( ! font ) {
                throw "Cannot calculate string width without a given font! Try with '12px arial'.";
            }
            // re-use canvas object for better performance
            var canvas   = self.canvas || (self.canvas = document.createElement("canvas"));
            var context  = canvas.getContext("2d");
            context.font = font;
            var metrics  = context.measureText(this);
            return Math.ceil(metrics.width);
        }

    });


    // Object prototype extensions

    // $.extend(Object.prototype, {
    //     // NOTE: It's highly recommended not to extend Object.prototype.
    //     //       It'll do far more than break jQuery.
    //     //       Sources: http://stackoverflow.com/a/1827611
    //     //                http://markmail.org/message/tv7vxcir6w3p2h5e
    // });

    util.object = {
        // NOTE: Doing this instead of extending Object.prototype.
        setOrPush: function (object, key, value) {
            if (object[key] !== undefined) {
                if (!object[key].push) {
                    object[key] = [object[key]];
                }
                object[key].push(value || '');
            } else {
                object[key] = value || '';
            }
            return object;
        },

        keysString: function(object, separator) {
            return Object.keys(object).join(separator || ", ");
        },

        valsString: function(object, separator) {
            var vals = [];
            $.each(object, function(k, v) {
                vals.push(v);
            });
            return vals.join(separator || ", ");
        }
    };


    // Array prototype extensions

    $.extend(Array.prototype, {

        isEmpty: function() {
            return this.length === 0;
        },

        notEmpty: function() {
            return this.length > 0 ? this : undefined;
        },

        first: function() {
            return this[0];
        },

        last: function() {
            return this.length > 0 ? this[this.length - 1] : undefined;
        },

        butLast: function() {
            return this.length > 0 ? this.splice(0, this.length - 1) : this;
        },

        getReversed: function() {
            // The native Array.prototype.reverse() will mutate the original array.
            // getReversed() returns a reversed copy, without changing the original.
            return this.slice().reverse();
        },

        call: function(thisArg, arg1, arg2, arg3, arg4) {
            // Equivalent to Function.prototype.call() on an Array of fns.
            var lastResult;
            $.each(this, function(index, f){
                if ($.isFunction(f)) {
                    // If 'f' returns 'false' (not a falsey value), '$.each()'
                    // will break the loop and return 'false'.
                    // Else, we return the last return value.
                    lastResult = f.call(thisArg, arg1, arg2, arg3, arg4);
                    return lastResult;
                }
                return true;
            });
            return lastResult;
        },

        sortObjectsByKey: function(key) {
            if ($.type(key) !== "string") {
                throw "Key must be a string.";
            }
            this.forEach(function(o){
                if ($.type(o) !== "object") {
                    throw "Array contains items that are not objects.";
                }
            });
            this.sort(function (a, b) {
                if (a[key] > b[key]) {
                    return 1;
                }
                if (a[key] < b[key]) {
                    return -1;
                }
                return 0;
                });
            return this;
        },

        contains: function(thing) {
            return ($.inArray(thing, this) === -1) ? false : true;
        },

        unique: function() {
            // From: http://stackoverflow.com/a/9229821
            var seen = {};
            var out = [];
            var len = this.length;
            var j = 0;
            for(var i = 0; i < len; i++) {
                var item = this[i];
                if(seen[item] !== 1) {
                    seen[item] = 1;
                    out[j++] = item;
                }
            }
            return out;
        }
    });


    // Boolean prototype extensions

    $.extend(Boolean.prototype, {
        not: function() {
            // Might be more clear to use in long jQuery chined API calls than
            // a mere '!' at the very begining of the call.
            return ! this.valueOf();
        }
    });


    // Function prototype extensions

    $.extend(Function.prototype, {

        // TODO: Contribute back this implementation of .partial() here:
        //       http://stackoverflow.com/questions/7282158/function-prototype-bind
        partial: function() {
            // Takes fewer than the normal arguments to 'this' fn, and
            // returns a fn that, when called, calls f with args + additional args.
            // The returned fn doesn't change the 'this' context, so that it is still
            // compatible with .call() and .apply(), i.e. .partial() is similar to
            // .bind(), but without locking the 'this' context override.
            // Ex: function f(a,b){return a + b;}; var f2 = f.partial(2); f2(3) === 5;
            var fn = this,
                firstArgs = Array.prototype.slice.call(arguments);
            return function() {
                var restArgs = Array.prototype.slice.call(arguments),
                    args = firstArgs.concat(restArgs);
                return fn.apply(this, args);
            };
        },

        identity: function() {
            return (this.valueOf) ? this.valueOf() : this;
        }

    });


    // jQuery extensions

    $.fn.extend({
        valOr: function(defaultVal) {
            var val = this.val();
            if (val === undefined) {
                return defaultVal;
            }
            return val;
        },

        id: function() {
            // Value of the 'id' attribute of the first element in the set of matched elements.
            return this.attr("id");
        },

        foundNothing: function() {
            // A more idiomatic way to check if a jQuery selection has no matches.
            return this.length === 0;
        },

        foundAny: function() {
            // A more idiomatic way to check if a jQuery selection has any matches.
            return this.length !== 0;
        },

        foundOne: function() {
            // A more idiomatic way to check if a jQuery selection has one match.
            return this.length === 1;
        },

        foundMany: function() {
            // A more idiomatic way to check if a jQuery selection has more than one match.
            return this.length > 1;
        },

        take: function(numberOfElementsToTake) {
            var n = numberOfElementsToTake === undefined ? 1 : numberOfElementsToTake;
            return this.filter(function(index, element){
                return n > index;
            });
        },

        takeLast: function(numberOfElementsToTakeFromEnd) {
            var totalNumberOfElements = this.length,
                n = numberOfElementsToTakeFromEnd === undefined ? 1 : numberOfElementsToTakeFromEnd;
            return this.filter(function(index, element){
                return n >= (totalNumberOfElements - index);
            });
        },

        takeFirstAndLast: function(numberOfElementsToTakeFromBothEnds) {
            return this
                    .take(numberOfElementsToTakeFromBothEnds)
                    .add(this.takeLast(numberOfElementsToTakeFromBothEnds));
        },

        textArray: function() {
            // Return a javascript native array with the results of
            // applying $().text() to each of the matched elements.
            // Compare with applying directly $().text() to the
            // matched elements, which returns the combined text
            // contents of each element in the set of matched
            // elements, including their descendants.
            // Source: http://api.jquery.com/text/
            var res = [];
            this.each( function() {
                res.push($(this).text());
            });
            return res;
        },

        filters: function() {
            // Like $().filter(function), but taking any number of be
            // predicate functions. They will be applied in order
            // following AND semantics (i.e. the first 'false' will
            // stop evaluation of following predicates). Only elements
            // returning true for all predicates will be included in
            // the result. See this.predicates for a number of useful
            // predicates.
            var predicates = Array.prototype.slice.call(arguments);
            return this
                    .filter(function( index, element ) {
                        return $$.util.boolean.cast(predicates.call(this, index, element ));
                    });
        },

        predicates: {
            // As per $.filter(), predicates receive (Integer index, Element element)
            // as arguments.
            isVisibleNode: function( index, element ) {
                // This is to be used with $.filters() above. In most
                // cases though, using $(... + ":visible") might be
                // faster.
                return $(this).is(":visible");
            },

            isNodeWithOnlyText: function( index, element ) {
                var $contents = $(this).contents(),
                    containtedElem = $contents.get(0);
                return $contents.foundOne() &&
                         containtedElem.nodeType === Node.TEXT_NODE &&
                         $$.util.string.isEmpty(containtedElem.nodeValue).not();
            },

            isNodeWithOverflow: function( index, element ) {
                // Works only for visible elements (i.e. not with display: none).
                return this.offsetWidth < this.scrollWidth;
            },

            isNodeWithOverflowText: function( index, element ) {
                // Like (predicates.isNodeWithOnlyText AND predicates.isNodeWithOverflow)
                // but tries to guess it even for not visible elements.
                var $elem = $(this);
                if ( ! $elem.predicates.isNodeWithOnlyText.call(this, index, element) ) {
                    return false;
                }
                if ( $elem.is(":visible") ) {
                    return $elem.predicates.isNodeWithOverflow.call(this, index, element);
                }
                // If the element is NOT visible, try to foresee the
                // overflow if a fix width is specified in CSS.
                var elemWidthStr = $elem.css("width") || $elem.css("max-width") || "",
                    elemWidth    = elemWidthStr.asInt(/^(\d+)px$/);
                return elemWidth && elemWidth < $elem.renderedTextWidth();
            }
        },

        addOfClass: function(cls) {
            // Helper to add elements to the selection by class, to make it equivalent to hasClass(), addClass()...
            return this.add(cls.asSel());
        },

        filterOfClass: function(cls) {
            // Helper to filter elements by class, to make it equivalent to hasClass(), addClass()...
            return this.filter(cls.asSel());
        },

        findOfClass: function(cls) {
            // Helper to find elements by class, to make it equivalent to hasClass(), addClass()...
            return this.find(cls.asSel());
        },

        findClosest: function(selector) {
            // Like closest() but downwards, i.e. like find().first() but including itself. ;)
            return this.is(selector) ? this.filter(selector).first() : this.find(selector).first();
        },

        findIncludingItself: function(selector) {
            var $selection = this.find(selector);
            if (this.is(selector)) {
                return $selection.add(this);
            }
            return $selection;
        },

        toggleData: function(key, value) {
            // Toggle data boolean value with the same semantics than toggleClass()
            if ($.type(value) === "boolean") {
                return this.data(key, value);
            }
            var currentValue = this.data(key);
            if (currentValue && $.type(currentValue) !== "boolean") {
                console.warn("Toggling to false a data key '" + key + "' which " +
                    " already contains a non-boolean value: " + currentValue);
            }
            return this.data(key, ! currentValue);
        },

        dataIn: function(keyPath) {
            var keys = keyPath.split("."),
                rootKey = keys.shift(),
                data = this.data(rootKey);
            $.each(keys, function(i, key) {
                if (data) {
                    data = data[key];
                }
                return data ? true : false;
            });
            return data;
        },

        // Toggle disabled status of buttons, inputs and anchors
        // Inspired from: http://stackoverflow.com/a/16788240
        disable: function(disable) {
            var flagAsDisabled = disable;
            if ($.type(flagAsDisabled) !== "boolean") {
                flagAsDisabled = true;
            }
            return this.each(function() {
                var $this = $(this);
                if($this.is("input, button, select, optgroup")) {
                    this.disabled = flagAsDisabled;
                } else if ($this.is("a")) {
                    // In Bootstrap links of class ".btn.btn-link" handle the
                    // disabled state as expected with ".disabled".
                    $this.toggleClass("disabled", flagAsDisabled);
                    if (! $this.hasClass("btn")) {
                        // If the <a> doesn't have the Bootstrap class ".btn" we
                        // have to remove the href to prevent clicking.
                        var hrefOffAttr = "href-disabled",
                            hrefAttr = "href",
                            offStyle = "text-decoration: none !important;";
                        if (flagAsDisabled) {
                            $this
                                .attr(hrefOffAttr, $this.attr(hrefAttr))
                                .removeAttr(hrefAttr)
                                .attr("style", offStyle);
                        } else {
                            $this
                                .attr(hrefAttr, $this.attr(hrefOffAttr))
                                .removeAttr(hrefOffAttr)
                                .removeAttr("style");
                        }
                    }
                } else {
                    $this.toggleClass("disabled", flagAsDisabled);
                }
            });
        },

        enable: function(enable) {
            return this.disable(enable === false);
        },

        disabledRowCls: "ss-disabled-row",

        disableRow: function(disable, optionsArg) {
            // Fades the table row to 0.3 opacity and disables all form inputs inside,
            // except the ones matching 'options.exceptElemSel' (useful to not disable
            // the "Remove row" button).
            // A 'options.disableReason' string can be passed to be displayed as tooltip
            // on the whole row when it is disabled.
            var options = optionsArg || {},
                flagAsDisabled = disable,
                $selectedRows = this.filter("tr");
            if ($.type(flagAsDisabled) !== "boolean") {
                flagAsDisabled = true;
            }
            $selectedRows.each(function () {
                var $this = $(this),
                    callbackAfterRowStateChange = $this.data("callbackAfterRowStateChange");
                $this
                    .toggleClass(this.disabledRowCls, flagAsDisabled)
                    .fadeTo(200, flagAsDisabled ? 0.3 : 1)
                    .attr("title", flagAsDisabled ? options.disableReason : "") // Simple tooltip
                    .find("input, button, select, a")
                        .not(options.exceptElemSel)
                        .not(options.exceptElem)
                        .disable(flagAsDisabled);
                    if (callbackAfterRowStateChange) {
                        callbackAfterRowStateChange.call($this, !flagAsDisabled);
                    }
            });
            return this;
        },

        isDisabledRow: function() {
            return this.hasClass(this.disabledRowCls);
        },

        getDisabledRows: function() {
            return this.filterOfClass(this.disabledRowCls);
        },

        enableRow: function(enable, options) {
            return this.disableRow((enable === false), options);
        },

        isEnabledRow: function() {
            return ! this.hasClass(this.disabledRowCls);
        },

        slidedUpRowCls: "ss-slided-up-row",
        tmpDivToSlidedUpRowCls: "ss-tmp-div-to-slide-up-row",

        // NOTE: <tr> tags cannot be used with jQuery's 'slideUp()' and 'slideDown()'
        //       functions.
        //       Source: http://stackoverflow.com/a/920480
        //       The used solution wraps the content in a <div> tag and applies the
        //       functions on that.
        //       Inspired from: http://stackoverflow.com/a/3410943

        slideUpRow: function(duration) {
            var $row = this;
            $row
                .filter("tr")
                .not(this.slidedUpRowCls.asSel())
                .find('td')
                .wrapInner("<div class='" + $row.tmpDivToSlidedUpRowCls + "'style='display: block;' />")
                .parent()
                .find('td > div')
                .slideUp(duration, function(){
                    var $tmpInnerDiv = $(this);
                    $tmpInnerDiv
                        .closest("tr")
                            .addClass($row.slidedUpRowCls);
                });
            return this;
        },

        slideDownRow: function(duration) {
            this
                .filter("tr." + this.slidedUpRowCls)
                .removeClass(this.slidedUpRowCls)
                .find("td > div." + this.tmpDivToSlidedUpRowCls)
                .slideDown(duration, function(){
                    var $tmpInnerDiv = $(this);
                    $tmpInnerDiv.replaceWith($tmpInnerDiv.contents());
                });
            return this;
        },

        onRowStateChange: function(callbackAfterRowStateChange) {
            this.each(function() {
                var $this = $(this),
                    callbacks = $this.data("callbackAfterRowStateChange") || [];
                callbacks.push(callbackAfterRowStateChange);
                $this.data("callbackAfterRowStateChange", callbacks);
            });
            return this;
        },

        updateAttr: function(attrName, strModifierFn) {
            this
                .filter("[" + attrName + "]")
                    .each(function(){
                        var $this = $(this),
                            newAttrValue = strModifierFn.call(this, $this.attr(attrName));
                        $this.attr(attrName, newAttrValue);
                    });
            return this;
        },

        fade: function(shouldFadeIn, duration, easing, complete) {
            // If shouldFadeIn is true it fades the element in, i.e. shows it.
            // If shouldFadeIn is false it fades the element out, i.e. hides it.
            // If shouldFadeIn is not a boolean it toggles the fades state.
            // Like .toggle(), but with no arguments, .toggle() shows or hides the
            // element without fading.
            if (shouldFadeIn === true) {
                this.fadeIn(duration, easing, complete);
            } else if (shouldFadeIn === false) {
                this.fadeOut(duration, easing, complete);
            } else {
                this.fadeToggle(duration, easing, complete);
            }
            return this;
        },

        // Inspired from: http://stackoverflow.com/a/1186309
        // If more complex form serialization is needed, see https://github.com/macek/jquery-serialize-object
        serializeObject: function () {
            var o = {};
            var a = this.serializeArray();
            $.each(a, function() {
                $$.util.object.setOrPush(o, this.name, this.value);
            });
            return o;
        },

        triggerDelayed: function(delayInMillis, eventName) {
            // Trigger an event after 'delayInMillis'. If this function is called many times
            // it ensures that the event is triggered *only once*, 'delayInMillis' millis
            // after the last call.
            var $this = this,
                triggerIDKey = "triggerTimeoutFor" + eventName,
                deadlineKey  = "deadlineFor" + eventName;
            this.data(deadlineKey, $.now() + delayInMillis);
            if(! this.data(triggerIDKey)) {
                this.data(triggerIDKey, setTimeout(function(){
                    $this.removeData(triggerIDKey);
                    if ($.now() < $this.data(deadlineKey)) {
                        $this.triggerDelayed(delayInMillis, eventName);
                    } else {
                        $this.trigger(eventName);
                    }
                }, delayInMillis));
            }
            return this;
        },

        hoverDelayed: function(handlerInOrInOutArg, handlerOutArg, delayInMillis, selector) {
            // If 'delayInMillis' is an int, it's applied for both 'handlerIn' and 'handlerOut' events.
            // To define different delays, provide an object like: {enter: 100, leave: 0}
            // Add {alwaysTriggerHandlerOut: false} to trigger the handlerOut only if handlerIn was called.
            // Selector behaves like in jQuery's .on() function.
            var $this = this,
                eventParams = {
                    mouseenter: {
                        delay:          $.isPlainObject(delayInMillis) ? delayInMillis.enter : delayInMillis,
                        timeoutIDKey:   "timeoutMouseEnterIDKeyForHover",
                        handler:        handlerInOrInOutArg,
                        handlerWasTriggeredDataKey: "handlerInWasTriggered"
                    },
                    mouseleave: {
                        delay:          $.isPlainObject(delayInMillis) ? delayInMillis.leave : delayInMillis,
                        timeoutIDKey:   "timeoutMouseLeaveIDKeyForHover",
                        handler:        handlerOutArg || handlerInOrInOutArg,
                        alwaysTrigger:  $.isPlainObject(delayInMillis) ? delayInMillis.alwaysTriggerHandlerOut : true,
                        handlerWasTriggeredDataKey: "handlerOutWasTriggered"
                    }
                };
            function scheduleHandlerForEvent(e, params) {
                var that = this;
                 if ( $this.data(params.timeoutIDKey) === undefined ) {
                        $this.data(
                            params.timeoutIDKey,
                            setTimeout(
                                function(){
                                    params.handler.call(that, e);
                                    $this
                                        .removeData(params.timeoutIDKey)
                                        .data(params.handlerWasTriggeredDataKey, true);
                                },
                                params.delay
                            )
                        );
                    }
            }

            function stopHandlerForEvent(e, params) {
                if ( $this.data(params.timeoutIDKey) !== undefined ) {
                    clearTimeout($this.data(params.timeoutIDKey));
                    $this.removeData(params.timeoutIDKey);
                }
                $this.removeData(params.handlerWasTriggeredDataKey);
            }

            return this
                    .on("mouseenter",
                        selector,
                        function(e) {
                            scheduleHandlerForEvent.call(this, e, eventParams.mouseenter);
                            stopHandlerForEvent.call(    this, e, eventParams.mouseleave);
                        })
                    .on("mouseleave",
                        selector,
                        function(e) {
                            if ( $this.data(eventParams.mouseenter.handlerWasTriggeredDataKey) ||
                                eventParams.mouseleave.alwaysTrigger ) {
                                scheduleHandlerForEvent.call(this, e, eventParams.mouseleave);
                            }
                            stopHandlerForEvent.call(        this, e, eventParams.mouseenter);
                        });
        },

        enableBufferedTextInputChangeEvent: function() {
            // The event is bound to the 'body' so that it's enabled for all relevant elements, present or future.
            var $staticParent = this.closest("body"),
                targetElemSel = "input[type=text], input[type=password], input[type=number], textarea",
                enabledKey = "isBufferedTextInputChangeEventEnabled";
            if (! $staticParent.data(enabledKey)) {
                $staticParent.on("input", targetElemSel, function() {
                    $(this).triggerDelayed(300, "bufferedtextinputchange");
                });
                this.data(enabledKey, true);
            }
            return this;
        },

        bufferedtextinputchange: function (callback) {
            // This event triggers max once every 300 millis (i.e. not on every keystroke).
            // See also onTextInputChange();
            this
                .enableBufferedTextInputChangeEvent()
                .on("bufferedtextinputchange", callback);
            return this;
        },

        onTextInputChange: function(callback) {
            // This event triggers at every input change (e.g. every keystroke).
            // See also bufferedtextinputchange(callback);
            var $textInputFields = $(this).findIncludingItself("input[type=text], input[type=password], input[type=number], textarea");
            if ($textInputFields.foundNothing()) {
                return this;
            }
            // Inspired from: http://stackoverflow.com/a/6458946
            $textInputFields
                .on("input", callback);
            return this;
        },

        offTextInputChange: function(callback) {
            var $textInputFields = $(this).findIncludingItself("input[type=text], input[type=password], input[type=number], textarea");
            if ($textInputFields.foundNothing()) {
                return this;
            }
            $textInputFields.off('input', callback);
            return this;
        },

        enableEnterKeyPressEvent: function() {
            var $this = this,
                enabledKey = "isEnterKeyPressEventEnabled";
            if (! this.data(enabledKey)) {
                this.keypress(function (e) {
                    if (e.keyCode === 13) {
                        $this.trigger("enterkeypress");
                    }
                });
                this.data(enabledKey, true);
            }
            return this;
        },

        enterkeypress: function (callback) {
            this
                .enableEnterKeyPressEvent()
                .on("enterkeypress", callback);
            return this;
        },

        onAltEnterPress: function (callback) {
            // TODO: Rename it in jQuery style (i.e. 'altenterpress') and create a custom event?
            if ($.isFunction(callback)) {
                $(this).keypress(function (e) {
                    if (e.altKey && e.keyCode === 13) {
                        e.preventDefault();
                        callback.call(this);
                      }
                });
            }
            return this;
        },

        clickWhenEnabled: function (callback) {
            // <a> tags do not honor the 'disabled' property. Therefore, Bootstrap
            // offers the helper class 'disabled', to make them look like so.
            // However they are still clickable (the .click() callback will still
            // be called), and therefor we have to disable it manually here.
            this.each(function () {
                var $this = $(this);
                $this.click( function (event) {
                    var $thisIntern = $(this),
                        reason      = $thisIntern.attr("disabled-reason");
                    if (! $thisIntern.hasClass("ss-action-disabled") && $.isFunction(callback)) {
                        callback.call($thisIntern, event);
                    } else if (reason) {
                        $$.alert.showWarning(reason);
                    }
                });
            });
            return this;
        },

        hasFocus: function() {
            return this.is(":focus");
        },

        formHiddenFieldCls: "ss-form-hidden-field-added-with-addFormHiddenField-fn",

        addFormHiddenField: function (fieldName, fieldValue) {
            this
                .filter("form")
                    .each(function () {
                        var $form = $(this);
                        // Clean up hidden field with the same name before adding it.
                        $form.children("input:hidden[name='" + fieldName + "']").remove();
                        $("<input>")
                            .addClass($form.formHiddenFieldCls)
                            .attr("type", "hidden")
                            .attr("name", fieldName)
                            .attr("value", fieldValue)
                            .appendTo($form);
                    });
            return this;
        },

        cleanFormHiddenFields: function () {
            // Only the ones added via the function .addFormHiddenField()
            this
                .find("input." + this.formHiddenFieldCls)
                    .remove();
            return this;
        },

        setFormField: function (fieldName, fieldValue) {
            $(this).filter("form").each(function () {
                var $form = $(this);
                // If the field doesn't exists already in the form, this is a no-op.
                $form.find("input[name=" + fieldName + "]").val(fieldValue);
            });
            return this;
        },

        formHasErrorsCls: "ss-form-has-errors",
        formFieldToValidateCls: "ss-input-needs-validation",
        requiredFormFieldCls: "ss-required-input",
        formFieldHasRequirementsCls: "ss-input-has-requirements",

        setRequiredFormInput: function(isRequired) {
            this.toggleClass(this.requiredFormFieldCls, isRequired);
            return this;
        },

        isRequiredFormInput: function() {
            return this.hasClass(this.requiredFormFieldCls);
        },

        onFormFieldValidation: function(callback){
            // This callback will be called after each validation of the input field,
            // receiving as the only argument a boolean to indicate if the field value
            // is valid or not.
            if (! $.isFunction(callback)) {
                throw "Callback is not a function.";
            }
            this.each(function(){
                    var $this = $(this),
                        callbacks = $this.data("onFormFieldValidationCallback");
                    if (! callbacks) {
                        $this.data("onFormFieldValidationCallback", [callback]);
                    } else {
                        callbacks.push(callback);
                    }
                });
            return this;
        },

        onFormFieldValidationStateChange: function(callback){
            // Like 'onFormFieldValidation()' but called only when the state changes
            // from valid ('true') to invalid ('false') or viceversa.
            if (! $.isFunction(callback)) {
                throw "Callback is not a function.";
            }
            this.each(function(){
                    var $this = $(this),
                        callbacks = $this.data("onFormFieldValidationStateChangeCallback");
                    if (! callbacks) {
                        $this.data("onFormFieldValidationStateChangeCallback", [callback]);
                    } else {
                        callbacks.push(callback);
                    }
                });
            return this;
        },

        addCustomFormFieldRequirement: function(testCallback, errorHelpHint){
            // testCallback will receive the 'fieldValue' string as the first argument
            // and the jQuery input element as the second.
            // It should return a boolean: 'true' if the value passes the requirement
            // and 'false' if not. In this second case, the 'errorHelpHint' will be
            // displayed below the input field.
            var compiledRequirements = this.data("input-compiled-requirements"),
                requirement = {
                    test: testCallback,
                    helpHint: {
                        whenFalse: errorHelpHint
                    },
                    status: {
                        whenTrue:   "success",
                        whenFalse:  "error"
                    }
                };
            if (! compiledRequirements) {
                this.data("input-compiled-requirements", [requirement]);
            } else {
                compiledRequirements.push(requirement);
            }
            return this;
        },

        addValidationTrigger: function (eventName, $elem) {
            // If $elem is undefined, the trigger for eventName will be bound to
            // the $this element.
            var that = this,
                $eventTarget = $elem || this;
            $eventTarget.on(eventName, function() {
                $(that).validateFormInput();
            });
        },

        enableDisplayOfValidationHelpHint: function() {
            this.data("displayValidationHelpHint", true);
            return this;
        },

        enableLiveInputValidation: function() {
            this
                .findIncludingItself(this.formFieldToValidateCls.asSel())
                .bufferedtextinputchange(function() {
                    $(this).validateFormInput();
                })
                .enableDisplayOfValidationHelpHint();
            return this;
        },

        validateForm: function() {
            this
                .closest("form")
                    .findOfClass(this.formFieldToValidateCls)
                        .validateFormInput();
            return this;
        },

        isValidForm: function() {
            var $form = this.closest("form");
            return $form
                        .validateForm()
                        .hasClass(this.formHasErrorsCls)
                        .not();
        },

        isValidFormInput: function() {
            return this
                        .filterOfClass(this.formFieldToValidateCls)
                        .first()
                        .validateFormInput()
                        .data("isValid") ? true : false;
        },

        isFormInputValidationState: function(state) {
            return this
                        .filterOfClass(this.formFieldToValidateCls)
                        .first()
                        .data("validationState") === state;
        },

        validateFormInput: function() {

            function getRequirements($fieldToValidate){
                if ($fieldToValidate.data("already-compiled-input-requirements")) {
                    return $fieldToValidate.data("input-compiled-requirements");
                }
                // lazily compile regexp only once
                var compiledRequirements = $fieldToValidate.data("input-compiled-requirements") || [],
                    requirements = $fieldToValidate.dataIn("validation.requirements") || [];
                if (requirements.length === 0 && $fieldToValidate.hasClass($fieldToValidate.requiredFormFieldCls)) {
                    requirements = [{"pattern": "\\w+"}];
                }
                $.each(requirements, function(i, requirement){
                    compiledRequirements.push($.extend({}, requirement, {
                        re: new RegExp(requirement.pattern),
                        test: function(fieldValue) {
                            return this.re.test(fieldValue);
                        }
                    }));
                });
                $fieldToValidate
                    .data("input-compiled-requirements", compiledRequirements)
                    .data("already-compiled-input-requirements", true);

                return compiledRequirements;
            }

            this
                .filterOfClass(this.formFieldToValidateCls)
                    .each(function() {
                        var $fieldToValidate    = $(this),
                            fieldValue          = $fieldToValidate.val() || "",
                            requirements        = getRequirements($fieldToValidate),
                            isRequired          = $fieldToValidate.isRequiredFormInput(),
                            validationState     = "success",
                            validationHelpHint;
                        if (fieldValue || isRequired) {
                            $.each(requirements, function(i, requirement){
                                validationState = requirement.status.whenTrue || "success";
                                validationHelpHint = requirement.helpHint.whenTrue;
                                var requirementTestResult = requirement.test(fieldValue, $fieldToValidate);
                                if ($.type(requirementTestResult) === "string") {
                                    // NOTE: In that case requirementTestResult should be either "error", "warning" or "success"
                                    validationState = requirementTestResult;
                                } else if (requirementTestResult === false) {
                                    validationState = requirement.status.whenFalse || "error";
                                    validationHelpHint = requirement.helpHint.whenFalse;
                                }
                                if (validationState !== "success") {
                                    return false; // break 'each' loop
                                }
                            });
                        } else {
                            validationState = $fieldToValidate.dataIn("validation.stateWhenEmpty") || validationState;
                        }
                        if (validationState === "success" && ! fieldValue) {
                            $fieldToValidate.clearFormInputValidationState();
                        } else {
                            $fieldToValidate.setFormInputValidationState(validationState, validationHelpHint);
                        }
                    });
            return this;
        },

        getGenericFormAlertMsg: function(category) {
            // 'category' is 'error' or 'success'
            return this
                    .closest("form")
                    .dataIn("generic-form-messages." + category);
        },

        setFormInputValidationState: function (stateArg, customHelpHint) {
            // 'stateArg' can be a boolean or a string: 'success', 'warning' or 'error'.
            // If it is a boolean 'true' means 'success' and 'false' means 'error'.
            var stateArgType = $.type(stateArg),
                stateIcons = {
                    success:    "glyphicon-ok",
                    validating: "glyphicon-refresh",
                    warning:    "glyphicon-alert",
                    error:      "glyphicon-remove"
                },
                allowedStateStrings = Object.keys(stateIcons),
                allIconsClasses = $$.util.object.valsString(stateIcons, " "),
                state,
                isValid;

            if (stateArgType === "boolean") {
                state = stateArg ? "success" : "error";
            } else if (stateArgType === "string") {
                if (allowedStateStrings.contains(stateArg)) {
                    state = stateArg;
                } else {
                    console.warn("Validation state '" + stateArg + "' is not allowed. Using 'warning' instead.");
                    state = "warning";
                }
            }

            isValid = (state === "success");

            this
                .filterOfClass(this.formFieldToValidateCls)
                    .each(function() {
                        var $this = $(this),
                            $formGroup = $this.closest(".form-group"),
                            $elemsToFlag = $this.closest(".ss-section")
                                                .find("> .ss-section-activator")
                                                    .add($formGroup),
                            isNewState = ($this.data("isNewState") !== state),
                            callback = $this.data("onFormFieldValidationCallback"),
                            callbackOnStateChange = $this.data("onFormFieldValidationStateChangeCallback"),
                            genericHelpHints = $this.dataIn("validation.genericHelpHints") || {},
                            validationHelpHint = customHelpHint || genericHelpHints[state],
                            displayHelpHint = validationHelpHint && $this.data("displayValidationHelpHint") || false; // A real boolean
                        if ($formGroup.foundNothing()) {
                            throw "No .form-group element could be found from jQuery selection.";
                        }
                        $this.data("validationState", state);
                        if (state !== "warning") {
                            // Warning doesn't change the validation state of the field.
                            // If it was valid before the warning it stays valid, and it
                            // it was not valid before the warning it stays not valid.
                            $this.data("isValid", isValid);
                        }
                        $elemsToFlag
                            .removeClass("has-success has-warning has-error")
                            .addClass(state === "validating" ? "has-warning" : "has-" + state);
                        $formGroup
                            .find(".ss-validation-help-hint")
                                .stop(true, true)
                                .html(validationHelpHint)
                                .toggleClass("hidden", ! displayHelpHint)
                                .end()
                            .find(".form-control-feedback")
                                .removeClass(allIconsClasses + " hidden")
                                .addClass(stateIcons[state])
                                .end()
                            .closest("tr")
                                .toggleClass("danger", state === "error")
                                .toggleClass("warning", state === "warning");
                        if (state === "success" && displayHelpHint) {
                            // Fade success help hint after 5 secs
                            $formGroup
                                .find(".ss-validation-help-hint")
                                    .captureInlineStyle()
                                    .delay(5000)
                                    .slideUp(400, function() {
                                        $(this)
                                            .addClass("hidden")
                                            .restoreInlineStyle();
                                    });
                        }
                        var hasErrors = $this.closest("form").find(".has-error").foundAny();
                        $this
                            .closest("form")
                                // Flag the form as having errors if needed
                                .toggleClass($this.formHasErrorsCls, hasErrors)
                                // Enable or disable the submit button accordingly
                                .find("button[type=submit]")
                                    .disable(hasErrors);
                        if (callback) {
                            callback.call($this, isValid, state);
                        }
                        if (isNewState && callbackOnStateChange) {
                            callbackOnStateChange.call($this, isValid, state);
                        }
                    });
            return this;

        },

        toggleFormInputValidationState: function (state, customErrorHelpHint) {
            if ($.type(state) === "boolean") {
                this.setFormInputValidationState(state, customErrorHelpHint);
            } else if ($.type(state) === "undefined") {
                this.setFormInputValidationState(! this.data("isValid"), customErrorHelpHint);
            } else {
                throw "State must be a boolean or left undefined.";
            }
            return this;
        },

        clearFormInputValidationState: function () {
            var $this = $(this),
                $formGroup = $this.closest(".form-group"),
                callback = $this.data("onFormFieldValidationCallback"),
                callbackOnStateChange = $this.data("onFormFieldValidationStateChangeCallback");
            if ($formGroup.foundNothing()) {
                throw "No .form-group element can be found from jQuery selection.";
            }
            $this
                .removeData("validationState")
                .removeData("isValid");

            $formGroup
                .removeClass("has-success has-warning has-error")
                .find(".ss-validation-help-hint")
                    .addClass("hidden")
                    .end()
                .find(".form-control-feedback")
                    .addClass("hidden")
                    .end()
                .closest("tr")
                    .removeClass("warning danger");
            if (callback) {
                callback.call($this, undefined, undefined);
            }
            if (callbackOnStateChange) {
                callbackOnStateChange.call($this, undefined, undefined);
            }
            // Do the same with the submit button, if no .has-error in form
            var hasErrors = $this.closest("form").find(".has-error").foundAny();
            $this
                .closest("form")
                .find("button[type=submit]")
                .disable(hasErrors);
            return this;
        },

        getSelectedOptionText: function() {
            // For <select> tags, val() returns the value only if it was clicked
            // This returns always the displayed text.
            return this.find("option:selected").text();
        },

        focusFirstInput: function() {
            var $firstElem = $(this).find("input[type=text], textarea").first();
            if ($firstElem.foundNothing()) {
                return this;
            }
            var strLength= $firstElem.val().length * 2; // x 2 to ensure cursor always ends up at the end
            $firstElem.focus();
            $firstElem[0].setSelectionRange(strLength, strLength);
            return this;
        },

        blankInputs: function() {
            var $inputs = this.findIncludingItself("input[type=text], textarea");
            if ($inputs.foundNothing()) {
                return this;
            }
            return $inputs
                .filter(function(){
                    return $(this).val() === "";
                });
        },

        askConfirmation: function (callbackOnOKButtonPress) {
            var $modalDialog = $(this).filter("div.modal");
            if ($modalDialog.foundNothing()) {
                throw "No modal dialog in jQuery selection.";
            }
            if ($modalDialog.length !== 1) {
                throw "More than one modal dialog in jQuery selection. Please select only one.";
            }
            // Update the callback called by the event hanlder on every askConfirmation event
            $modalDialog.data("callbackOnOKButtonPress", callbackOnOKButtonPress);
            if (! $modalDialog.data("eventHandlerOnOKButtonPressSetUp")) {
                // Add the 'on' event handler only once, not on every askConfirmation event
                $modalDialog.find(".ss-ok-btn").on("click", function() {
                    $modalDialog.data("callbackOnOKButtonPress").call(this);
                });
                $modalDialog.data("eventHandlerOnOKButtonPressSetUp", true);
            }
            $modalDialog.modal("show");
            return this;
        },

        scheduleAlertDismiss: function(millis){
            var $alertElem = $(this).filter("div[role=alert]:not(.hidden)");
            $alertElem.data("dismissTimeout", setTimeout(function(){
                $alertElem.slideUp("slow", function() {
                    $alertElem.remove();
                });
            }, millis || 5000));
            if (! $alertElem.data("mouseHandlersAlreadySetup")) {
                // Schedule mouse handlers only once
                $alertElem.mouseenter(function() {
                    // Cancelling alert dismiss
                    clearTimeout($alertElem.data("dismissTimeout"));
                });
                $alertElem.mouseleave(function() {
                    // Scheduling new dismiss
                    $alertElem.scheduleAlertDismiss(millis);
                });
                $alertElem.data("mouseHandlersAlreadySetup", true);
            }
            return this;
        },

        categoryStyle: {
            transparent:{
                backgroundColor:    "rgba(255, 255, 255,  .0)"
            },
            // From Bootstrap classes
            info:{
                color:              "rgba( 36,  82, 105, 1.0)", // .text-info:hover
                backgroundColor:    "rgba(175, 217, 238,  .6)"  // .bg-info:hover
            },
            success:{
                color:              "rgba( 43,  84,  44, 1.0)", // .text-success:hover
                backgroundColor:    "rgba(193, 226, 179,  .6)"  // .bg-success:hover
            },
            warning:{
                color:              "rgba(102,  81,  44, 1.0)", // .text-warning:hover
                backgroundColor:    "rgba(247, 236, 181,  .6)"  // .bg-warning:hover
            },
            danger:{
                color:              "rgba(132,  53,  52, 1.0)", // .text-danger:hover
                backgroundColor:    "rgba(228, 185, 185,  .6)"  // .bg-danger:hover
            }
        },

        captureInlineStyle: function() {
            return this.data("inlineStyle", this.attr("style"));
        },

        restoreInlineStyle: function($elemToRestoreFrom) {
            var inlineStyle = ($elemToRestoreFrom || this).data("inlineStyle");
            if (inlineStyle) {
                this.attr("style", inlineStyle);
            } else {
                this.removeAttr("style");
            }
            return this;
        },

        realOpacity: function() {
            var opacity = this.css("opacity");
            this
                .parents()
                    .each(function() {
                        opacity *= $(this).css("opacity");
                    });
            return opacity;
        },

        captureInlineOpacity: function() {
            var inlineStyle = this.attr("style");
            if (/\bopacity\b/.test(inlineStyle)) {
                this.data("inlineOpacity", inlineStyle.match(/.*opacity\s*:([^;]*)*;/)[1].trim());
            } else {
                this.removeData("inlineOpacity");
            }
            return this;
        },

        getCapturedInlineOpacity: function() {
            return this.data("inlineOpacity");
        },

        restoreInlineOpacity: function($elemToRestoreFrom) {
            var inlineOpacity = ($elemToRestoreFrom || this).getCapturedInlineOpacity();
            if (inlineOpacity) {
                this.css("opacity", inlineOpacity);
            } else {
                this.css("opacity", "");
            }
            return this;
        },

        flash: function(category) {
            var $elemToFlash = this,
                originalStyle = {
                    color:              $elemToFlash.css("color"),
                    backgroundColor:    $elemToFlash.css("backgroundColor")
                };
            return $elemToFlash
                    .captureInlineStyle()
                    .animate(this.categoryStyle[category || "info"], 200)
                    .animate(originalStyle, 2000, function() {
                        $elemToFlash.restoreInlineStyle();
                     });
        },

        flagAsLoading: function() {
            return this.html("<span class=\"glyphicon glyphicon-refresh\"></span>");
        },

        updateContent: function(contentGetterFn, contentSetterFn, newContent, todoIfUpdatedArg, callbackIfUpdated) {
            // NOTE: Refer to 'updateWith()', 'updateHTML()' and 'updateText()' below.
            // todoIfUpdatedArg is an object with '{flash: true, flashClosestSel: "tr", flashCategory: "danger"}'
            // to indicate what element to flash (and how) if 'this' was actually changed.
            // In that case, 'callbackIfUpdated' is also called, and receives the new content as argument.
            // In both 'todoIfUpdatedArg' and 'callbackIfUpdated' have no effect if the 'newContent' is the
            // same as the original content.
            var $originalElem           = this,
                $newElem                = this,
                originalContent         = contentGetterFn.call($originalElem),
                shouldUpdateContent     = (originalContent != newContent),
                todoIfUpdatedOptions    = $.isPlainObject(todoIfUpdatedArg) ? todoIfUpdatedArg : {},
                todoIfUpdatedDefaults   = {flash: true, flashCategory: "info", flashDuration: 400},
                todoIfUpdated           = $.extend(todoIfUpdatedDefaults, todoIfUpdatedOptions),
                fadeDuration            = todoIfUpdated.flashDuration / 2,
                shouldVisuallyHighlightUpdate = true;
            if (newContent instanceof jQuery) {
                if (! (originalContent instanceof jQuery)) {
                    throw "'contentGetterFn' should return a jQuery element if 'newContent' is a jQuery element.";
                }
                $newElem = newContent
                                .bsEnableDynamicElements()
                                .enableEnterKeyPressEvent()
                                .enableLiveInputValidation();
                // enable dynamic Bootstrap elements before comparing outerHTML, since attributes might change.
                shouldUpdateContent = (newContent[0].outerHTML != originalContent[0].outerHTML);
                shouldVisuallyHighlightUpdate = todoIfUpdated.flash &&
                                                ((newContent.text() != originalContent.text()) ||
                                                 (newContent.val() != originalContent.val()));
                if (shouldVisuallyHighlightUpdate) {
                    $newElem.css("opacity", 0);
                }
            }
            if (shouldUpdateContent) {
                if (shouldVisuallyHighlightUpdate) {
                    var originalOpacity = $originalElem.css("opacity");
                    $originalElem
                        .captureInlineStyle()
                        .animate({opacity: 0}, fadeDuration, function() {
                            contentSetterFn.call($originalElem, newContent);
                            $newElem
                                .animate({opacity: originalOpacity}, fadeDuration, function (){
                                    $newElem.restoreInlineStyle($originalElem);
                                    var closestSel = todoIfUpdated.flashClosestSel,
                                        $elemToFlash = closestSel ? $newElem.closest(closestSel) : $newElem;
                                    $elemToFlash.flash(todoIfUpdated.flashCategory);
                                    if ($.isFunction(callbackIfUpdated)) {
                                        callbackIfUpdated.call($newElem, newContent, originalContent);
                                    }
                                });
                        });
                } else {
                    // Update without visually highlighting the change
                    contentSetterFn.call($originalElem, newContent);
                    if ($.isFunction(callbackIfUpdated)) {
                        callbackIfUpdated.call($newElem, newContent, originalContent);
                    }
                }
            }
            return this;
        },

        updateWith: function($newElem, todoIfUpdated, callbackIfUpdated) {
            return this.updateContent(Function.identity, this.replaceWith, $newElem, todoIfUpdated, callbackIfUpdated);
        },

        updateText: function(newText, todoIfUpdated, callbackIfUpdated) {
            return this.updateContent(this.text, this.text, newText, todoIfUpdated, callbackIfUpdated);
        },

        updateHTML: function(newHTML, todoIfUpdated, callbackIfUpdated) {
            return this.updateContent(this.html, this.html, newHTML, todoIfUpdated, callbackIfUpdated);
        },

        replaceHTMLContentBySelector: function(selectorsAndContentsMap) {
            if ( $.type(selectorsAndContentsMap) !== "object" ) {
                console.warn("selectorsAndContentsMap must be a map of selectors to jQuery elements or HTML strings.");
                return this;
            }
            var $this = this;
            $.each(selectorsAndContentsMap, function(sel, newHTMLString) {
                $this
                    .find(sel)
                        .html(newHTMLString)
                        .bsEnableDynamicElements();
            });
            return $this;
        },

        // Bottom shadow

        _onScrollCallbackToAdjustIFrameBottomShadowOpacity: function (iFrameHeight, $bottomShadow){
            var scrollHeight = this.document.body.scrollHeight,
                scrollY = this.scrollY,
                bottomShadowOpacity = 4 * (1 - (iFrameHeight / (scrollHeight - scrollY))); // NOTE: Empirical formula.
            if ( bottomShadowOpacity >= 0 && bottomShadowOpacity <= 1 ) {
                $bottomShadow
                    .css("opacity", bottomShadowOpacity);
            }
        },

        enableBottomShadowAdjustOnScroll: function() {
            $(this[0].contentWindow).scroll(this._onScrollCallbackToAdjustIFrameBottomShadowOpacity.partial(
                this
                    .outerHeight(),
                this
                    .closest(".ss-iframe-container")
                        .find(".ss-shadow-bottom")
                ));
            return this;
        },

        isOnWindow: function() {
            // Returns true if the top of the element is in the window (i.e. the
            // user can see its top part, if the elem is not invisible).
            var $w = $(window),
                windowTop = $w.scrollTop(),
                windowHeight = $w.height(),
                elemTop = this.offset().top;
            return  windowTop   < elemTop &&
                    elemTop     < (windowTop + windowHeight);
        },

        reveal: function() {
            // Scroll to the element, if it's not currently on the window port.
            var topOffset = 80; // Due to the menu bar
            if (! this.isOnWindow()) {
                $("html, body").animate({
                    scrollTop: Math.max(0, this.offset().top - topOffset)
                }, 800);
            }
            return this;
        },

        font: function() {
            // NOTE: this.css("font") is not specified to work as expected.
            //       Safari and Chrome for example return the whole 'font' string as expected, but not Firefox.
            //       See http://stackoverflow.com/a/27523553
            return this.css("font-weight") + " " + this.css("font-size") + " " + this.css("font-family");
        },

        renderedTextWidth: function() {
            // Returns the length of the string for the first matched
            // element which is a text-only node.
            var $textNodeElem = this
                                    .filters(this.predicates.isNodeWithOnlyText)
                                        .first();
            if ( $textNodeElem.foundOne() ) {
                return $textNodeElem.text().width($textNodeElem.font());
            }
            return undefined;
        },

        enableTooltipOnEllipsedTexts: function() {
            var showingFullTextCls = "ss-showing-full-text-on-hover",
                paddingTopBottomPx = 2,
                paddingLeftRightPx = 6,
                borderPx  = 1;
            this
                .find("*:not(.sr-only)")
                    .filters(
                        this.predicates.isNodeWithOverflowText
                    )
                        .each(function(){
                            var $this = $(this),
                                $thisFullForHover = $("<span class='ss-full-text-for-hover'></span>")
                                                .text($this.text())
                                                .css({
                                                    position:       "absolute",
                                                    pointerEvents:  "none",
                                                    border:         borderPx + "px solid #777",
                                                    background:     "rgba(255,255,255,0.9)",
                                                    font:           $this.font(),
                                                    padding:        paddingTopBottomPx + "px " + paddingLeftRightPx + "px"
                                                });
                            $this
                                .hoverDelayed(
                                    function(){
                                        var pos = $this.offset();
                                        $this.addClass(showingFullTextCls);
                                        $thisFullForHover
                                            .css({
                                                top:        pos.top  - paddingTopBottomPx - borderPx,
                                                left:       pos.left - paddingLeftRightPx - borderPx,
                                                opacity:    $this.realOpacity()
                                            })
                                            .appendTo("body");
                                    },
                                    function() {
                                        $this.removeClass(showingFullTextCls);
                                        $thisFullForHover.remove();
                                    },
                                    200);
                        });
            return this;
        },

        // jQuery extensions related to Bootstrap components are prefixed by 'bs'

        bsEnableDropdownToggle: function(enable) {
            if (enable === false) {
                this.findClosest(".dropdown-toggle").removeAttr("data-toggle");
            } else {
                this.findClosest(".dropdown-toggle").attr("data-toggle", "dropdown");
            }
            return this;
        },

        bsFlickDropdownToggle: function() {
            // Disables for a short moment the functionality of the .dropdown-toggle
            var $this = this;
            $this.bsEnableDropdownToggle(false);
            setTimeout(function(){
                $this.bsEnableDropdownToggle(true);
            }, 400);
            return $this;
        },

        bsOpenDropdown: function() {
            var $closedDropdown = this.closest(".btn-group:not(.open), .dropdown:not(.open)"),
                $dropdownToggle = $closedDropdown.find(".dropdown-toggle");
            if ($closedDropdown.foundOne()) {
                $dropdownToggle.dropdown("toggle");
                // Disable the dropdown toggle for a short moment to avoid the
                // user closing involuntarily the dropdown that was just opened by
                // the 'mouseover' event by clicking by reflex on the dropdown
                // toggle after having 'mouseenter'ed it.
                $dropdownToggle.bsFlickDropdownToggle();
            }
            return this;
        },

        bsCloseDropdown: function() {
            this
                .closest(".btn-group.open, .dropdown.open")
                .find(".dropdown-toggle")
                .dropdown("toggle");
            return this;
        },

        bsOpenDropdownOnMouseOver: function() {
            var $dropdownToggles = this.find(".dropdown-toggle"),
                openDelay = 175,
                closeDelay = 400;
            $dropdownToggles.each(function() {
                // Since we handle each dropdown individually, this can be called
                // on $("body") to enable at once mouseover reactivity on all dropdowns.
                var $dropdownToggle = $(this),
                    $dropdown = $dropdownToggle.parent();
                $dropdown.mouseleave(function(){
                    // Cancel the scheduled dropdown opening if leaving.
                    clearTimeout($dropdown.data("openTimer"));
                    // Schedule to close the dropdown a short moment after having left it.
                    $dropdown.data("closeTimer",
                        setTimeout(function(){
                            $dropdown.bsCloseDropdown();
                        }, closeDelay));
                    // Ensure that the dropdown toggle is enabled always when
                    // exiting it for the case that the timeout to reenable it has
                    // not yet fired.
                    $dropdownToggle.bsEnableDropdownToggle(true);
                });
                $dropdownToggle.mouseenter(function(){
                    // Cancel the scheduled dropdown closing if entering again.
                    clearTimeout($dropdown.data("closeTimer"));
                    // Note that the click action is still available for touch devices.
                    // Schedule to open the dropdown a very short moment after having entered the toggle button.
                    // This avoids opening menus when "flying" over the screen with the mouse.
                    $dropdown.data("openTimer",
                        setTimeout(function(){
                            $dropdown.bsOpenDropdown();
                        }, openDelay));
                });
            });
            return this;
        },

        bsOnToggleButtonPressed: function(callback){
            this.filter("button.btn[data-toggle=button][data-active-text]")
                .each(function () {
                    $(this).data("callbackOnToggleButtonUnpressed", callback);
                });
            return this;

        },

        bsOnToggleButtonUnpressed: function(callback){
            this.filter("button.btn[data-toggle=button][data-active-text]")
                .each(function () {
                    $(this).data("callbackOnToggleButtonPressed", callback);
                });
            return this;
        },

        bsEnableToggleButton: function(){
            this.filter("button.btn[data-toggle=button][data-active-text]")
                .clickWhenEnabled(function () {
                    var callback;
                    if (this.hasClass("active")) {
                        callback = this.data("callbackOnToggleButtonPressed");
                        if ($.isFunction(callback)) {
                            callback.call(this);
                        }
                        this.button("reset");
                    } else {
                        callback = this.data("callbackOnToggleButtonUnpressed");
                        if ($.isFunction(callback)) {
                            callback.call(this);
                        }
                        this.button("active");
                    }
                });
            return this;
        },

        bsAddDynamicHelpHint: function(helpHintStr) {
            return this.bsAddAlertPopover({
                type:       "info",
                content:    helpHintStr,
                trigger:    "manual hover",
                placement:  "bottom"
            });
        },

        bsAddAlertPopover: function(optionsObject) {
            var contentMaxLength = 500,
                options = $.extend(
                {
                    type:       "info",
                    trigger:    "hover",
                    placement:  "bottom",
                    container:  "body",
                    content:    "",
                    html:       false,
                    delay:      {show: 600, hide: 100}
                },
                optionsObject
                );
            options.template = {
                info:       "<div class=\"popover ss-alert-popover ss-alert-popover-info\" role=\"tooltip\"><div class=\"arrow\"></div><div class=\"popover-content bg-info text-info\"></div></div>",
                success:    "<div class=\"popover ss-alert-popover ss-alert-popover-success\" role=\"tooltip\"><div class=\"arrow\"></div><div class=\"popover-content bg-success text-success\"></div></div>",
                warning:    "<div class=\"popover ss-alert-popover ss-alert-popover-warning\" role=\"tooltip\"><div class=\"arrow\"></div><div class=\"popover-content bg-warning text-warning\"></div></div>",
                error:      "<div class=\"popover ss-alert-popover ss-alert-popover-error\" role=\"tooltip\"><div class=\"arrow\"></div><div class=\"popover-content bg-danger text-danger\"></div></div>"
            }[options.type];
            options.content = ( $.type(options.content) === 'string' ) ? options.content.trimToMaxLength(contentMaxLength) : "";
            return this.popover(options);
        },

        bsEnableAlertPopovers: function() {
            this
                .find("[data-from-server]")
                    .each(function(i, elem){
                        var $elem = $(elem),
                            popoverOptions = $elem.dataIn("fromServer.alertPopoverOptions");
                        if ( $.isPlainObject(popoverOptions) ){
                            $elem.bsAddAlertPopover(popoverOptions);
                        }
                    });
            return this;
        },

        bsEnableExpandableProgressBars: function() {
            return this
                .hoverDelayed(
                    function () {
                        $(this).animate({height: "20px"}, 'fast');
                    },
                    function () {
                        $(this).animate({height: "4px"}, 'fast');
                    },
                    {
                        enter: 100,
                        leave: 400
                    },
                    ".progress.expand-on-hover");
        },

        bsEnableDynamicElements: function() {
            this
                .enableTooltipOnEllipsedTexts()
                .bsEnableAlertPopovers()
                // Enable popovers
                .find("[data-toggle='popover']")
                    .popover({
                        container:  "body",
                        html:       true,
                        delay:      200
                    })
                    .end()
                // Enable tooltips
                .find("[data-toggle='tooltip']")
                    .tooltip();
            return this;
        },

        // Image Preloader utils
        //
        // '.imagesLoaded()' requires following library:
        //   <script src="external/imagesloaded/js/imagesloaded.min.js"></script>
        // Source: https://github.com/desandro/imagesloaded

        reloadImage: function(imgSrcArg, always){
            // Put 'imgSrcArg' as centered justified background image only when and if it's loaded.
            // If 'imgSrcArg' is not given, it simply reloads the current image.
            var $imgPreloaders = this.filter("img.ss-image-preloader");

            $imgPreloaders.each(function() {
                var $imgPreloader = $(this);
                if ($imgPreloader.data("alreadyLoadingImage")) {
                    $imgPreloader
                        .data("alreadyLoadingImage", true)
                        .parent()
                            .captureInlineOpacity();
                } else {
                    $imgPreloader
                        .parent()
                            .stop(true, true)
                            .restoreInlineOpacity();
                }
                if (imgSrcArg !== undefined) {
                $imgPreloader
                        .attr("src", imgSrcArg);
                }
            });
            function finishImageLoading($imgParent, image) {
                $imgParent
                    .restoreInlineOpacity()
                    .data("alreadyLoadingImage", false);
                if (always) {
                    always.call(image.img, image.isLoaded);
                }
            }
            $imgPreloaders
                .imagesLoaded()
                .progress( function( instance, image ) {
                    var img = image.img,
                        imgSrc = img.src,
                        $imgParent = $(img).parent(),
                        originalOpacity = $imgParent.css("opacity");
                    if (!imgSrc || imgSrc.isRootURL()) {
                        // Empty image src
                        var hasCurrentImage = /background-image/.test($imgParent.attr("style"));
                        if (hasCurrentImage){
                            // Remove image
                            $imgParent
                                .fadeTo(150, 0, function() {
                                    $imgParent.css("background-image", "");
                                })
                                .fadeTo(200, originalOpacity, finishImageLoading.partial($imgParent, image));
                        }
                    } else {
                        $imgParent
                            .fadeTo(150, 0, function() {
                                if (image.isLoaded) {
                                    $imgParent.css("background-image", "url(" + imgSrc +")");
                                } else {
                                    // Image link broken
                                    var image_placeholder = $imgParent
                                                                .css("background-image", "")
                                                                .css("background-image"),
                                        broken_image_placeholder = image_placeholder.replace(".png", "_warning.png");
                                    $imgParent.css("background-image", broken_image_placeholder);
                                }
                            })
                            .fadeTo(200, originalOpacity, finishImageLoading.partial($imgParent, image));
                    }
                });
            return this;
        },

        reloadAllImages: function(){
            this
                .find("img.ss-image-preloader")
                    .reloadImage();
            return this;
        }

    });

    util.string = {
        caseInsensitiveEqual: function (str1, str2) {
            if ($.type(str1) !== "string" || $.type(str2) !== "string") {
                return false;
            }
            return str1.toUpperCase() ===  str2.toUpperCase();
        },

        randomInt: function (length) {
            var numberOfDigits = length;
            if ($.type(length) !== "number" || length < 1 || length > 20) {
                numberOfDigits = 6; // default lenght
            }
            var number = Math.round(Math.random() * Math.pow(10, numberOfDigits)),
                padding = "00000000000000000000";
            return (padding + number).slice(-1 * numberOfDigits);
        },

        notEmpty: function(str) {
            return str ? str : undefined;
        },

        isEmpty: function(str) {
            return $.trim(str) === "" ? true : false;
        },

        isNotEmpty: function(str) {
            return ! this.isEmpty(str);
        },

        defaultIfEmpty: function(value, defaultValue) {
            return $$.util.string.isEmpty(value) ? defaultValue : value;
        }

    };

    util.boolean = {
        cast: function (x) {
            // Returns a boolean from a 'booly' value.
            return !! x;
        }
    };


    util.url = {
        hash: {
            segmentSeparator: "+",

            getValues: function() {
                return window.location.hash
                        .trimPrefix("#")
                        .split($$.util.url.hash.segmentSeparator)
                        .filter($$.util.string.notEmpty);
            },

            setValues: function(valuesArray) {
                // Setting the hash value directly with 'window.location.hash = value'
                // adds an entry to the browser history, so that the "back" button
                // does not work as expected. Instead, we replace the last entry in
                // the browser history (i.e. the current URL) with the modified hash.

                // Without arguments, 'set()' will remove the hash (see also 'clean()' below)
                // With one array argument, 'set()' will set the array items as hash value joined with 'segmentSeparator'.
                // With many arguments, 'set()' will set them as hash value joined with 'segmentSeparator'.
                var values = ( $.type(valuesArray) === "array" ) ? valuesArray : Array.prototype.slice.call(arguments),
                    updatedURL = $$.util.url.getCurrentURLWithoutHash();

                values = values.filter($$.util.string.notEmpty);

                if ( values.length > 0 ) {
                    updatedURL += "#" + values.join($$.util.url.hash.segmentSeparator);
                }

                return history.replaceState(null, document.title, updatedURL);
            },

            updateValues: function(newValuesObjet) {
                if ( ! $.isPlainObject(newValuesObjet) ) {
                    throw "An object with the indexes values mapped to their new values is expected. Found instead: " + newValuesObjet;
                }
                var currentValues = $$.util.url.hash.getValues();
                $.each(newValuesObjet, function(k,v){
                    if ( k > currentValues.length ) {
                        throw "Out of bounds! Current hash only contains " + currentValues.length + " values. Indexes are 0-based.";
                    }
                    currentValues[k] = v;
                });
                return $$.util.url.hash.setValues(currentValues);
            },

            clean: function() {
                return $$.util.url.hash.setValues();
            }
        },

        getCurrentURLBase: function (withHash) {
            var path = window
                        .location
                        .pathname  // URL without query params
                        .trimSuffix("/new")
                        .trimSuffix("/module");
            if (withHash === true) {
                return path + document.location.hash;
            }
            return path;
        },
        getCurrentURLWithoutHash: function () {
            return window.location.href.split('#')[0];
        },
        getParentResourceURL: function () {
            var path = window.location.pathname; // URL without query params
            return path.trimLastURLSegment();
        },
        redirectTo: function (url) {
            // TODO: Which one if the correct way to redirect?
            // window.location = url;
            return window.location.assign(url);
        },
        reloadPage: function () {
            return this.redirectTo(window.location.href);
        },
        reloadPageWithoutHashInURL: function () {
            return this.redirectTo(this.getCurrentURLWithoutHash());
        },
        redirectToCurrentURLBase: function (withHash) {
            return this.redirectTo(this.getCurrentURLBase(withHash));
        },
        redirectToParentResourceURL: function () {
            return this.redirectTo(this.getParentResourceURL());
        }
    };

    util.urlQueryParams = {
        getValue: function (param) {
            try {
                return window.location.search.split(new RegExp("[?&]" + param + "="))[1].split("&")[0];
            } catch (e) {
                return undefined;
            }
        },
        remove: function () {
            var updatedQuerystring  = window.location.search.trimPrefix("?");
            $.each(arguments, function (i, param) {
                updatedQuerystring = updatedQuerystring.replace(new RegExp("(^|&)" + param + "=[^?&]+&?"), "");
            });
            if ( ! $$.util.string.isEmpty(updatedQuerystring) ) {
                updatedQuerystring = updatedQuerystring.ensurePrefix("?");
            }
            history.replaceState(
                null,
                document.title,
                window.location.origin + window.location.pathname + updatedQuerystring + window.location.hash
            );
        }
        // TODO: Create deparam() function from http://stackoverflow.com/questions/1131630/the-param-inverse-function-in-javascript-jquery
    };

    util.meta = {
        getMetaValue: function (name, $elem) {
            var metaFieldSelector = "meta[name=" + name + "]",
                $metaFieldElem;
            if ($elem) {
                $metaFieldElem = $elem.find(metaFieldSelector);
            } else {
                $metaFieldElem = $(metaFieldSelector);
            }
            return $$.util.string.notEmpty($metaFieldElem.attr("content"));
        },
        getPageType: function ($elem) {
            // Page type is one of 'view', 'edit', 'new', 'chooser', etc...
            // as in the slipstream.ui.util.page-type Clojure namespace.
            return this.getMetaValue("ss-page-type", $elem);
        },
        isPageType: function (pageTypes, $elem) {
            // Returns true is the current page type is one of the coma separated 'pageTypes'.
            return pageTypes.split(" ").contains(this.getPageType($elem));
        },
        getUserType: function ($elem) {
            // User type is one of 'super' or 'regular',
            // as in the slipstream.ui.util.curent-user/type-name Clojure fn.
            return this.getMetaValue("ss-user-type", $elem);
        },
        getUsername: function ($elem) {
            return this.getMetaValue("ss-username", $elem);
        },
        isSuperUserLoggegIn: function ($elem) {
            return util.string.caseInsensitiveEqual(this.getUserType($elem), "super");
        },
        getViewName: function ($elem) {
            // View name is one of 'user', 'module', 'dashboard', etc...
            // Technically it is the last segment of the view's Clojure namespace.
            // See clj/src/slipstream/ui/views/base.clj:215 or nearby ;)
            return this.getMetaValue("ss-view-name", $elem);
        },
        isViewName: function (viewNames, $elem) {
            // Returns true is the current view name is one of the coma separated 'viewNames'.
            return viewNames.split(" ").contains(this.getViewName($elem));
        }
    };

    util.leavingConfirmation = {

        pageHasChanges: false,

        elemSelToObserve: "input, textarea, option",
        eventsToObserve: "input change",

        flagPageAsChanged: function() {
            util.leavingConfirmation.pageHasChanges = true;
        },

        reset: function() {
            // The unsaved state warns the user when trying to navigate away
            // from the current page if any changes where made.
            // At some specific points, it is intended to leave the page even if
            // changes were made (e.g. when requesting to 'Save' the page, or when
            // requesting to 'Delete' a resource)
            // In that case, we reset the unsaved state to avoid asking for
            // confirmation before leaving.
            this.pageHasChanges = false;
            $("body").one(this.eventsToObserve, this.elemSelToObserve, util.leavingConfirmation.flagPageAsChanged);
        },

        askIfPageHasChanges: function(msg) {
            window.onbeforeunload = function() {
                return util.leavingConfirmation.pageHasChanges ? (msg || "If you leave this page you will lose your unsaved changes.") : null;
            };
            this.reset();
        }
    };

    util.recurrentJob = {

        jobs: {},

        runJobsOnlyOnWindowsFocused: function() {
            if (this.windowsFocusListenersSet === true) {
                return;
            }
            // NOTE: The event name has vendor prefixes only on Android.
            //       https://developer.mozilla.org/en-US/docs/Web/Guide/User_experience/Using_the_Page_Visibility_API
            //       http://www.html5rocks.com/en/tutorials/pagevisibility/intro/
            $(window.document)
                .on("visibilitychange", function() {
                    if (window.document.hidden) {
                        $$.util.recurrentJob.stopAll();
                    } else {
                        $$.util.recurrentJob.restartAll();
                    }
                });
            this.windowsFocusListenersSet = true;
        },

        setJob: function(name, callback, delayInSecs) {
            if (this.jobs[name]) {
                this.clear(name);
            }

            var newJob = {
                name:           name,
                callback:       undefined,
                delayInSecs:    delayInSecs,
                runCount:       0
            };

            function jobRunner(){
                newJob.runCount += 1;
                var ret = callback.call(newJob, name);
                if (ret === false && util.recurrentJob.jobs[name]) {
                    util.recurrentJob.stop(name);
                }
                return ret;
            }

            newJob.callback = jobRunner;
            this.jobs[name] = newJob;

            return this;
        },

        getJob: function(name) {
            var job = this.jobs[name];
            if (! job) {
                throw "No job found with name: " + name;
            }
            return job;
        },

        restart: function(name) {
            if ( $$.util.urlQueryParams.getValue("prevent-job-start") === "true" ) {
                console.warn("Job '" + name + "' will not be started because 'prevent-job-start=true' is set in the URL.");
                return;
            }
            this.runJobsOnlyOnWindowsFocused();
            var job = this.getJob(name),
                shouldSchedule = true;
            if (job.timeoutID === 0) {
                // Call it once right when restarting it
                shouldSchedule = (job.callback() !== false);
            } else if (job.timeoutID > 0) {
                this.stop(name);
            }
            if (shouldSchedule) {
                job.timeoutID = setInterval(job.callback, job.delayInSecs * 1000);
            }
            return this;
        },

        _logActionOnAll: function(actionName) {
            if ($.isEmptyObject(this.jobs)){
                console.log("No jobs to " + actionName.toLowerCase());
            } else {
                console.log(actionName + "ing all jobs: " + util.object.keysString(this.jobs));
            }
        },

        restartAll: function() {
            this._logActionOnAll("Restart");
            $.each(this.jobs, this.restart.bind(this));
            return this;
        },

        start: function(name, callback, delayInSecs) {
            if (! name || ! callback || ! delayInSecs) {
                throw "Provide all arguments: start(jobName, callback, delayInSecs) or try restart(jobName).";
            }
            this
                .setJob(name, callback, delayInSecs)
                .restart(name);
            return this;
        },

        stop: function(name){
            var job = this.getJob(name);
            clearTimeout(job.timeoutID);
            job.timeoutID = 0;
            return this;
        },

        stopAll: function() {
            this._logActionOnAll("Stop");
            $.each(this.jobs, this.stop.bind(this));
            return this;
        },

        clear: function(name){
            this.stop(name);
            delete this.jobs[name];
            return this;
        },

        clearAll: function() {
            this._logActionOnAll("Clear");
            $.each(this.jobs, this.clear.bind(this));
            return this;
        }
    };

    util.touch = {

        isTouchDevice: function () {
            // Source: http://stackoverflow.com/a/4819886
            return  'ontouchstart'         in window ||    // works on most browsers
                    'onmsgesturechange'    in window;      // works on ie10
        }

    };

    util.tour = {

        alice: {
            intro: {
                welcome:                "alice.intro.welcome",
                deployingWordpress:     "alice.intro.deploying-wordpress",
                waitingForWordpress:    "alice.intro.waiting-for-wordpress",
                wordpressInDashboard:   "alice.intro.wordpress-in-dashboard",
                wordpressRunning:       "alice.intro.wordpress-running"
            },
            introWithoutConnectors: {
                goToProfile:            "alice.intro-without-connectors.go-to-profile",
                editProfile:            "alice.intro-without-connectors.edit-profile",
                navigateBackToWelcome:  "alice.intro-without-connectors.navigate-back-to-welcome",
                welcome:                "alice.intro-without-connectors.welcome",
                deployingWordpress:     "alice.intro-without-connectors.deploying-wordpress",
                waitingForWordpress:    "alice.intro-without-connectors.waiting-for-wordpress",
                wordpressInDashboard:   "alice.intro-without-connectors.wordpress-in-dashboard",
                wordpressRunning:       "alice.intro-without-connectors.wordpress-running"
            }
        },

        current: function () {
            return $$.util.meta.getMetaValue("ss-current-tour");
        },

        enableMouseShield: function () {
            if ( $(".ss-tour-mouse-shield").foundNothing() ) {
                $("<div class=\"bootstro-backdrop ss-tour-mouse-shield\"></div>")
                    .appendTo("body");
            }
        },

        disableMouseShield: function () {
            $(".ss-tour-mouse-shield")
                .remove();
        },

        // Bootstro's Options

        // nextButton
        // nextButtonText
        // prevButton
        // prevButtonText
        // finishButton
        // finishButtonText
        // stopOnBackdropClick
        // stopOnEsc
        // margin
        // onComplete
        // onExit
        // onStep

        // Bootstro's public methods

        // bootstro.start(selector, options)
        // bootstro.go_to(i)
        // bootstro.stop()
        // bootstro.next()
        // bootstro.prev()
        // bootstro.bind()
        // bootstro.unbind()

        defaultOptions: {
            prevButton:   "<button class=\"btn btn-primary btn-xs bootstro-prev-btn\">« Prev</button>",
            nextButton:   "<button class=\"btn btn-primary btn-xs bootstro-next-btn\">Next »</button>",
            finishButton: "<button class=\"btn btn-xs btn-link bootstro-finish-btn\">Exit guided tour</button>",
            stopOnBackdropClick: false,
            beforeStart: undefined, // NOTE: This is not from bootstro
            onExit: function(){
                $$.util.tour.disableMouseShield();
            }
        },

        setup: function (options) {
            this.customOptions = options;
        },

        start: function() {
            var options = $.extend({}, this.defaultOptions, this.customOptions);
            if ( $.type(options.beforeStart) === "function" ) {
                options.beforeStart();
            }
            bootstro.start(".bootstro", options);
        },

        goToStep: function (stepIndex) {
            bootstro.goToStep(stepIndex);
        },

        askToStart: function() {
            $('#ss-start-tour-dialog').askConfirmation(function () {
                $$.util.tour.start();
            });
        },

        stop: function() {
            bootstro.stop();
        },

        cookiePrefix: "launch-tour.",

        shouldLaunchAny: function() {
            var args = Array.prototype.slice.call(arguments),
                tourNames,
                shouldLaunchIfUndefined,
                launchAny = false;
            if ( $.type(args.last()) === "boolean" ) {
                shouldLaunchIfUndefined = args.pop();
            }
            tourNames = args;
            $.each(tourNames,
                function(i, tourName) {
                     launchAny = $$.util.tour.shouldLaunch(tourName, shouldLaunchIfUndefined);
                     if ( launchAny ) {
                        // Break the 'each' loop
                        return false;
                     }
                });
            return launchAny;
        },

        shouldLaunch: function(tourName, shouldLaunchIfUndefined) {
            var persistedShouldLaunchBehaviour = $$.util.cookie.get(this.cookiePrefix + tourName),
                tourNameInURL = $$.util.urlQueryParams.getValue("tour");
            if ( $.type(persistedShouldLaunchBehaviour) === "boolean" ) {
                return persistedShouldLaunchBehaviour;
            } else if ( tourNameInURL ) {
                return tourNameInURL === tourName;
            } else if ( $.type(shouldLaunchIfUndefined) === "boolean" ) {
                // Persist the wanted behaviour
                $$.util.cookie.set(this.cookiePrefix + tourName, shouldLaunchIfUndefined);
                return shouldLaunchIfUndefined;
            } else if ( shouldLaunchIfUndefined !== undefined ) {
                throw "'shouldLaunchIfUndefined' must be a boolean.";
            }
        },

        optInDialogMutedKey: "optInDialogMuted",

        muteNextOptInDialog: function() {
            return $$.util.cookie.setShortLived(this.cookiePrefix + this.optInDialogMutedKey, true, 300);
        },

        shouldShowOptInDialog: function() {
            var persistedMuteOptInDialogBehaviour = $$.util.cookie.get(this.cookiePrefix + this.optInDialogMutedKey);
            $$.util.cookie.delete(this.cookiePrefix + this.optInDialogMutedKey);
            if ( persistedMuteOptInDialogBehaviour === true ) {
                return false; // I.e. if 'mute' is true, do show dialog
            }
            return true;
        },

        queueLaunch: function(tourName) {
            return $$.util.cookie.setShortLived(this.cookiePrefix + tourName, true, 180);
        },

        dequeueLaunch: function(tourName) {
            return $$.util.cookie.delete(this.cookiePrefix + tourName);
        },

        persistDismissal: function(tourName) {
            return $$.util.cookie.set(this.cookiePrefix + tourName, false);
        },

        forgetDismissal: function(tourName) {
            return $$.util.cookie.delete(this.cookiePrefix + tourName );
        }

    };

    util.cookie = {
        // Inspired from: http://www.w3schools.com/js/js_cookies.asp

        // cookies are scoped to the looged user
        scopePrefix: ($$.util.meta.getUsername() || "unlogged") + ".",

        setShortLived: function (cname, cvalue, ttl_in_secs) {
            // ttl_in_secs defaults to 5 min
            var d = new Date();
            d.setTime(d.getTime() + ((ttl_in_secs || 300)*1000));
            var expires = "expires="+d.toUTCString();
            document.cookie = this.scopePrefix + cname + "=" + cvalue + "; " + expires + ";path=/";
        },

        set: function (cname, cvalue, ttl_in_days) {
            // TTL defaults to 1 year
            return this.setShortLived(cname, cvalue, ((ttl_in_days || 365)*24*60*60));
        },

        get: function (cname) {
            var name = this.scopePrefix + cname + "=";
            var ca = document.cookie.split(';');
            for(var i=0; i<ca.length; i++) {
                var c = ca[i];
                while (c.charAt(0)===' ') c = c.substring(1);
                if (c.indexOf(name) === 0) {
                    var cvalue = c.substring(name.length, c.length);
                    switch (cvalue) {
                        case "true":    return true;
                        case "false":   return false;
                        default:        return $$.util.string.notEmpty(cvalue);
                    }
                }
            }
            return undefined;
        },

        delete: function (cname) {
            document.cookie = this.scopePrefix + cname + "=;expires=Wed; 01 Jan 1970";
        }

    };

}( window.SlipStream = window.SlipStream || {}, window.SlipStream.util = {}, jQuery ));});
