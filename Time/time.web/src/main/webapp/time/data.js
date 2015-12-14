(function() {

    function data() {

    }

    data.prototype.getBuckets = function(term, scale, callback) {
        var params = {
            scale : scale,
            filter : term
        };
        ga('send', 'pageview', '/buckets/'+term);
        $.get("buckets", params).done(callback);
    };

    data.prototype.getPhrases = function(term, scale, bucket, lastKey, callback) {
        var params = {
            scale : scale,
            bucket : bucket,
            term : term,
            lastKey : lastKey
        };
        ga('send', 'pageview', '/phrases/'+term);
        $.get("phrases", params).done(callback);
    };

    data.prototype.getSynonyms = function(term, callback) {
        var params = {
            term : term
        };
        ga('send', 'pageview', '/synonyms/'+term);
        $.get("synonyms", params).done(callback);
    };

    Time.Data = data;

})();