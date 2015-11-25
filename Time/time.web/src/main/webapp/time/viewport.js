(function() {
    //constructeur
    function viewport() {
        this.global = 1000;
        this.local = 0;
    };

    viewport.prototype.delta = function() {
        return this.global + this.local;
    };

    Time.Viewport = viewport;
})();