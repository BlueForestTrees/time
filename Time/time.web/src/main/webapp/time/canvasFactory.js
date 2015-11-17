(function() {
    function canvasFactory() {

    }

    canvasFactory.prototype.build = function(height) {
        var canvasId = 'canvas' + $('canvas').length;
        $('<canvas>').attr({
            id : canvasId,
            width : '100%',
            height : height + 'px'
        }).css({
            border : '1px solid #000000'
        }).appendTo('#timeline');
        return document.getElementById(canvasId).getContext("2d");
    };

    Time.CanvasFactory = canvasFactory;
})();