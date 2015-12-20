(function() {
    Time.history = {};

    Time.history.params = {};

    Time.history.popState = function() {
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
                var bar = Scale.bars[Scale.details[params.s].index];
                Time.filter.term = params.s;
                Time.view.termInput.val(params.s);
                bar.beginStory(params.s);
            } else {
                Time.filter.onFilter(params.q, true);
            }
        }
        Time.history.params = params;
    };

    Time.history.pushState = function(term) {
        if (Time.history.params.q !== term || Time.history.params.b !== bucket || Time.history.params.s !== scale) {
            history.pushState("", "", "/?q=" + encodeURIComponent(term));
        }
    };

    Time.history.pushCompleteState = function(term, bucket, scale) {
        if (Time.history.params.q !== term || Time.history.params.b !== bucket || Time.history.params.s !== scale) {
            history.pushState("", "", "/?q=" + encodeURIComponent(term) + "&s=" + scale + "b=" + bucket);
        }
    };

})();
