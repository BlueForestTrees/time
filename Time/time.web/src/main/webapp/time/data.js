(function() {

    function data() {

    }

    data.prototype.getBuckets = function(term, scale, callback) {
        var params = {
            scale : scale,
            term : term
        };
        ga('send', 'event', 'buckets', 'get', params);
        $.get("buckets", params).done(callback);
    };

    data.prototype.getPhrases = function(term, scale, bucket, lastKey, callback) {
        var params = {
            scale : scale,
            bucket : bucket,
            term : term,
            lastKey : lastKey
        };
        ga('send', 'event', 'phrases', 'get', params);
        $.get("phrases", params).done(callback);
    };

    data.prototype.getSynonyms = function(term, callback) {
        var params = {
            term : term
        };
        ga('send', 'event', 'synonyms', 'get', params);
        $.get("synonyms", params).done(callback);
    };

    Time.Data = data;

})();