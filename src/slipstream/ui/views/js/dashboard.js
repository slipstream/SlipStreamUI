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

    $("#fragment-vm-running").metrics({
        meter: "instance"
    });
    $("#fragment-cpu-requested").metrics({
        meter: "vcpus"
    });
    $("#fragment-memory-requested").metrics({
        meter: "memory"
    });
    $("#fragment-disk-requested").metrics({
        meter: "disk"
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

    var g1 = new JustGage({
      id: "gauge-vm",
      value: getRandomInt(0, 32),
      min: 0,
      max: 100,
      title: "Virtual Machines",
      levelColorsGradient: false
    });

    var g2 = new JustGage({
      id: "gauge-cpu",
      value: getRandomInt(34, 65),
      min: 0,
      max: 100,
      title: "CPU",
      levelColorsGradient: false
    });

    var g3 = new JustGage({
      id: "gauge-memory",
      value: getRandomInt(66, 100),
      min: 0,
      max: 100,
      title: "Memory",
      levelColorsGradient: false
    });

    var g4 = new JustGage({
      id: "gauge-disk",
      value: getRandomInt(66, 100),
      min: 0,
      max: 100,
      title: "Disk",
      levelColorsGradient: false
    });
});