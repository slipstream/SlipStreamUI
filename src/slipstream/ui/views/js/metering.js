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

        function get_series(samples) {
            var series = _gen_series(samples);
            return [ _get_current_series(series), _get_past_series(series) ];
        }

        function _gen_series(samples) {
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
            return series;
        }

        function _get_past_series(series) {
            var _series = [];
            $.each(series, function(service, series) {
                _series.push({ label: service, data: series });
            });
            return _series;
        }

        function _get_current_series(series) {
            var _series = [];
            $.each(series, function(service, series) {
                var last_idx = series.length - 1;
                _series.push({ label: service, data: series[last_idx][1] });
            });
            return _series;
        }

        return this.each(function(elem) {
            $.ajax({
                url: settings.url.replace("<meter>", settings.meter),
                data: settings.params,
                context: this,
                success: function(samples, status, xhr) {
                    var $this = $(this);
                    $this.empty();

                    if (xhr.status == 204) {
                        $("<div/>").addClass("empty-section").text("No samples currently available").appendTo($this);
                        return;
                    }

                    var series = get_series(samples);

                    $('<div class="col1"></div><div class="col2"></div><div class="col3"></div>').appendTo($this);

                    $(".col1", $this).css({
                        width: 200,
                        height: 215
                    }).plot(series[0], {
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

                    $(".col2", $this).css({
                        width: 590,
                        height: 215
                    }).plot(series[1], {
                        xaxis: {
                            show: true,
                            mode: "time",
                            reserveSpace: true
                        },
                        yaxis: {
                            show: true,
                            reserveSpace: true
                        },
                        stack: true,
                        lines: {
                            show: true,
                            fill: true
                        },
                        legend: {
                            sorted: true ,
                            container: $(".col3", $this),
                        },
                        points: { show: true },
                        grid: { hoverable: true, borderWidth: 0 },
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