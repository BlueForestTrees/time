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
            Time.filter.onFilter(params.q);
        }
        Time.history.params = params;
    };

    Time.history.pushState = function(term) {
        if (Time.history.params.q !== term) {
            history.pushState("", "", "/?q=" + encodeURIComponent(term));
        }
    };

})();
