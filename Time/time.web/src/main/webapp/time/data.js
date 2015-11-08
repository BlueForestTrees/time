(function() {

    function data() {

    }

    data.prototype.getBuckets = function(filter, scale, bucket, callback) {
        var params = {
            scale : scale,
            filter : filter,
            bucket : bucket
        };
        $.get("buckets", params).done(callback);
    };

    data.prototype.getPhrases = function(filter, scale, bucket, last, callback) {
        var params = {
            scale : scale,
            bucket : bucket,
            filter : filter
        };
        if(last){
            params.last = last;
        }
        $.post("phrases", params).done(callback);
    };

    Time.Data = data;

})();