(function(){
	function Bar(scale){
		this.scale = scale;
		this.viewport = new Time.Viewport(0);
		this.buckets = []; //new Time.BucketsFactory().generateBuckets(1000);
		this.context = new Time.CanvasFactory().build(25);
		this.canvas = this.context.canvas;
		this.amplitude = 5;
	}
	
	Bar.prototype.searchBucketAt = function(mouseX){
		var searchedBucketX= mouseX - this.viewport.x;		
		var imageData = this.context.getImageData(mouseX-this.amplitude, 10, 2*this.amplitude, 1);
		var offset = this.searchIn(imageData, mouseX);
		console.clear();
		if(offset !== null){			
			var bucket = this.getBucketAt(offset + searchedBucketX);
			
			console.log(this.scale,", date:", Scale.date(this.scale, bucket.bucket),", bucket: ", bucket);
			
			return bucket;
		}else{
			console.log(this.scale,", date:", Scale.date(this.scale, searchedBucketX),", bucket: ", searchedBucketX);
			return null;
		}
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
	
	Bar.prototype.searchIn = function(imageData, xView){
		var middle = this.amplitude;
		var data = imageData.data;
		var found = null;
		for(var i = 0, j = 0; i < data.length; i+=4){
			if(data[i] < 255 || data[i+1] < 255 || data[i+2] < 255){
				if(found == null){
					found = j;
				}else if(Math.abs(j - middle) < Math.abs(found - middle)){
					found = j;
				}
			}
			j++;
		}
		
		//console.log("returned: ", found ? found-middle : null);
		
		return found ? found-middle : null;
	};
	
	Time.Bar = Bar;
})();