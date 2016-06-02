(function() {
    function Upload() {
    }
    Upload.prototype.init = function(){
        console.log("init");
        Liveparse.upload.showBook();
        Liveparse.view.throbber.hide();
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
        Liveparse.view.metadatas.empty();
        Liveparse.view.book.empty();
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
            Liveparse.view.metadatas.empty();
            Liveparse.view.book.empty();
            Liveparse.view.datedPhrases.empty();
            Liveparse.view.throbber.show();
            var url = "api/liveparse/url/" + encodeURIComponent(Liveparse.view.uploadUrlText.val());
            $.get(url).done(Liveparse.upload.onLiveparse).fail(Liveparse.upload.onLiveparseError);
     };

     Upload.prototype.onLiveparse = function(data){
        console.log('uploaded', data);
        Liveparse.view.throbber.hide();
        Liveparse.upload.updateMetadatas(data.metadatas);
        Liveparse.view.book.append(data.text);
        Liveparse.view.datedPhrases.append(Liveparse.upload.buildPhrasesHtml(data.datedPhrases))
     };

     Upload.prototype.onLiveparseError = function(data){
        console.error('onLiveparseError', data);
        alert("erreur :( TODO remonter l'erreur");
     };

    Upload.prototype.updateMetadatas = function(metaDico){
        console.log("buildMetaHtml", metaDico);
        Liveparse.view.titre = metaDico.titre;
        Liveparse.view.auteur = metaDico.auteur;
        Liveparse.view.date = metaDico.date;
        Liveparse.view.paragraphes = metaDico.paragraphes;
        Liveparse.view.phrases = metaDico.phrases;
    };

    Upload.prototype.buildPhrasesHtml = function(phrases){
        console.log("buildPhrasesHtml ");
        var html = "";
        phrases.forEach(function(phrase){
            html += "<div class=\"phrase\">" + phrase.text + "</div>";
        });
        return html;
    };

    Upload.prototype.showBook = function(){
        Liveparse.view.datedPhrases.hide();
        Liveparse.view.book.show();
    }

    Upload.prototype.showDatedPhrases = function(){
        Liveparse.view.book.hide();
        Liveparse.view.datedPhrases.show();
    }

    Liveparse.Upload = Upload;
})();