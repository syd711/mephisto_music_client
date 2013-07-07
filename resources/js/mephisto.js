/**
 * Global Settings
 */
$.mobile.page.prototype.options.domCache = false;

/**
 * Loads the album to a responsive layout
 */
function albums() {
    $.getJSON('/rest/albums/all', function(data) {
        var albums = data.items;
        var items = [];
        $.each(albums, function(key, value) {
            var url = value.artUrl;
            if(!url || url.length === 0) {
                url = 'img/info.png';
            }
            var html='<a href=""><div class="collection-container">';
            html+='<div><img class="collection-thumbnail" with="150" height="150" alt="' + value.artist + '" src="' + url + '" /></div>';
            html+='<div class="track-info"><i class="icon-music icon-1x"></i>&nbsp;&nbsp;' + value.size + '</div>';
            html+='<div class="time-info"><i class="icon-time icon-1x"></i>&nbsp;&nbsp;' + value.size + '</div>';
            html+='<div class="collection-title">' + value.name + '</div>';
            //html+='<div class="collection-subtitle">' + value.artist + '</div>';
            html+='</div></a>';
            items.push(html);
        });
        $('#collection').append(items);
    });
}

function showInterprets() {
	$('#content').load('interprets.html', function(){
	});
}
function showTitles() {
	$('#content').load('titles.html', function(){
	});
}
function showGenres() {
	$('#content').load('genres.html', function(){
	});
}