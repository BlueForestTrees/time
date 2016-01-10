(function() {
    // constructeur
    function viewport(scale) {
        this.global = 1000;
        this.local = 0;
        this.scale = scale;
        this.listener = null;
    }

    viewport.prototype.addToLocal = function(delta) {
        this.local += delta;
        if(this.listener !== null){
            this.listener();
        }
    };

    viewport.prototype.setListener = function(listener){
        this.listener = listener;
    };

    viewport.prototype.delta = function() {
        return this.global + this.local;
    };

    viewport.prototype.lookAt = function(bucket) {
        this.local = -Time.scale.firstSubBucket(this.scale - 1, bucket);
    };
    
    Time.Viewport = viewport;
})();