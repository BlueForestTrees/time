(function() {
    Time.historic = {};

    Time.historic.params = {};

    Time.historic.popState = function() {
        var params = {};
        var url = document.location.href;
        if (url.indexOf("?") !== -1) {
            url.split("?")[1].split('&').forEach(function(param) {
                var keyValue = param.split("=");
                params[keyValue[0]] = decodeURIComponent(keyValue[1]);
            });
        }
        if (params.q) {
            if (params.s && params.b) {
                var bar = Time.bars[params.s];
                Time.filter.term = params.q;
                Time.view.termInput.val(params.q);
                bar.beginStory(params.q);
            } else {
                Time.filter.onFilter(params.q, true);
            }
        }
        Time.historic.params = params;
    };

    Time.historic.pushState = function(term, bucket, scale) {
        if (Time.historic.params.q !== term || Time.historic.params.b !== bucket || Time.historic.params.s !== scale) {
            history.pushState("", "", "/?q=" + encodeURIComponent(term) + (scale && bucket ? "&s=" + scale + "b=" + bucket : ""));
        }
    };

})();
