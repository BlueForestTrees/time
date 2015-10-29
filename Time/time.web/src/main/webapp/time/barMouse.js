(function() {
	function BarMouse(drawer) {
		this.drawer = drawer;
	}

	BarMouse.prototype.installMouse = function(bar, bucketSelect) {
		var data = {
				previousX : null,
				deltaX : null,
				bar : bar,
				move : false,
				bucketSelect : bucketSelect,
				drawer : this.drawer
			};
		$(bar.canvas).on('mousedown.barmouse', data, $.proxy(this.onMouseDown, this));
	};
	
	BarMouse.prototype.onMouseDown = function(event) {
		event.data.previousX = event.clientX;
		$(window).on('mousemove.barmouse', event.data, $.proxy(this.onMouseMove, this));
		$(window).on('mouseup.barmouse', event.data, $.proxy(this.onMouseUp, this));
	}
	
	BarMouse.prototype.onMouseMove = function(event) {
		var previousX = event.data.previousX;
		var currentX = event.clientX;
		event.data.deltaX = currentX - previousX;
		this.moveBar(event);
		event.data.previousX = currentX;
		event.data.move = true;
	};
	
	BarMouse.prototype.onMouseUp = function(event) {
		if (!event.data.move) {
			this.onMouseClick(event);
		}else{
			console.log("viewport : ",event.data.bar.viewport.x);
		}
		event.data.move = false;

		$(window).off('mousemove.barmouse');
		$(window).off('mouseup.barmouse');
	};

	BarMouse.prototype.onMouseClick = function(event) {
		//-1 hack pour la bordure de 1px
		var mouseX = event.clientX-1;
		var bucket = event.data.bar.searchBucketAt(mouseX);
		if (bucket) {
			event.data.bucketSelect(bucket, event.data.bar);
		}
	}

	BarMouse.prototype.moveBar = function(event) {
		event.data.bar.viewport.x += event.data.deltaX;
		event.data.drawer.draw(event.data.bar);
	}

	Time.BarMouse = BarMouse;
})();