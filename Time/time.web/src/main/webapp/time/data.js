(function() {

	function Data() {

	}

	Data.prototype.getFacets = function(filter, scale, bucket, callback) {
		var params = {
			scale : scale,
			filter : filter,
			bucket : bucket
		};
		$.get("subbuckets", params).done(callback);
	}

	Data.prototype.getPhrases = function(filter, scale, bucket, callback) {
		var params = {
			scale : scale,
			bucket : bucket,
			filter : filter
		};
		$.get("phrases", params).done(callback);
	}

	Time.Data = Data;

})();