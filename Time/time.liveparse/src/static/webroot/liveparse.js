(function() {
    function Upload() {
        this.metadata = null;
    }

    Upload.prototype.init = function () {
        console.log("init");
        Liveparse.upload.showBook();
        Liveparse.view.throbber.hide();
        Liveparse.view.metadatas.hide();
        Liveparse.view.upload.change(this.doFileLiveparse);
        Liveparse.view.uploadUrlBt.click(this.doUrlLiveparse);
        Liveparse.view.uploadUrlText.keypress(function (e) {
            if (e.which == 13) {
                Liveparse.upload.doUrlLiveparse();
            }
        });
    };

    Upload.prototype.clear = function(){
        Liveparse.view.book.empty();
        Liveparse.view.metadatas.hide();
        Liveparse.view.datedPhrases.empty();
        Liveparse.view.throbber.show();
    }

    Upload.prototype.doFileLiveparse = function (event) {
        console.log('uploading. . .');
        Liveparse.upload.clear();
        var files = event.target.files;
        var data = new FormData();
        $.each(files, function (key, value) {
            data.append(key, value);
        });
        $.ajax({
            url: '/api/liveparse/file', type: 'POST', data: data, cache: false, processData: false, contentType: false,
            success: Liveparse.upload.onLiveparse,
            error: Liveparse.upload.onError
        });
    };

    Upload.prototype.doUrlLiveparse = function (event) {
        console.log('uploading url. . .');
        Liveparse.upload.clear();
        var url = "api/liveparse/url/" + encodeURIComponent(Liveparse.view.uploadUrlText.val());
        $.get(url).done(Liveparse.upload.onLiveparse).fail(Liveparse.upload.onError);
    };

    Upload.prototype.onLiveparse = function (data) {
        console.log('onLiveparse');
        Liveparse.view.throbber.hide();
        Liveparse.upload.updateMetadata(data.metadata);
        Liveparse.view.book.append(data.text);
        Liveparse.view.datedPhrases.append(Liveparse.upload.buildPhrasesHtml(data.datedPhrases))
    };

    Upload.prototype.onError = function (data) {
        console.error('onError', data.responseText);
        alert("Erreur " + data.status + " " + data.statusText + " : voir la console");
    };

    Upload.prototype.updateMetadata = function (metadata) {
        console.log("updateMetadata", metadata);
        this.metadata = metadata;
        Liveparse.view.titre.val(this.metadata.titre);
        Liveparse.view.auteur.val(this.metadata.auteur);
        Liveparse.view.date.val(this.metadata.date);
        Liveparse.view.paragraphes.val(this.metadata.paragraphes);
        Liveparse.view.phrases.val(this.metadata.phrases);
        Liveparse.view.url.val(this.metadata.url);
        Liveparse.view.metadatas.show();
    };

    Upload.prototype.buildPhrasesHtml = function (phrases) {
        console.log("buildPhrasesHtml ");
        var html = "";
        phrases.forEach(function (phrase) {
            html += "<div class=\"phrase\">" + phrase.text + "</div>";
        });
        return html;
    };

    Upload.prototype.doAdd = function () {

        this.metadata.titre = Liveparse.view.titre.val();
        this.metadata.auteur = Liveparse.view.auteur.val();
        this.metadata.date = Liveparse.view.date.val();
        this.metadata.paragraphes = Liveparse.view.paragraphes.val();
        this.metadata.phrases = Liveparse.view.phrases.val();
        this.metadata.url = Liveparse.view.url.val();

        $.post("api/liveparse/add", JSON.stringify(this.metadata)).done(Liveparse.upload.onAdd).fail(Liveparse.upload.onError);
    };

    Upload.prototype.onAdd = function () {
        console.log("bravo for doAdd!!");
    };

    Upload.prototype.showBook = function () {
        Liveparse.view.datedPhrases.hide();
        Liveparse.view.book.show();
    };

    Upload.prototype.showDatedPhrases = function () {
        Liveparse.view.book.hide();
        Liveparse.view.datedPhrases.show();
    };

    Liveparse.Upload = Upload;
})();