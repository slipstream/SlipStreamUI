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

    fillVms = function(html) {
        $("#vms").html(html);
        $('.tab_block').tabs();
    };
    $.get("/vms", fillVms, "html");

    var OPTIONS = {
        "1h": {
            rollup: 600,            // 10 minutes, in seconds
            period: 60*60           // 1 hour, in seconds
        },
        "1d": {
            rollup: 3600,           // 1 hours, in seconds
            period: 60*60*24        // 1 day, in seconds
        },
        "7d": {
            rollup: 86400,          // 1 day, in seconds
            period: 60*60*24*7      // 7 days, in seconds
        },
        "30d": {
            rollup: 86400,          // 1 day, in seconds
            period: 60*60*24*30     // 30 days, in seconds
        }
    };

    function drawHistograms(panel) {
        if (panel === undefined) {
            var panel_idx = $("#metering").tabs('option', 'active');
            panel = $("#metering .ui-tabs-panel").get(panel_idx);
        }
        var opt_idx = $("#metering-selector option:selected").val();
        var opt = OPTIONS[opt_idx];

        $(panel).metrics({
            params: {
                period: opt["rollup"],
                groupby: "source",
                start_timestamp: Math.ceil(new Date() / 1000) - opt["period"],
                end_timestamp: Math.ceil(new Date() / 1000)
            }
        });
    }

    function drawGauges(panel) {
        $(".gauge", panel).each(function(idx, elem) {
            var $elem = $(elem).empty();
            new JustGage({
              id: elem.id,
              value: $elem.data('quota-current'),
              min: 0,
              max: $elem.data('quota-max') || 100,
              title: $elem.data('quota-title'),
              levelColorsGradient: false
            });
        });
    }

    $("#metering-selector").change(function() {
        drawHistograms();
    });

    $(".accordion").on("accordionactivate", function(event, ui) {
        if (ui.newPanel.length) {
            if (ui.newPanel[0].id == "metering") {
                drawHistograms();
            }
            if (ui.newPanel[0].id == "quota") {
                drawGauges(ui.newPanel);
            }
        }
    });

    $("#metering").on("tabsactivate", function(event, ui) {
        drawHistograms(ui.newPanel);
    });

    $("#quota").on("tabsactivate", function(event, ui) {
        drawGauges(ui.newPanel);
    });
});