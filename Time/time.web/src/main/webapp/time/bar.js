(function(){
	function Bar(scale){
		this.scale = scale;
		this.viewport = new Time.Viewport(0);
		this.buckets = []; //new Time.BucketsFactory().generateBuckets(1000);
		this.context = new Time.CanvasFactory().build(25);
		this.canvas = this.context.canvas;
		this.amplitude = 5;
	}
	
	Bar.prototype.searchBucketAt = function(xView){
		//console.clear();
		
		var imageData = this.context.getImageData(xView-this.amplitude, 10, 2*this.amplitude, 1);
		var xViewFound = this.searchIn(imageData, xView);
		if(xViewFound !== null){			
			var xBucket = xViewFound + xView - this.viewport.x;
			var bucket = this.getBucketAt(xBucket);
			//console.log("look at " ,xView);
			//console.log("xBucket : ",xBucket);
			//console.log("selectedBucket : ",bucket);
			return bucket;
		}else{
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
						
			/*var xBucket = j-middle + xView - this.viewport.x;
			var bucket = this.getBucketAt(xBucket);
			if(bucket){
				console.log((j-middle), "    ", data[i],data[i+1],data[i+2] , '>>', bucket);				
			}else{
				console.log((j-middle), data[i],data[i+1],data[i+2]);
			}*/
			
			j++;
		}
		
		//console.log("returned: ", found ? found-middle : null);
		
		return found ? found-middle : null;
	};
	
	Time.Bar = Bar;
})();