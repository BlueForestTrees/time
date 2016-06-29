(function() {
    // constructeur
    function viewport(_scale) {
        this._local = 0;
        this._scale = _scale;
        this._listener = null;
        this._zoom = 1;
    }

    viewport.prototype.move = function(increment) {
        this._local += increment;
        if(this._listener !== null){
            this._listener();
        }
    };

    viewport.prototype.setListener = function(_listener){
        this._listener = _listener;
    };

    viewport.prototype.delta = function() {
        return Math.round(this._local);
    };

    viewport.prototype.lookAt = function(bucket) {
        this._local = -Time.scale.firstSubBucket(this._scale - 1, bucket);
    };

    viewport.prototype.toCanvasX = function (bucketX) {
        return bucketX * this._zoom + this.delta();
    };

    viewport.prototype.toBucketX = function (mouseX) {
        return (mouseX - this.delta()) / this._zoom;
    };

    viewport.prototype.normalize = function (firstBucketX, lastBucketX, firstCanvasX, lastCanvasX){
        this._local = firstCanvasX - firstBucketX;
        console.log(this._local);
    };

    Time.Viewport = viewport;
})();