(function() {
    // constructeur
    function viewport(scale) {
        this.global = 1000;
        this.local = 0;
        this.scale = scale;
        this.upScale = Scale.up(scale);
    }

    viewport.prototype.delta = function() {
        return this.global + this.local;
    };

    viewport.prototype.lookAt = function(bucket) {
        this.local = -Scale.firstSubBucket(this.upScale, bucket);
    };

    Time.Viewport = viewport;
})();