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

$(document).ready(function() {

	$('#save-module-dialog').dialog({
		autoOpen: false,
		title: 'Save Module?',
		modal: true,
		buttons: {
			"Cancel": function() {
				$(this).dialog("close");
			},
			"Save": function(event) {
			    // the presence of parentname indicate a new module
				$(this).dialog("close");
				$("#module-comment").val($("#save-comment").val());
				var moduleVersion = $("#version").text();
				var moduleName = $("#module-name-input").val();
            	$("#form-save").attr("action", "/module/" + moduleName + "/" + moduleVersion + "?method=put")

            	sceditor2textarea('#execute');
                sceditor2textarea('#report');
                sceditor2textarea('#recipe');
                sceditor2textarea('#prerecipe');
                sceditor2textarea('#onvmadd');
                sceditor2textarea('#onvmremove');

        		$$.send($("#form-save"), event, $.put);
				return false;
			},
		}
	});
	$('#save-module-dialog').dialog('moveToTop');

})
