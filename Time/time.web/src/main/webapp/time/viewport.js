(function() {
    // constructeur
    function viewport(scale) {
        this.global = 1000;
        this.local = 0;
        this.scale = scale;
    }

    viewport.prototype.delta = function() {
        return this.global + this.local;
    };

    viewport.prototype.lookAt = function(bucket) {
        this.local = -Scale.firstSubBucket(this.scale - 1, bucket);
    };

    Time.Viewport = viewport;
})();