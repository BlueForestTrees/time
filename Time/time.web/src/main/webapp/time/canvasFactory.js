(function() {
    function canvasFactory() {

    }

    canvasFactory.prototype.getNextCanvasId = function(scale) {
        return 'bar#' + scale;
    };

    canvasFactory.prototype.build = function(height, scale) {
        var canvasId = this.getNextCanvasId(scale);
        var canvasAttributes = {
            id : canvasId,
            width : '100%',
            height : height + 'px'
        };
        var canvasCss = {
            border : '1px solid #000000'
        };
        $('<canvas>').attr(canvasAttributes)
                     .css(canvasCss)
                     .appendTo(Time.view.timeline);

        return document.getElementById(canvasId).getContext("2d");
    };

    Time.CanvasFactory = canvasFactory;
})();