/**
 * Global Settings
 */
$.mobile.page.prototype.options.domCache = false;

/**
 * Loads the album to a responsive layout
 */
function albums() {
    hidePlaylistView();
    showCollectionView();
    $.getJSON('/rest/collections/albums', function(data) {
        var albums = data.items;
        var items = [];
        $.each(albums, function(key, value) {
            var url = value.artUrl;
            var artist = value.artist || '&nbsp;';
            if(!url || url.length === 0) {
                url = 'img/cover.png';
            }
            var html='<a href="#" onClick="showCollection(' + value.mid + ')"><div class="collection-container hover">';
            html+='<div><img class="collection-thumbnail" with="150" height="150" alt="' + value.artist + '" src="' + url + '" /></div>';
            html+='<div class="track-info"><i class="icon-music icon-1x"></i>&nbsp;&nbsp;' + value.size + '</div>';
            html+='<div class="time-info"><i class="icon-time icon-1x"></i>&nbsp;&nbsp;' + value.size + '</div>';
            html+='<div class="play-wrapper"><a href=""><i class="icon-play-circle icon-3x"></i></div>';
            html+='<div class="collection-title">' + value.name + '</div>';
            html+='<div class="collection-subtitle">' + artist + '</div>';
            html+='</div></a>';
            items.push(html);
        });
        $('#collection').empty();
        $('#collection').append(items);
    });
}

/**
 * Creates the list view for a song collection.
 */
function showCollection(id) {
    hideCollectionView();
    showPlaylistView();
    $.getJSON('/rest/collections/album/' + id, function(value) {

            var items = [];
            var url = value.artUrl;
            var artist = value.artist || '&nbsp;';
            if(!url || url.length === 0) {
            url = 'img/cover.png';
            }
            var html='<a href="#" onClick=""><div class="collection-container hover">';
            html+='<div><img class="collection-thumbnail" with="150" height="150" alt="' + value.artist + '" src="' + url + '" /></div>';
            html+='<div class="track-info"><i class="icon-music icon-1x"></i>&nbsp;&nbsp;' + value.size + '</div>';
            html+='<div class="time-info"><i class="icon-time icon-1x"></i>&nbsp;&nbsp;' + value.size + '</div>';
            html+='<div class="play-wrapper"><a href=""><i class="icon-play-circle icon-3x"></i></div>';
            html+='<div class="collection-title">' + value.name + '</div>';
            html+='<div class="collection-subtitle">' + artist + '</div>';
            html+='</div></a>';
            items.push(html);
            $('#playlist').empty();
            $('#playlist').append(items);
       });
}

/**
 * Hides generated elements
 */
function hideCollectionView() {
    $('#collection').hide();
}

function showCollectionView() {
    $('#collection').show();
}

function hidePlaylistView() {
    $('#playlist').hide();
}

function showPlaylistView() {
    $('#playlist').show();
}