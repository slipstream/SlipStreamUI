jQuery( function() { ( function( $$, $, undefined ) {

    $(".ss-code-viewer").each(function (){
        var viewer = ace.edit($(this).attr("id"));
        // viewer.setTheme("ace/theme/solarized_light");
        viewer.setTheme("ace/theme/tomorrow");
        viewer.getSession().setMode("ace/mode/sh");
        viewer.setReadOnly(true);
        viewer.setHighlightActiveLine(false);
        viewer.setHighlightGutterLine(false);
        viewer.setShowInvisibles(false);
    });

    $(".ss-code-editor").each(function (){
        var editor = ace.edit($(this).attr("id"));
        // editor.setTheme("ace/theme/solarized_dark");
        editor.setTheme("ace/theme/tomorrow_night");
        editor.getSession().setMode("ace/mode/sh");
        editor.setReadOnly(false);
        editor.setShowInvisibles(true);
    });

}( window.SlipStream = window.SlipStream || {}, jQuery )); });
