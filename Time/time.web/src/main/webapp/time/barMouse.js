
(function(){
	function BarMouse(drawer){
		this.drawer = drawer;
		this.mouse = {previousX:null, bar:null, move:false, bucketSelect : null};
	}

	BarMouse.prototype.installMouse = function(bar, bucketSelect){
		$(bar.canvas).mousedown($.proxy(this.onMouseDown,this, bar, bucketSelect));
		//move et up seront global
		$(bar.canvas).mousemove($.proxy(this.onMouseMove,this));
		$(bar.canvas).mouseup($.proxy(this.onMouseUp,this));
	};
	BarMouse.prototype.onMouseDown = function(bar, bucketSelect, event){
		this.mouse.bar = bar;
		this.mouse.previousX = event.clientX;
		this.mouse.bucketSelect = bucketSelect;
	}
	BarMouse.prototype.onMouseMove = function(e){
		if (this.mouse.bar) {
			//la recup du X se fera par rapport au composant
			var currentX = e.clientX;
			this.move(currentX - this.mouse.previousX);
			this.mouse.previousX = currentX;
			this.mouse.move = true;
		}
	};
	BarMouse.prototype.onMouseUp = function(e){
		if(!this.mouse.move){
			this.onMouseClick(e);
		}else{
			this.mouse.move = false;			
		}
		this.mouse.bar = null;
	};
	
	BarMouse.prototype.onMouseClick = function(e){
		var bucket = this.mouse.bar.searchBucketAt(e.clientX);
		if(bucket){
			this.mouse.bucketSelect(bucket, this.mouse.bar);
		}
	}
	
	BarMouse.prototype.move = function(x){
		this.mouse.bar.viewport.x += x;
		this.drawer.draw(this.mouse.bar);
	}
	
	Time.BarMouse = BarMouse;
})();