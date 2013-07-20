/**
 * Global Settings
 */
$.mobile.page.prototype.options.domCache = false;

/**
 * The onload handler of the site
 */
function initSite() {
    albums();
    $('#volume-slider').hide();
    $('#progressbar').hide();
    $('#player-cover').hide();
}

/**
 * Loads the album to a responsive layout
 */
function albums() {
    hidePlaylistView();
    showCollectionView();
    setNameAndTitle('Albums', '');
    $.getJSON('/rest/collections/albums', function(data) {
        var albums = data.items;
        var items = [];
        $.each(albums, function(key, value) {
            var url = value.artUrl;
            var artist = value.artist || '&nbsp;';
            if(!url || url.length === 0) {
                url = 'img/cover.png';
            }
            var html='<div class="collections-container hover">';
            html+='<div><a href="#" onClick="showCollection(' + value.mid + ')"><img class="collection-thumbnail" with="150" height="150" alt="' + value.artist + '" src="' + url + '" /></a></div>';
            html+='<div class="collection-title"><a class="a-album" href="#" onclick="showCollection(' + value.mid + ')">' + value.name + '</a></div>';
            html+='<div class="collection-subtitle"><a href="#" class="a-artist" onclick="">' + artist + '</a></div>';
            html+='</div>';
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
            var html='<a href="#" onClick=""><div class=" hover">';
            html+='<div id="collection-mid" data-mid="' + id + '"><img id="album-cover" with="150" height="150" alt="' + value.artist + '" src="' + url + '" /></div>';
            html+='</div></a>';
            html+='<div style="margin-top: -150px;margin-left: 170px;">';
            html+='<div class="album-data">' + value.songs.length + ' Tracks</div>';
            html+='<div class="album-data">' + value.duration + '</div>';
            html+='<div style="height:10px;"></div>';
            html+='<div class="album-meta-data">' + value.genre + '</div>';
            if(value.year > 0) {
                html+='<div class="album-meta-data">' + value.year + '</div>';
            }
            html+='</div>';
            items.push(html);

            setNameAndTitle(value.name, artist);
            $('#playlist-header').empty();
            $('#playlist-header').append(items);

            var items = [];
            $.each(value.songs, function(key, song) {
                var track = song.track;
                if(song.track == 0) {
                    track = '-';
                }
                var html='<tr id="row-track-' + song.mid + '" class="row" onclick="playTrack(' + song.mid + ')" onmouseover="showPlay(' + song.mid+ ')" onmouseout="hidePlay(' + song.mid + ',' + track +')">';
                html+='<td class="tracklist-column track" id="column-track-' + song.mid + '">' + track + '</td>';
                html+='<td class="tracklist-column">' + song.name + '</td>';
                html+='<td class="tracklist-column">' + song.artist + '</td>';
                html+='<td class="tracklist-column">' + song.duration + '</td>';
                html+='</tr>';
                items.push(html);
            });
            $('#playlist-table-body').empty();
            $('#playlist-table-body').append(items);
       });
}

/**
 * Titles and names
 */
function setNameAndTitle(name, title) {
    $('#title-bar-name').html(name);
    $('#title-bar-title').html('<a class="a-album" href="#">' + title + '</a>');
    if(title && title.length > 0) {
        $('#title-bar-separator').show();
    }
    else {
        $('#title-bar-separator').hide();
    }
}

/**
 * The on-click mouse handler for a row
 */
function playTrack(id) {
    var tableRows =  $('#row-track-' + id).parent().children();
    for(var i=0; i<tableRows.length; i++) {
        var rowId = tableRows[i].attributes.id.value;
        $('#'+rowId).removeClass('row-selected');
    }
    $('#row-track-' + id).addClass('row-selected');
    play(id);
}

/**
 * The on mouse over effect for the playback table.
 */
function showPlay(id) {
    $('#column-track-' + id).html('<img src="img/play.png" alt="" border="" />');
}

/**
 * The mouse leave event for the playback table.
 */
function hidePlay(id, track) {
    $('#column-track-' + id).html(track);
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