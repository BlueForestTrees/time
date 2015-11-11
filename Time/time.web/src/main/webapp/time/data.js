(function() {

    function data() {

    }

    data.prototype.getBuckets = function(filter, scale, callback) {
        var params = {
            scale : scale,
            filter : filter
        };
        $.get("buckets", params).done(callback);
    };

    data.prototype.getPhrases = function(filter, scale, bucket, doc, score, lastIndex, callback) {
        var params = {
            scale : scale,
            bucket : bucket,
            filter : filter,
            doc : doc,
            score : score,
            lastIndex : lastIndex
        };
        $.get("phrases", params).done(callback);
    };

    Time.Data = data;

})();