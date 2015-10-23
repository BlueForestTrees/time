(function(){
	function Bar(scale, drawer){
		this.scale = scale;
		this.viewport = new Time.Viewport(0);
		this.buckets = new Time.BucketsFactory().generateBuckets(1000);
		this.context = new Time.CanvasFactory().build(25);
		this.canvas = this.context.canvas;
		this.drawer = drawer;
		this.barmouse = new Time.BarMouse(this, this.drawer, $.proxy(this.bucketSelect,this));
		this.drawer.resize(this);
		$(window).resize($.proxy(function() {
			this.drawer.resize(this);
		},this));
		this.barmouse.installMouse();
		this.amplitude = 5;
	}
	
	Bar.prototype.searchBucketAt = function(xView){
		var imageData = this.context.getImageData(xView-1-this.amplitude, 10, 2*this.amplitude + 1, 1);
		var xViewFound = this.searchIn(imageData);
		var xBucket = xViewFound + xView - this.viewport.x;
		return this.getBucketAt(xBucket);
	};
	
	Bar.prototype.getBucketAt = function(xBucket){
		for(var i = 0; i < this.buckets.length; i++){
			var bucket = this.buckets[i];
			if(bucket.x == xBucket){
				return bucket;
			}
		}
		return null;
	}
	
	Bar.prototype.searchIn = function(imageData){
		var middle = this.amplitude + 1;
		var data = imageData.data;
		var found = null;
		for(var i = 0, j = 0; i < data.length; i+=4){
			if(data[i] > 0 || data[i+1] > 0 || data[i+2] > 0){
				if(found == null){
					found = j;
				}else if(Math.abs(j - middle) < Math.abs(found - middle)){
					found = j;
				}
			}
			j++;
		}
		return found-middle;
	};
	
	Bar.prototype.bucketSelect = function(bucket){
		console.log(this);
		console.log(bucket);
	};
	
	Time.Bar = Bar;
})();