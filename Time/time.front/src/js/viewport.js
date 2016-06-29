(function() {
    // constructeur
    function viewport(scale) {
        this.global = 0;
        this.local = 0;
        this.scale = scale;
        this.listener = null;
        this.zoom = 1;
    }

    viewport.prototype.move = function(increment) {
        this.local += increment;
        if(this.listener !== null){
            this.listener();
        }
    };

    viewport.prototype.setListener = function(listener){
        this.listener = listener;
    };

    viewport.prototype.delta = function() {
        return Math.round(this.global + this.local);
    };

    viewport.prototype.lookAt = function(bucket) {
        this.local = -Time.scale.firstSubBucket(this.scale - 1, bucket);
    };
    
    viewport.prototype.setGlobal = function(global){
        this.global = 0;
    };

    viewport.prototype.locationOf = function (bucket) {
        return this.delta() + bucket.x * this.zoom;
    };

    Time.Viewport = viewport;
})();