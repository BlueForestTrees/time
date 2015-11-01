(function() {
	function BarDrawer(bars) {
		this.bars = bars;
	}
	BarDrawer.prototype.resize = function(bar) {
		bar.canvas.width = window.innerWidth - 2;
		this.draw(bar);
	};
	BarDrawer.prototype.draw = function(bar) {
		bar.context.fillStyle = 'rgb(255,255,255)';
		bar.context.fillRect(0, 0, bar.canvas.width, bar.canvas.height);
		var nbBuckets = bar.buckets.length;
		for (var i = 0; i < nbBuckets; i++) {
			var bucket = bar.buckets[i];
			bar.context.fillStyle = bucket.color;
			bar.context.fillRect(bar.viewport.delta() + bucket.x, 0, 1, bar.canvas.height);
		}
	};

	BarDrawer.prototype.hide = function(scale) {
		do {
			var bar = this.bars[scale];
			this.hideBar(bar);
			scale = Scale.sub(scale);
		} while (scale);
	};

	BarDrawer.prototype.hideBar = function(bar) {
		$(bar.canvas).hide();
	};
	BarDrawer.prototype.showBar = function(bar) {
		$(bar.canvas).show();
	};

	BarDrawer.prototype.clearText = function() {
		$('#phrases').empty();
	};
	BarDrawer.prototype.setPhrases = function(phrases, filter) {
		this.clearText();
		for (facetIndex in phrases) {
			$('#phrases').append(("<p>" + phrases[facetIndex].text + "</p>").replace(filter,'<strong>'+filter+'</strong>'));
		}
	};

	Time.BarDrawer = BarDrawer;
})();