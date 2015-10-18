
(function(){
	function BarMouse(bar, drawer){
		this.bar = bar;
		this.drawer = drawer;
		this.mouseDown = false;
		this.mouse = {x:null,deltaX:null};
		this.softstop = null;
	}
	BarMouse.prototype.installMouse = function(){
		$(this.bar.canvas).mousedown($.proxy(this.onMouseDown,this));
		$(this.bar.canvas).mousemove($.proxy(this.onMouseMove,this));
		$(this.bar.canvas).mouseup($.proxy(this.onMouseUp,this));
	};
	BarMouse.prototype.onMouseDown = function(e){
		this.mouse.x = e.clientX;
		this.mouseDown = true;
	}
	BarMouse.prototype.onMouseMove = function(e){
		if (this.mouseDown) {
			var x = e.clientX;
			var deltaX = x - this.mouse.x;
			this.mouse = {x:x,deltaX:deltaX};
			this.move(this.mouse.deltaX);
		}
	};
	BarMouse.prototype.onMouseUp = function(e){
		this.mouseDown = false;
		var x = e.clientX;
		var deltaX = x - this.mouse.x;
		this.mouse = {x:x,deltaX:deltaX};
		this.softstop = this.mouse.deltaX*3;
		this.softStop();
	};
	
	BarMouse.prototype.move = function(x){
		this.bar.viewport.x += x;
		this.drawer.draw(this.bar);
	}

	BarMouse.prototype.softStop = function(){
		if(!this.mouseDown && Math.abs(this.softstop) > 1){
			this.move(this.softstop);
			this.softstop *= 0.85;
			setTimeout($.proxy(this.softStop,this), 20);
		}
	}
	
	Time.BarMouse = BarMouse;
})();