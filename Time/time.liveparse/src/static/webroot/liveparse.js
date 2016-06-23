(function() {
    function Liveparse() {
        console.log("init");
        this.view = {};
        $('[class]').each($.proxy(function(o, element) {
            this.view[$(element).attr("class")] = $(element);
        }, this));
        this.metadata = null;

        this.view.showBookBt.click($.proxy(this.showBook, this));
        this.view.addBt.click($.proxy(this.doAdd, this));
        this.view.showPhrasesBt.click($.proxy(this.showDatedPhrases, this));

        this.clear();
        this.showBook();
        this.view.throbber.hide();
        this.view.metadatas.hide();
        this.view.uploadFile.change($.proxy(this.doFileLiveparse, this));
        this.view.uploadUrlBt.click($.proxy(this.doUrlLiveparse, this));
        this.view.uploadUrlText.keypress($.proxy(function (e) {
            if (e.which == 13) {
                this.doUrlLiveparse();
            }
        }, this));
    }

    Liveparse.prototype.clear = function(){
        this.view.displayZone.hide();
        this.view.book.empty();
        this.view.metadatas.hide();
        this.view.datedPhrases.empty();
        this.view.throbber.show();
    };

    Liveparse.prototype.doFileLiveparse = function (event) {
        console.log('uploading. . .');
        this.clear();
        var files = event.target.files;
        var data = new FormData();
        $.each(files, function (key, value) {
            data.append(key, value);
        });
        $.ajax({
            url: '/api/App/file', type: 'POST', data: data, cache: false, processData: false, contentType: false,
            success: $.proxy(this.onLiveparse, this),
            error: $.proxy(this.onError, this)
        });
    };

    Liveparse.prototype.doUrlLiveparse = function(event) {
            console.log('doUrlLiveparse. . .');
            this.clear();
            var url = "api/App/url/" + encodeURIComponent(this.view.uploadUrlText.val());
            $.get(url)
             .done($.proxy(this.onLiveparse, this))
             .fail($.proxy(this.onLiveparseError, this));
     };

     Liveparse.prototype.onLiveparse = function(data){
        console.log('onLiveparse. . .');
        this.view.throbber.hide();
        this.updateMetadata(data.metadata);
        this.view.metadatas.show();
        this.view.book.append(data.text);
        this.view.datedPhrases.append(this.buildPhrasesHtml(data.datedPhrases));
        this.view.displayZone.show();
        this.unlockAddBt();
     };

    Liveparse.prototype.onError = function (data) {
        console.error('onError', data.responseText);
        alert("Erreur " + data.status + " " + data.statusText + " : voir la console");
    };

    Liveparse.prototype.updateMetadata = function (metadata) {
        console.log("updateMetadata", metadata);
        this.metadata = metadata;
        this.view.titre.val(this.metadata.titre);
        this.view.auteur.val(this.metadata.auteur);
        this.view.date.val(this.metadata.date);
        this.view.paragraphes.val(this.metadata.paragraphes);
        this.view.phrases.val(this.metadata.phrases);
        this.view.url.val(this.metadata.url);
        this.view.metadatas.show();
    };

    Liveparse.prototype.buildPhrasesHtml = function (phrases) {
        console.log("buildPhrasesHtml ");
        var html = "";
        phrases.forEach(function (phrase) {
            html += "<div class=\"phrase\">" + phrase.text + "</div>";
        });
        return html;
    };

    Liveparse.prototype.doAdd = function () {

        this.metadata.titre = this.view.titre.val();
        this.metadata.auteur = this.view.auteur.val();
        this.metadata.date = this.view.date.val();
        this.metadata.paragraphes = this.view.paragraphes.val();
        this.metadata.phrases = this.view.phrases.val();
        this.metadata.url = this.view.url.val();

        $.post("api/App/add", JSON.stringify(this.metadata))
                        .done($.proxy(this.onAdd, this))
                        .fail($.proxy(this.onError));
    };

    Liveparse.prototype.onAdd = function () {
        this.lockAddBt();
    };

    Liveparse.prototype.lockAddBt = function(){
        this.view.addBt.val("Ajouté");
        this.view.addBt.prop('disabled', true);
    };

    Liveparse.prototype.unlockAddBt = function(){
        this.view.addBt.val("Ajouter");
        this.view.addBt.prop('disabled', false);
    };

    Liveparse.prototype.showBook = function () {
        this.view.datedPhrases.hide();
        this.view.book.show();
    };

    Liveparse.prototype.showDatedPhrases = function () {
        this.view.book.hide();
        this.view.datedPhrases.show();
    };

    Time.Liveparse = Liveparse;
})();