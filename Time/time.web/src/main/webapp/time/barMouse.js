
(function(){
	function BarMouse(bar, drawer, bucketSelect){
		this.bar = bar;
		this.drawer = drawer;
		this.mouse = {previousX:null, down:false, move:false};
		this.bucketSelect = bucketSelect;
	}
	BarMouse.prototype.installMouse = function(){
		$(this.bar.canvas).mousedown($.proxy(this.onMouseDown,this));
		$(this.bar.canvas).mousemove($.proxy(this.onMouseMove,this));
		$(this.bar.canvas).mouseup($.proxy(this.onMouseUp,this));
	};
	BarMouse.prototype.onMouseDown = function(e){
		this.mouse.down = true;
		this.mouse.previousX = e.clientX;//stocke en previous pour le prochain move.
	}
	BarMouse.prototype.onMouseMove = function(e){
		if (this.mouse.down) {
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
		this.mouse.down = false;
	};
	
	BarMouse.prototype.onMouseClick = function(e){
		var bucket = this.bar.searchBucketAt(e.clientX);
		if(bucket){
			this.bucketSelect(bucket);
		}
	}
	
	BarMouse.prototype.move = function(x){
		this.bar.viewport.x += x;
		this.drawer.draw(this.bar);
	}
	
	Time.BarMouse = BarMouse;
})();