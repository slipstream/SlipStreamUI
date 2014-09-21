jQuery( function() { ( function( $$, $, undefined ) {

    function getId(elem) {
        var elemId = $(elem).attr("id");
        if (! elemId) {
            elemId = 'ss-code-area-' + Math.floor(Math.random() * 100000);
            $(elem).attr("id", elemId);
        }
        return elemId;
    }

    $("pre.ss-code-viewer").each(function (){
        var thisId = getId(this);
        var viewer = ace.edit(thisId);
        // viewer.setTheme("ace/theme/solarized_light");
        viewer.setTheme("ace/theme/tomorrow");
        viewer.getSession().setMode("ace/mode/sh");
        viewer.setReadOnly(true);
        viewer.setHighlightActiveLine(false);
        viewer.setHighlightGutterLine(false);
        viewer.setShowInvisibles(false);
    });

    $("pre.ss-code-editor").each(function (){
        var thisId = getId(this);
        var editor = ace.edit(thisId);
        // editor.setTheme("ace/theme/solarized_dark");
        editor.setTheme("ace/theme/tomorrow_night");
        editor.getSession().setMode("ace/mode/sh");
        editor.setReadOnly(false);
        editor.setHighlightActiveLine(true);
        editor.setHighlightGutterLine(true);
        editor.setShowInvisibles(true);
    });

}( window.SlipStream = window.SlipStream || {}, jQuery )); });
