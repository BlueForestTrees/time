var canvas;
var ctx;
var buckets;
var viewport;
var height;

$(document).ready(function() {
	canvas = document.getElementById('canvas');
	ctx = canvas.getContext("2d");
	height = canvas.height;
	init();
});

function init() {
	viewport = {
		x : 0
	};
	buckets = [];
	resizeCanvas();
	$(window).resize(function() {
		resizeCanvas();
	});

	var bucketsCount = 10000;
	generateBuckets(bucketsCount);
	drawBuckets(buckets);

	installMouse();
}

function resizeCanvas() {
	canvas.width = window.innerWidth - 2;
}

function changeColor(color) {
	ctx.fillStyle = color;
}
function addBucket(x) {
	ctx.fillRect(viewport.x + x, 0, 1, height);
}

function generateBuckets(count) {
	for (var i = 0; i < count; i++) {
		var x = (Math.random() - 0.333) * 30000; // -1000 à 2000
		var color = Math.random() * 16581375;
		var bucket = {
			x : x,
			color : color
		};
		buckets.push(bucket);
	}
}

function drawBuckets(buckets) {
	ctx.clearRect(0, 0, canvas.width, canvas.height);
	var count = buckets.length;
	for (var i = 0; i < count; i++) {
		var bucket = buckets[i];
		changeColor(bucket.color);
		addBucket(bucket.x);
	}
}

var mouseDown = false;
var mouse = {x:null,deltaX:null};
var softsoft = null;
function installMouse() {
	$('#canvas').mousedown(function(e) {
		mouse.x = e.pageX - this.offsetLeft;
		mouseDown = true;
	});
	$('#canvas').mousemove(function(e) {
		if (mouseDown) {
			var x = e.pageX - this.offsetLeft;
			var deltaX = x - mouse.x;
			mouse = {x:x,deltaX:deltaX};
			move(mouse.deltaX);
		}
	});
	$('#canvas').mouseup(function(e) {
		mouseDown = false;
		
		var x = e.pageX - this.offsetLeft;
		var deltaX = x - mouse.x;
		mouse = {x:x,deltaX:deltaX};
		
		softstop = mouse.deltaX*3;
		softStop();
	});
}

function move(x){
	viewport.x += x;
	drawBuckets(buckets);
}

function softStop(){
	if(!mouseDown && Math.abs(softstop) > 1){
		move(softstop);
		softstop *= 0.9;
		setTimeout(function(){ softStop() }, 10);
	}
}

