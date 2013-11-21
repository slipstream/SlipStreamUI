(function($, window, document) {
    $.fn.metrics = function(options) {
        var settings = $.extend({
            url: "/meters/<meter>/statistics",
            colors: ["rgb(77, 169, 68)", "rgb(56, 128, 170)"],
            params: {
                period: 60 * 10, // 10 minutes , in seconds
                groupby: "source",
                start_timestamp: Math.ceil((new Date() - 1000*60*60) / 1000)  // last hour, in seconds
            }
        }, options );

        return this.each(function(elem) {
            $.ajax({
                url: settings.url.replace("<meter>", settings.meter),
                data: settings.params,
                context: this,
                success: function(samples, status, xhr) {
                    //---------------------------------------------------------
                    // Data coputation
                    //---------------------------------------------------------
                    var series = {};
                    $.each(samples, function(index, sample) {
                        var key = sample.groupby.source,
                            serie = [ sample.period_start * 1000.0, sample.sum ];
                        if (key in series) {
                            series[key].push(serie);
                        } else {
                            series[key] = Array(serie);
                        }
                    });

                    var lineData = [];
                    $.each(series, function(service, series) {
                        lineData.push({ label: service, data: series });
                    });

                    var pieData = [];
                    $.each(series, function(service, series) {
                        var last_idx = series.length - 1;
                        pieData.push({ label: service, data: series[last_idx][1] });
                    });

                    //---------------------------------------------------------
                    // UI refresh
                    //---------------------------------------------------------
                    var $this = $(this);
                    $this.empty();

                    $('<div class="col1" width="200" height="215"></div><div class="col2" width="590" height="215"></div><div class="col3"></div>').appendTo($this);

                    $(".col1", $this).plot(pieData, {
                        series: {
                            pie: {
                                radius: 0.75,
                                innerRadius: 0.5,
                                show: true,
                                label: { show: false },
                            }
                        },
                        legend: { show: false },
                        grid: { hoverable: true },
                        tooltip: true,
                        tooltipOpts: {
                            content: "%s: %p.0%",
                            shifts: {
                                x: 20,
                                y: 0
                            },
                            defaultTheme: false
                        },
                        colors: settings.colors
                    });

                    $(".col2", $this).plot(lineData, {
                        xaxis: { mode: "time" },
                        stack: true,
                        lines: {
                            show: true,
                            fill:true
                        },
                        legend: {
                            sorted: true ,
                            container: $(".col3", $this),
                        },
                        points: { show: true },
                        grid: { hoverable: true },
                        tooltip: true,
                        tooltipOpts: {
                            content: "%s: %y",
                            shifts: {
                                    x: 10,
                                    y: 20
                            },
                            defaultTheme: false
                        },
                        colors: settings.colors
                    });
                },
                dataType: "json",
            });
        });
    };
}(window.jQuery, window, document));