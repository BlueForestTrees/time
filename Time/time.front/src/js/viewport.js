(function() {
    // constructeur
    function viewport(_scale) {
        this._local = 0;
        this._zoom = 1;
        this._scale = _scale;
    }

    viewport.prototype.delta = function() {
        return Math.round(this._local);
    };

    viewport.prototype.toCanvasX = function (bucketX) {
        // y = ax + b
        return this._zoom * bucketX + this.delta();
    };

    viewport.prototype.toBucketX = function (canvasX) {
        // x = ( y - b ) / a
        return Math.round((canvasX - this.delta()) / this._zoom);
    };

    viewport.prototype.normalize = function (x1, x2, y1, y2){
        //on évalue a
        this._zoom = (y2 - y1) / (x2 - x1);
        //qu'on réutilise pour avoir b
        this._local = y1 - this._zoom*x1;
    };

    Time.Viewport = viewport;
})();