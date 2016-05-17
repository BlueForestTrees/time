(function() {
    function upload() {
    }
    upload.prototype.init = function(){
        console.log("init");
        Liveparse.view.upload.change(this.doUpload);
        Liveparse.view.uploadUrlBt.click(this.doUrlUpload);
        Liveparse.view.uploadUrlText.keypress(function (e) {
            if (e.which == 13) {
                Liveparse.upload.doUrlUpload();
            }
        });
    }

    upload.prototype.doUpload = function(event) {
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
    }

    upload.prototype.doUrlUpload = function(event) {
            console.log('uploading url. . .');
            Liveparse.view.metadatas.empty();
            Liveparse.view.book.empty();
            var url = "api/liveparse/url/" + encodeURIComponent(Liveparse.view.uploadUrlText.val());
            $.get(url).done(Liveparse.upload.onLiveparse).fail(Liveparse.upload.onLiveparseError);
     }

     upload.prototype.onLiveparse = function(data){
        console.log('uploaded', data);
        Liveparse.view.metadatas.append(Liveparse.upload.buildMetaHtml(data.metadatas));
        Liveparse.view.book.append(data.text);
     }

     upload.prototype.onLiveparseError = function(data){
        console.error('onLiveparseError', data);
        alert("erreur :( l'adresse est-elle correcte?")
     }

    upload.prototype.buildMetaHtml = function(metaDico){
        console.log("buildMetaHtml", metaDico);
        var html = "";
        for(var key in metaDico){
            html += "<div class=\"metadata\">" + key + ": " + metaDico[key] + "</div>";
        }
        return html;
    }
    Liveparse.Upload = upload;
})();