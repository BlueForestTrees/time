(function() {

	function Data() {

	}

	Data.prototype.getFacets = function(scale, bucket, word, callback) {
		var params = {
			scale : scale,
			word : word,
			bucket : bucket
		};
		$.get("facets", params).done(callback);
	}

	Data.prototype.getPhrases = function(scale, bucket, word, callback) {
		var params = {
			scale : scale,
			bucket : bucket,
			word : word
		};
		$.get("phrases", params).done(callback);
	}

	Time.Data = Data;

})();