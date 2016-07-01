(function() {

    function Data() {

    }

    Data.prototype.getBuckets = function(term, scale, callback) {
        var params = {
            scale : scale,
            term : term
        };
        Time.anal.ga('send', 'event', 'buckets', term, scale);
        $.get("api/buckets", params).done(callback);
    };

    Data.prototype.getPhrases = function(request, field, from, to, lastKey, callback) {
        var params = {
            field : field,
            from : from,
            to : to,
            request : request,
            lastKey : lastKey
        };
        Time.anal.ga('send', 'event', 'phrases', request, field);
        $.get("api/phrases", params).done(callback);
    };

    Data.prototype.getSynonyms = function(term, callback) {
        var params = {
            term : term
        };
        Time.anal.ga('send', 'event', 'synonyms', term);
        $.get("api/synonyms", params).done(callback);
    };

    Time.Data = Data;

})();