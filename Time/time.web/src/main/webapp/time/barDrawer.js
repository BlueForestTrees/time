(function() {
    function barDrawer(bars) {
        this.bars = bars;
    }
    barDrawer.prototype.resize = function(bar) {
        bar.canvas.width = window.innerWidth - 2;
        this.draw(bar);
    };
    barDrawer.prototype.draw = function(bar) {
        bar.context.fillStyle = 'rgb(255,255,255)';
        bar.context.fillRect(0, 0, bar.canvas.width, bar.canvas.height);
        var nbBuckets = bar.buckets.length;
        for (var i = 0; i < nbBuckets; i++) {
            var bucket = bar.buckets[i];
            bar.context.fillStyle = bucket.color;
            bar.context.fillRect(bar.viewport.delta() + bucket.x, 0, 1, bar.canvas.height);
        }
    };

    barDrawer.prototype.hide = function(barIndex) {
        while (barIndex < this.bars.length) {
            this.hideBar(this.bars[barIndex]);
            barIndex++;
        }
    };

    barDrawer.prototype.hideBar = function(bar) {
        $(bar.canvas).hide();
    };
    barDrawer.prototype.showBar = function(bar) {
        $(bar.canvas).show();
    };

    barDrawer.prototype.clearText = function() {
        $('#phrases').empty();
    };
    barDrawer.prototype.setPhrases = function(phrases, filter) {
        phrases.phraseList.forEach(function(phrase) {
            $('#phrases').append(("<p>" + phrase.text + "</p>").replace(filter, '<strong>' + filter + '</strong>'));
        });
        $('#phrases').append("<p>   -   -   -   -   -   -   -   -   -   -   -   -   </p>");
    };

    Time.BarDrawer = barDrawer;
})();