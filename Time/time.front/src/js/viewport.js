(function() {
    // constructeur
    function viewport(_scale) {
        this._local = 0;
        this._zoom = 1;
        this._scale = _scale;
        this._listener = null;
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
        // a = zoom
        // b = delta
        // y = canvasX
        // x = bucketX
        // y = ax + b
        return this._zoom * bucketX + this.delta();
    };

    viewport.prototype.toBucketX = function (canvasX) {
        // x = ( y - b ) / a
        return (canvasX - this.delta()) / this._zoom;
    };

    viewport.prototype.normalize = function (x1, x2, y1, y2){
        this._zoom = (y2 - y1) / (x2 - x1);
        this._local = y1 - this._zoom*x1;
    };

    Time.Viewport = viewport;
})();