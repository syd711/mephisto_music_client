/**
 * Global Settings
 */
$.mobile.page.prototype.options.domCache = false;

/**
 * The onload handler of the site
 */
function initSite() {
    hidePlaylistView();
    initPlayer();
    initSearch();
    initSettings();
}

function initSearch() {
    $('#search').keypress(function (e) {
        if (e.which == 13) {
            var value = $('#search').val();
            if(value.length > 0) {
                hidePlaylistView();
                showCollectionView();
                setNameAndTitle('Search Result for "' + value + '"', '');
                $.getJSON('/rest/collections/search?term=' + value, function(data) {
                    showCollections(data.items);
                });
            }
        }
    });
}

/**
 * Loads the album to a responsive layout
 */
function albums() {
    hidePlaylistView();
    showCollectionView();
    setNameAndTitle('Albums', '');
    loading('Loading Albums');
    $.getJSON('/rest/collections/albums', function(data) {
        showCollections(data.items);
        loaded();
    });
}

/**
 * Looks up all albums of the given collection.
 */
function artistAlbums(collectionId) {
    hidePlaylistView();
    hideCollectionView();
    setNameAndTitle('Albums', '');
    loading('Loading Albums');
    $.getJSON('/rest/collections/artist/albums/' + collectionId, function(data) {
        if(data.items.length > 0) {
            setNameAndTitle('Albums', data.items[0].artist);
            setToolbarLink('artistAlbums(' + collectionId + ')');
        }
        showCollections(data.items);
        loaded();
    });
}

/**
 * Looks up all albums of the given genre.
 */
function genre(collectionId) {
    hidePlaylistView();
    hideCollectionView();
    setNameAndTitle('Genre', '');
    loading('Loading Genres');
    $.getJSON('/rest/collections/genre/albums/' + collectionId, function(data) {
        if(data.items.length > 0) {
            setNameAndTitle('Genre', data.items[0].genre);
            setToolbarLink('genre(' + collectionId + ')');
        }
        showCollections(data.items);
        loaded();
    });
}

/**
 * Displays the given list of collections.
 */
function showCollections(collectionsItems) {
    var items = [];
    $.each(collectionsItems, function(key, value) {
        var url = value.artUrl;
        var artist = value.artist || '&nbsp;';
        if(!url || url.length === 0) {
            url = 'img/cover.png';
        }
        var html='<div class="collections-container hover">';
        html+='<div><a href="#" onClick="showCollection(' + value.mid + ')"><img class="collection-thumbnail" with="150" height="150" alt="' + value.artist + '" src="' + url + '" /></a></div>';
        html+='<div class="collection-title"><a class="a-album" href="#" onclick="showCollection(' + value.mid + ')">' + value.name + '</a></div>';
        html+='<div class="collection-subtitle"><a href="#" class="a-album" onclick="artistAlbums(\'' + value.mid + '\')">' + artist + '</a></div>';
        html+='</div>';
        items.push(html);
    });
    $('#collection').empty();
    $('#collection').append(items);
    showCollectionView();
}

/**
 * Creates the list view for a song collection.
 */
function showCollection(id) {
    hideCollectionView();
    showPlaylistView();
    loading('Loading Album');
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
            if(value.genre) {
                html+='<div class="album-meta-data"><a class="a-album" href="#" onclick="genre(\'' + id + '\')">' + value.genre + '</a></div>';
            }

            if(value.year > 0) {
                html+='<div class="album-meta-data">' + value.year + '</div>';
            }
            html+='</div>';
            items.push(html);

            setNameAndTitle(value.name, artist);
            setToolbarLink('artistAlbums(' + id + ')');
            $('#playlist-header').empty();
            $('#playlist-header').append(items);

            var items = [];
            $.each(value.songs, function(key, song) {
                var track = song.track;
                if(song.track == 0) {
                    track = '';
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

            loaded();
       });
}

/**
 * Titles and names
 */
function setNameAndTitle(name, title) {
    $('#title-bar-name').html(name);
    $('#title-bar-title').html('<a id="toolbarlink" class="a-album" href="#">' + title + '</a>');
    if(title && title.length > 0) {
        $('#title-bar-separator').show();
    }
    else {
        $('#title-bar-separator').hide();
    }
}

/**
 * Sets the link for the top toolbar
 */
function setToolbarLink(call) {
  $('#toolbarlink').attr('onclick', call);
}

/**
 * The on-click mouse handler for a row
 */
function playTrack(id) {
    selectTrack(id);
    playSong(id);
}
function selectTrack(id) {
    var tableRows =  $('#row-track-' + id).parent().children();
    for(var i=0; i<tableRows.length; i++) {
        var rowId = tableRows[i].attributes.id.value;
        $('#'+rowId).removeClass('row-selected');
    }
    $('#row-track-' + id).addClass('row-selected');
}

/**
 * The on mouse over effect for the playback table.
 */
function showPlay(id) {
    $('#column-track-' + id).html('<span class="play-icon"/>');
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