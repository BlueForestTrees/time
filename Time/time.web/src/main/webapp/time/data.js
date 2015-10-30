(function() {

	function Data() {

	}

	Data.prototype.getFacets = function(filter, scale, bucket, callback) {
		var params = {
			scale : scale,
			word : filter,
			bucket : bucket
		};
		$.get("facets", params).done(callback);
	}

	Data.prototype.getPhrases = function(filter, scale, bucket, callback) {
		var params = {
			scale : scale,
			bucket : bucket,
			word : filter
		};
		$.get("phrases", params).done(callback);
	}

	Time.Data = Data;

})();