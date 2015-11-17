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
        $(bar.canvas).fadeOut(100);
    };
    barDrawer.prototype.showBar = function(bar) {
        $(bar.canvas).fadeIn(100);
    };

    barDrawer.prototype.clearText = function() {
        $('.phrases').empty();
    };
    barDrawer.prototype.setPhrases = function(phrases, filter) {

        var prevOne =  $('.phrases').children().last();
        var phraseOne = phrases.phraseList[0];
        if(!prevOne || phraseOne.text !== prevOne.text){
          $('.phrases').append(("<p date='"+phraseOne.date+"' page='"+phraseOne.pageUrl+"'>" + phraseOne.text + "</p>").replace(filter, '<strong>' + filter + '</strong>'));
        }

        for(var i = 1; i < phrases.phraseList.length; i++){
            var prev = phrases.phraseList[i-1];
            var phrase = phrases.phraseList[i];
            if(phrase.text !== prev.text){
                $('.phrases').append(("<p date='"+phrase.date+"' page='"+phrase.pageUrl+"'>" + phrase.text + "</p>").replace(filter, '<strong>' + filter + '</strong>'));
            }
        }

        $('.phrases').append("<p>   -   -   -   -   -   -   -   -   -   -   -   -   </p>");
    };

    Time.BarDrawer = barDrawer;
})();