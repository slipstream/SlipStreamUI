jQuery( function() { ( function( $$, $, undefined ) {

    function selectFromCombobox(e) {
        var $combobox = $(e),
            targetTab = $combobox.children("option:selected").attr("value");
            $tabAnchor = $combobox.parent().find("a[href='" + targetTab + "']");
        $tabAnchor.tab("show");
        return;
    }

    function enableSubsections() {
        // Match the height of the tab contents to the highest to avoid jumping
        // NOTE: Doesn't work if the element is not visible on page load (e.g.
        //       in a hidden section) since the height will be 0.
        $(".ss-subsection-group, .ss-subsection-group-stacked, .ss-subsection-group-dropdown").each(function (){
            var $thisTabPanes = $(this).find(".tab-pane"),
                highestHight = Math.max.apply(null,
                    $thisTabPanes.map(function(){
                        return $(this).height();
                    }));
            $thisTabPanes.css("min-height", highestHight);
            return;
        });

        // Sync the combobox .ss-subsection-activator-xs-group at page load
        $(".ss-subsection-activator-xs-group").each(function (){
            var $this = $(this),
                activeSubsectionId = $this.parent().find("li.active a").attr("href");
            $this.val(activeSubsectionId);
        });

        // Enable tab functionality, keeping in sync the combobox .ss-subsection-activator-xs-group
        $(".ss-subsection-activator-group a").click(function (e) {
            var $tabAnchor  = $(this),
                targetTab   = $tabAnchor.attr("href"),
                $combobox   = $tabAnchor.closest("div").find(".ss-subsection-activator-xs-group");
            e.preventDefault();
            $combobox.val(targetTab);
            $tabAnchor.tab("show");
        });

        // Enable ss-subsection-activator-xs-group
        $(".ss-subsection-activator-xs-group").change( function () {
            selectFromCombobox(this);
        });
    }

    enableSubsections();

    var subsectionIdPrefix = "ss-subsection-",
        onShowCallbackKey = "on-show-callback",
        tabAnchorSel = ".ss-subsection-activator-group a[role=tab]";

    $(tabAnchorSel).on("shown.bs.tab", function (e, extraParametersArg) {

        // Ensure correct hash when opening subsections
        var $tabAnchor          = $(this),
            subsectionTitle     = $tabAnchor.text(),
            subsectionId        = $tabAnchor.attr("href"),
            subsectionIdTrimmed = subsectionId
                                    .trimPrefix("#" + subsectionIdPrefix)
                                    .replace(/\d+-/, ""), // remove digits of the unique prefix
            $subsectionContent  = $(subsectionId),
            onShowCallback      = $tabAnchor.data(onShowCallbackKey),
            extraParameters = $.extend({
                    // In flat sections, do not persist the open subsection in the hash
                    skipHashUpdate: $tabAnchor.closest(".ss-section-flat").foundAny()
                },
                extraParametersArg);

        if (! extraParameters.skipHashUpdate) {
            $$.util.url.hash.updateValues({1: subsectionIdTrimmed});
        }

        // Run on-show-callback if present
        if ($.isFunction(onShowCallback)) {
            onShowCallback(subsectionTitle, $subsectionContent);
        }
    });

    $$.subsection = {
        reenableSubsections: enableSubsections,

        selectByTitle: function($section, title) {
            return $section
                       .find(".ss-subsection-activator-group")
                           .find("a[href^='#" + subsectionIdPrefix + "'][href$='" + title.toLowerCase() + "']")
                               .click()
                               .foundOne();
        },

        // Callback with the signature: function(subsectionTitle, $subsectionContent)
        onShow: function(callback) {
            $(tabAnchorSel).data(onShowCallbackKey, callback);
        },
        triggerOnShowOnOpenSubsection: function(skipHashUpdate) {
            var extraParameters = {
                skipHashUpdate: skipHashUpdate
            };
            $(".panel .panel-collapse.collapse.in, .ss-section-flat")       // The open collapsible section and the flat sections
                .find(".ss-subsection-group, .ss-subsection-group-stacked") // Its subsections (horizontal, vertical or combobox)
                    .find("li.active > a")                                  // The open subsection
                        .trigger("show.bs.tab", extraParameters)
                        .trigger("shown.bs.tab", extraParameters);
       }

    };

}( window.SlipStream = window.SlipStream || {}, jQuery ));});
