(function() {
    function upload() {
    }
    upload.prototype.init = function(){
        console.log("init");
        Liveparse.view.upload.change(this.doUpload);
    }
    upload.prototype.doUpload = function(event) {
        console.log('uploading. . .');
        var files = event.target.files;
        var data = new FormData();
        $.each(files, function(key, value)
        {
            data.append(key, value);
        });
        $.ajax({
            url: '/api/upload', type: 'POST', data: data, cache: false,
            processData: false, contentType: false,
            success: function(data, textStatus, jqXHR)
            {
                console.log('uploaded');
                Liveparse.view.metadatas.empty();
                Liveparse.view.metadatas.append(Liveparse.upload.buildMetaHtml(data.metadatas));
                Liveparse.view.book.empty();
                Liveparse.view.book.append(data.text);
            },
            error: function(jqXHR, textStatus, errorThrown)
            {
                console.log('ERROR', jqXHR, textStatus, errorThrown);
            }
        });
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