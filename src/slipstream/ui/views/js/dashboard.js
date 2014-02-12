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

    $("#metering div.metric").metrics();

    var now = Math.ceil(new Date() / 1000);  // datetime in seconds
    var OPTIONS = {
        "1h": {
            period: 600,  // 10 minutes, in seconds
            groupby: "source",
            start_timestamp: now - 60*60  // last hour, in seconds
        },
        "1d": {
            period: 3600,  // 1 hours, in seconds
            groupby: "source",
            start_timestamp: now - 60*60*24  // last day, in seconds
        },
        "7d": {
            period: 86400,  // 1 day, in seconds
            groupby: "source",
            start_timestamp: now - 60*60*24*7  // last 7 days, in seconds
        },
        "30d": {
            period: 86400,  // 1 day, in seconds
            groupby: "source",
            start_timestamp: now - 60*60*24*30  // last 30 days, in seconds
        }
    };
    $("#metering-selector").change(function() {
        var opt = $("option:selected", this).val();
        $("#metering div.metric").metrics({params: OPTIONS[opt]});
    });

    // The next two event handlers are dedicated to redraw the plot grid
    // ensuring axis labels are displayed as expected. Indeed, we found that
    // when a DOM element is hidden (not visible for the user), axis labels
    // are missing. Because we are using an accordion widget (collapsed by
    // default) layout with tabs, not all elements from the DOM are not
    // visible by default. So we trigger the grid redraw when accordion is
    // activated and each time a metric tab is activated.
    function refresh(plot) {
        if (plot) {
            plot.setupGrid();
            plot.draw();
        }
    }

    $(".accordion").on("accordionactivate", function(event, ui) {
        if (ui.newPanel.length && ui.newPanel[0].id == "metering-header") {
            var plot = $("div.col2", ui.newPanel).first().data('plot');
            refresh(plot);
        }
    });

    $("#metering").on("tabsactivate", function(event, ui) {
        var plot = $(".col2", ui.newPanel).data('plot');
        refresh(plot);
    });
    // --

    $(".gauge").each(function(idx, elem) {
        var $elem = $(elem).width(200).height(160).css("display", "inline-block");
        new JustGage({
          id: elem.id,
          value: $elem.data('quota-current'),
          min: 0,
          max: $elem.data('quota-max') || 100,
          title: $elem.data('quota-title'),
          levelColorsGradient: false
        });
    });
});