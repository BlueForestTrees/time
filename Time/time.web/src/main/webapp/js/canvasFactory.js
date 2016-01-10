(function() {
    function canvasFactory() {

    }

    canvasFactory.prototype.getNextCanvasId = function(scale) {
        return 'bar#' + scale;
    };

    canvasFactory.prototype.build = function(height, scale) {
        var canvasId = this.getNextCanvasId(scale);
        var canvasAttributes = {
            id : canvasId
        };
        var canvasCss = {
            border : '1px solid #CDCDCD',
            width : '100%',
            height : height + 'px'
        };
        $('<canvas>').attr(canvasAttributes)
                     .css(canvasCss)
                     .appendTo(Time.view.timeline);

        return document.getElementById(canvasId).getContext("2d");
    };

    Time.CanvasFactory = canvasFactory;
})();