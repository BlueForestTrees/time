(function() {

    function data() {

    }

    data.prototype.getBuckets = function(filter, scale, callback) {
        $.get("synonyms", {term:"dinosaure"}).done(function(data){console.log(data);});
        var params = {
            scale : scale,
            filter : filter
        };
        $.get("buckets", params).done(callback);
    };

    data.prototype.getPhrases = function(term, scale, bucket, lastKey, callback) {
        var params = {
            scale : scale,
            bucket : bucket,
            term : term,
            lastKey : lastKey
        };
        $.get("phrases", params).done(callback);
    };

    Time.Data = data;

})();