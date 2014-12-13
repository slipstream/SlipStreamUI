jQuery( function() { ( function( $$, util, $, undefined ) {

    // String prototype extensions

    $.extend(String.prototype, {

        startsWith: function(str) {
            return (this.match("^" + str) == str);
        },

        endsWith: function(str) {
            return (this.match(str + "$") == str);
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

        keysString: function(object) {
            return Object.keys(object).join(", ");
        }

    };


    // Array prototype extensions

    $.extend(Array.prototype, {
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
        },

        contains: function(thing) {
            return ($.inArray(thing, this) === -1) ? false : true;
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

        onAltEnterPress: function (callback) {
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
                    var $thisIntern = $(this);
                    if (! $thisIntern.hasClass("disabled") && $.isFunction(callback)) {
                        callback.call($thisIntern, event);
                    } else if ($thisIntern.attr("disabled-reason") === "ss-super-only-action") {
                        $$.alert.showWarning("Your must be have administrator access to perform this action.");
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
                        $form.children("input:hidden[name=" + fieldName + "]").remove();
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
                    errorHelpHint: errorHelpHint
                };
            if (! compiledRequirements) {
                this.data("input-compiled-requirements", [requirement]);
            } else {
                compiledRequirements.push(requirement);
            }
            return this;
        },

        enableDisplayOfValidationHelpHint: function() {
            this.data("displayValidationHelpHint", true);
            return this;
        },

        enableLiveFormValidation: function() {
            this
                .findOfClass(this.formFieldToValidateCls)
                .onTextInputChange(function() {
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

        validateFormInput: function() {

            function getRequirements($fieldToValidate){
                if ($fieldToValidate.data("already-compiled-input-requirements")) {
                    return $fieldToValidate.data("input-compiled-requirements");
                }
                // lazily compile regexp only once
                var compiledRequirements = $fieldToValidate.data("input-compiled-requirements") || [],
                    requirements = $fieldToValidate.data("input-requirements") || [];
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
                        var $fieldToValidate  = $(this),
                            fieldValue      = $fieldToValidate.val() || "",
                            requirements    = getRequirements($fieldToValidate),
                            isRequired      = $fieldToValidate.isRequiredFormInput(),
                            isValidField    = true,
                            errorHelpHint;
                        if (fieldValue || isRequired) {
                            $.each(requirements, function(i, requirement){
                                if (requirement.test(fieldValue, $fieldToValidate) ===  false){
                                    isValidField = false;
                                    errorHelpHint = requirement.errorHelpHint;
                                    return false; // break 'for' loop
                                }
                            });
                        }
                        $fieldToValidate.toggleFormInputValidationState(isValidField, errorHelpHint);
                    });
            return this;
        },

        showGenericErrorFormAlert: function() {
            var $form = this.closest("form");
            $$.alert.showError($form.data("generic-form-error-message") ||
                "Please check the fields flagged in red before sending this form.");
            return $form;
        },

        setFormInputValidationState: function (stateArg, customHelpHint) {
            // 'stateArg' can be a boolean or a string: 'success', 'warning' or 'error'.
            // If it is a boolean 'true' means 'success' and 'false' means 'error'.
            var stateArgType = $.type(stateArg),
                allowedStateStrings = ["success", "warning", "error"],
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
                            isNewState = ($this.data("isNewState") !== state),
                            callback = $this.data("onFormFieldValidationCallback"),
                            callbackOnStateChange = $this.data("onFormFieldValidationStateChangeCallback"),
                            genericHelpHints = $this.data("generic-help-hints") || {},
                            validationHelpHint = customHelpHint || genericHelpHints[state],
                            displayHelpHint = $this.data("displayValidationHelpHint") || false, // A real boolean
                            $formGroup = $this.closest(".form-group");
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
                        $formGroup
                            .removeClass("has-success has-warning has-error")
                            .addClass("has-" + state)
                            .find(".ss-validation-help-hint")
                                .html(validationHelpHint)
                                .toggleClass("hidden", ! (displayHelpHint && validationHelpHint));
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

        cleanFormInputValidationState: function () {
            var $this = $(this),
                $formGroup = $this.closest(".form-group");
            if ($formGroup.foundNothing()) {
                throw "No .form-group element can be found from jQuery selection.";
            }
            $formGroup
                .removeClass("has-success has-warning has-error")
                .find(".ss-validation-help-hint")
                    .addClass("hidden");
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

        onTextInputChange: function(callback) {
            var $textInputFields = $(this).findIncludingItself("input[type=text], input[type=password], textarea");
            if ($textInputFields.foundNothing()) {
                return this;
            }
            // Inspired from: http://stackoverflow.com/a/6458946
            $textInputFields.on('input', callback);
            return this;
        },

        offTextInputChange: function(callback) {
            var $textInputFields = $(this).findIncludingItself("input[type=text], input[type=password], textarea");
            if ($textInputFields.foundNothing()) {
                return this;
            }
            $textInputFields.off('input', callback);
            return this;
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
                $alertElem.hide("slow", function() {
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

        updateContent: function(contentGetterFn, contentSetterFn, newContent, todoIfUpdated, callbackIfUpdated) {
            // NOTE: Refer to 'updateWith()', 'updateHTML()' and 'updateText()' below.
            // todoIfUpdated is an object with '{flashClosestSel: "tr", flashCategory: "danger"}'
            // to indicate what element to flash (and how) if 'this' was actually changed.
            // In that case, 'callbackIfUpdated' is also called, and receives the new content as argument.
            // In both 'todoIfUpdated' and 'callbackIfUpdated' have no effect if the 'newContent' is the
            // same as the original content.
            var $originalElem = this,
                $newElem = this,
                originalContent     = contentGetterFn.call($originalElem),
                shouldUpdateContent = (originalContent != newContent),
                shouldVisuallyHighlightUpdate = true,
                fadeDuration        = 200;
            if (newContent instanceof jQuery) {
                if (! (originalContent instanceof jQuery)) {
                    throw "'contentGetterFn' should return a jQuery element if 'newContent' is a jQuery element.";
                }
                $newElem = newContent.bsEnableDynamicElements();
                // enable dynamic Bootstrap elements beofre comparing outerHTML, since attributes might change.
                shouldUpdateContent = (newContent[0].outerHTML != originalContent[0].outerHTML);
                shouldVisuallyHighlightUpdate = (newContent.text() != originalContent.text());
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
                                    if ($.isPlainObject(todoIfUpdated)) {
                                        var closestSel = todoIfUpdated.flashClosestSel,
                                            $elemToFlash = closestSel ? $newElem.closest(closestSel) : $newElem;
                                        $elemToFlash.flash(todoIfUpdated.flashCategory);
                                    } else {
                                        $newElem.flash();
                                    }
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

        enableBottomShadowAdjustOnScroll: function (){
            $(this[0].contentWindow).scroll(this._onScrollCallbackToAdjustIFrameBottomShadowOpacity.partial(
                this
                    .outerHeight(),
                this
                    .closest(".ss-iframe-container")
                        .find(".ss-shadow-bottom")
                ));
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
                    $dropdown = $dropdownToggle.closest(".btn-group, .dropdown");
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
                $dropdown.mouseenter(function(){
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

        bsEnableDynamicElements: function() {
            this
                // Enable popovers
                .find("[data-toggle='popover']")
                    .popover({
                        delay: 200
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
            if (imgSrcArg !== undefined) {
                $imgPreloaders
                    .attr("src", imgSrcArg);
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
                            $imgParent.fadeTo(150, 0, function() {
                                $imgParent.css("background-image", "");
                                $imgParent.fadeTo(200, originalOpacity,
                                    $.isFunction(always) ? always.bind(img, image.isLoaded) : undefined);
                            });

                        }
                        return;
                    }
                    $imgParent.fadeTo(150, 0, function() {
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
                        $imgParent.fadeTo(200, originalOpacity,
                            $.isFunction(always) ? always.bind(img, image.isLoaded) : undefined);
                    });
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
        }

    };

    util.url = {
        hash: {
            segmentSeparator: "+"
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
        getParentResourceURL: function () {
            var path = window.location.pathname; // URL without query params
            return path.trimLastURLSegment();
        },
        redirectTo: function (url) {
            // TODO: Which one if the correct way to redirect?
            // window.location = url;
            return window.location.assign(url);
        },
        reloadPageWithoutHashInURL: function () {
            return this.redirectTo(window.location.href.split('#')[0]);
        },
        redirectToCurrentURLBase: function (withHash) {
            return this.redirectTo(this.getCurrentURLBase(withHash));
        },
        redirectToParentResourceURL: function () {
            return this.redirectTo(this.getParentResourceURL());
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

    util.meta = {
        getMetaValue: function (name, $elem) {
            if ($elem) {
                return $elem.find("meta[name=" + name + "]").attr("content");
            } else {
                return $("meta[name=" + name + "]").attr("content");
            }
        },
        getPageType: function ($elem) {
            // Page type is one of 'view', 'edit', 'new', 'chooser', etc...
            // as in the slipstream.ui.util.page-type Clojure namespace.
            return this.getMetaValue("ss-page-type", $elem);
        },
        isPageType: function (pageType, $elem) {
            return util.string.caseInsensitiveEqual(this.getPageType($elem), pageType);
        },
        getUserType: function ($elem) {
            // User type is one of 'super' or 'regular',
            // as in the slipstream.ui.util.curent-user/type-name Clojure fn.
            return this.getMetaValue("ss-user-type", $elem);
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
        isViewName: function (viewName, $elem) {
            return util.string.caseInsensitiveEqual(this.getViewName($elem), viewName);
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
            $(window)
                .on("focus", function() {
                    $$.util.recurrentJob.restartAll();
                })
                .on("blur", function() {
                    $$.util.recurrentJob.stopAll();
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

}( window.SlipStream = window.SlipStream || {}, window.SlipStream.util = {}, jQuery ));});
