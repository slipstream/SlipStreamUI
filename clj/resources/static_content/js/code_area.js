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
        viewer.getSession().setMode("ace/mode/sh"); // TODO: Make auto-detect the lang
        viewer.setReadOnly(true);
        viewer.setHighlightActiveLine(false);
        viewer.setHighlightGutterLine(false);
        viewer.setShowInvisibles(false);
        viewer.setOption("minLines", 12);
        viewer.setOption("maxLines", 100);
    });

    $("pre.ss-code-editor").each(function (){
        var thisId = getId(this);
        var editor = ace.edit(thisId);
        // editor.setTheme("ace/theme/solarized_dark");
        editor.setTheme("ace/theme/tomorrow_night");
        editor.getSession().setMode("ace/mode/sh"); // TODO: Make auto-detect the lang
        editor.setReadOnly(false);
        editor.setHighlightActiveLine(true);
        editor.setHighlightGutterLine(true);
        editor.setShowInvisibles(true);
        editor.setOption("minLines", 40);
        editor.setOption("maxLines", 100);
    });

    $$.codeArea = {
        getCode: function (id) {
            var editor = ace.edit(id);
            var code = editor.getSession().getValue();
            return code;
        }
    };

}( window.SlipStream = window.SlipStream || {}, jQuery )); });
