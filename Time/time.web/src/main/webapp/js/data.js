(function() {

    function data() {

    }

    data.prototype.getBuckets = function(term, scale, callback) {
        var params = {
            scale : scale,
            term : term
        };
        Time.anal.ga('send', 'event', 'buckets', term, scale);
        $.get("api/buckets", params).done(callback);
    };

    data.prototype.getPhrases = function(term, field, from, lastKey, callback) {
        var params = {
            field : field,
            from : from,
            to : null,
            term : term,
            lastKey : lastKey
        };
        Time.anal.ga('send', 'event', 'phrases', term, field);
        $.get("api/phrases", params).done(callback);
    };

    data.prototype.getSynonyms = function(term, callback) {
        var params = {
            term : term
        };
        Time.anal.ga('send', 'event', 'synonyms', term);
        $.get("api/synonyms", params).done(callback);
    };

    Time.Data = data;

})();