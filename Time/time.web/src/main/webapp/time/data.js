(function() {

    function data() {

    }

    data.prototype.getBuckets = function(term, scale, callback) {
        var params = {
            scale : scale,
            term : term
        };
        Time.anal.ga('send', 'event', 'buckets', term, scale);
        $.get("buckets", params).done(callback);
    };

    data.prototype.getPhrases = function(term, scale, bucket, lastKey, callback) {
        var params = {
            scale : scale,
            bucket : bucket,
            term : term,
            lastKey : lastKey
        };
        Time.anal.ga('send', 'event', 'phrases', term, scale);
        $.get("phrases", params).done(callback);
    };

    data.prototype.getSynonyms = function(term, callback) {
        var params = {
            term : term
        };
        Time.anal.ga('send', 'event', 'synonyms', term);
        $.get("synonyms", params).done(callback);
    };

    Time.Data = data;

})();