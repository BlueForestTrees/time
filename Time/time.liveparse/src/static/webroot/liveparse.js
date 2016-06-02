(function() {
    function Upload() {
    }
    Upload.prototype.init = function(){
        console.log("init");
        Liveparse.upload.showBook();
        Liveparse.view.throbber.hide();
        Liveparse.view.metadatas.hide();
        Liveparse.view.upload.change(this.doUpload);
        Liveparse.view.uploadUrlBt.click(this.doUrlUpload);
        Liveparse.view.uploadUrlText.keypress(function (e) {
            if (e.which == 13) {
                Liveparse.upload.doUrlUpload();
            }
        });
    };

    Upload.prototype.doUpload = function(event) {
        console.log('uploading. . .');
        Liveparse.view.book.empty();
        Liveparse.view.metadatas.hide();
        Liveparse.view.throbber.show();
        var files = event.target.files;
        var data = new FormData();
        $.each(files, function(key, value)
        {
            data.append(key, value);
        });
        $.ajax({
            url: '/api/liveparse/file', type: 'POST', data: data, cache: false, processData: false, contentType: false,
            success: Liveparse.upload.onLiveparse,
            error: Liveparse.upload.onLiveparseError
        });
    };

    Upload.prototype.doUrlUpload = function(event) {
            console.log('uploading url. . .');
            Liveparse.view.metadatas.hide();
            Liveparse.view.book.empty();
            Liveparse.view.datedPhrases.empty();
            Liveparse.view.throbber.show();
            var url = "api/liveparse/url/" + encodeURIComponent(Liveparse.view.uploadUrlText.val());
            $.get(url).done(Liveparse.upload.onLiveparse).fail(Liveparse.upload.onLiveparseError);
     };

     Upload.prototype.onLiveparse = function(data){
        console.log('uploaded', data);
        Liveparse.view.throbber.hide();
        Liveparse.upload.updateMetadatas(data.metadata);
        Liveparse.view.metadatas.show();
        Liveparse.view.book.append(data.text);
        Liveparse.view.datedPhrases.append(Liveparse.upload.buildPhrasesHtml(data.datedPhrases))
     };

     Upload.prototype.onLiveparseError = function(data){
        console.error('onLiveparseError', data);
        alert("erreur :( TODO remonter l'erreur");
     };

    Upload.prototype.updateMetadatas = function(metaDico){
        console.log("updateMetadatas", metaDico);
        Liveparse.view.titre.val(metaDico.titre);
        Liveparse.view.auteur.val(metaDico.auteur);
        Liveparse.view.date.val(metaDico.date);
        Liveparse.view.paragraphes.val(metaDico.paragraphes);
        Liveparse.view.phrases.val(metaDico.phrases);
        Liveparse.view.url.val(metaDico.url);
        Liveparse.view.file.val(metaDico.file);
    };

    Upload.prototype.buildPhrasesHtml = function(phrases){
        console.log("buildPhrasesHtml ");
        var html = "";
        phrases.forEach(function(phrase){
            html += "<div class=\"phrase\">" + phrase.text + "</div>";
        });
        return html;
    };

    Upload.prototype.add = function(){
        var data = {
            titre : Liveparse.view.titre.val(),
            auteur : Liveparse.view.auteur.val(),
            date : Liveparse.view.date.val(),
            paragraphes : Liveparse.view.paragraphes.val(),
            phrases : Liveparse.view.phrases.val(),
            url : Liveparse.view.url.val(),
            file : Liveparse.view.file.val()
        };
        $.post("api/liveparse/add", JSON.stringify(data)).done(Liveparse.upload.onAdd).fail(Liveparse.upload.onAddError);
    };

    Upload.prototype.onAdd = function(){
        console.log("bravo for add!!");
    };

    Upload.prototype.onAddError = function(){
        console.log("shit for add error!!");
    }

    Upload.prototype.showBook = function(){
        Liveparse.view.datedPhrases.hide();
        Liveparse.view.book.show();
    };

    Upload.prototype.showDatedPhrases = function(){
        Liveparse.view.book.hide();
        Liveparse.view.datedPhrases.show();
    };

    Liveparse.Upload = Upload;
})();