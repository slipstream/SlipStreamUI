/*
 * +=================================================================+
 * SlipStream Server (WAR)
 * =====
 * Copyright (C) 2013 SixSq Sarl (sixsq.com)
 * =====
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * -=================================================================-
 */

sceditor_data = {};

function setAttributes(selector, attributes){
    var obj = $(selector)
    $.each(attributes, function(){
        obj.attr(this.name, this.value);
    });
}

function sceditor2textarea(selector){
    var obj = $(selector);
    if (obj.length < 1) return false;
    
    var id = obj.attr('id');
    var attributes = obj.prop("attributes");
    var code = sceditor_data[id].getValue();
    
    var textarea = $('<textarea id="' + id + '" />');
    textarea.text(code);
    obj.replaceWith(textarea);
    
    setAttributes(selector, attributes);
    
    return true;
}

function textarea2sceditor(selector, readonly){
    readonly = (typeof readonly === "undefined") ? false : true;

    var obj = $(selector);
    if (obj.length < 1) return false;
    
    var id = obj.attr('id');
    var attributes = obj.prop("attributes");
    
    obj.replaceWith(function(){
        return $('<div id="' + id + '" />').text($(this).val());
    });
    
    setAttributes(selector, attributes);
    
    var obj = $(selector);
    obj.height(300);
    obj.width(935);
    obj.parent().css('padding', 0);
    
    ace.require("ace/ext/language_tools");
    var editor = ace.edit(id); 
    sceditor_data[id] = editor;
    
    session = editor.getSession();
    var mode = detectLang(session);
    session.setMode("ace/mode/"+mode);
    session.setUseSoftTabs(true);
    session.setTabSize(4);
    editor.setHighlightActiveLine(true);
    editor.setShowPrintMargin(true);
    editor.setReadOnly(readonly);
    if (readonly == true){
        editor.setTheme("ace/theme/dawn");
    } else {
        editor.setTheme("ace/theme/ambiance");
    }
    editor.setHighlightSelectedWord(true);
    editor.setPrintMarginColumn(100); 
    editor.setOptions({enableBasicAutocompletion: true});
    
    session.on('change', function(e){
        if (arguments.length > 1){
            session = arguments[1];
            mode = detectLang(session);
            session.setMode("ace/mode/"+mode);
        }
    }); 
    
    return true;
}

function detectLang(session){
    var modes = ["sh", "python", "javascript", "powershell", "perl", "ruby", "tcl", "scheme", "plain_text"]
    var line = session.getLine(0);
    
    var out = line.match(/^#!(\/[a-z0-9./_-]+)*\/((env ([a-z0-9._-]+))|([a-z0-9._-]+))( .*)?$/i);
    if (out != null){
        var bin = (out[4] == undefined)? out[5] : out[4];
        if (bin != undefined && bin != null){
            bin = bin.toLowerCase();
            if(modes.indexOf(bin) > -1){
                return bin;
            }else{
                if (bin == 'psh') return 'perl';
                if (bin == 'pysh') return 'python';
                if (bin == 'scsh') return 'scheme';
                if (bin == 'wish') return 'tcl';
                if (bin.match(/sh$/i)) return 'sh';
                return 'plain_text';
            }
        }
    }
    return 'powershell';

    var lang = 'plain_text'
    var score_max = 0;
    var lines = Math.min(session.getLength(), 100) 
    modes.forEach(function(modename){
        session.setMode("ace/mode/" + modename)
        score = 0;
        for (var row = 0; row < lines; row++) {
            var tokens = session.getTokens(row)
            tokens.forEach(function(token){
                if (token.type == 'keyword' || token.type == 'support.function'){
                    score += 1;
                    //console.log(token);
                }
            });
        }
        //console.log(modename + ' score: ' + score);
        if (score > score_max){
            score_max = score;
            lang = modename;
        }
    }); 
    return lang;
}
