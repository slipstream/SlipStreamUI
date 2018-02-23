jQuery( function() { ( function( $$, $, undefined ) {

    var $body = $("body"),
        runModel = $body.getSlipStreamModel().run,
        tagsAreBeingSavedDataName = "ssAreRunTagsBeingSaved",
        tagsSaveStartEventName = "ssRunTagsSaveStart",
        tagsSaveEndEventName = "ssRunTagsSaveEnd",
        runUUID = window.location.pathname.split('/').slice(-1)[0];

    function reenableTagsInput(wasCommitSuccessful) {
        this
            .enable()
            .setFormInputValidationState(wasCommitSuccessful)
            .data(tagsAreBeingSavedDataName, false);
        $body.trigger(tagsSaveEndEventName, wasCommitSuccessful);
        if (wasCommitSuccessful) {
            $$.util.leavingConfirmation.reset();
        }
    }

    function saveTags() {
        var $tagsInput = $(this);
        $tagsInput.validateFormInput();
        if ($tagsInput.isFormInputValidationState("error") ||
            $tagsInput.isFormInputValidationState(undefined) ||
            $tagsInput.data(tagsAreBeingSavedDataName)) {
            // do nothing
            return;
        }
        $tagsInput.data(tagsAreBeingSavedDataName, true);
        $body.trigger(tagsSaveStartEventName);
        $tagsInput
            .disable()
            .val(runModel.getTags());
        runModel.commitTags(reenableTagsInput.bind($tagsInput));
    }

    $("#ss-section-1-summary")
        .on("enterkeypress", "#tags", saveTags);

    $("#tags")
        .enableEnterKeyPressEvent()
        .enableLiveInputValidation();

    sixsq.slipstream.webui.deployment_detail.views.set_runUUID(runUUID);

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
