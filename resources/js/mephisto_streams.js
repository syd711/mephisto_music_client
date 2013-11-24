/**
 * Global Stream variables
 */
var deletionSelection = -1;
var activeStreamId = -1;

/**
 * Loads the streams layout
 */
function streams() {
    hidePlaylistView();
    hideCollectionView();
    setNameAndTitle('Streams', '');
    $.getJSON('/rest/streams/all', function(data) {
        showStreams(data);
        $.getJSON('/rest/streams/stream', function(data) {
            if(data) {
                paused(function(paused) {
                    activeStreamId = data.mid;
                    if(paused) {
                        updatePauseIcon(data.mid);
                    }
                    else {
                        updatePlayIcon(data.mid);
                    }

                });
            }
            hidePlaylistView();
            loaded();
        });
    });
}

/**
 * Displays a jquery mobile list with dnd support.
 */
function showStreams(data) {
    var html = '<ul data-role="listview" data-theme="c" data-inset="true" id="streamlist">';
    $.each(data.streams, function(key, value) {
        var url = value.url;
        var mid = value.mid;
        html+='<li data-mid="'+mid+'"><a href="#" onclick="playOrPauseStream(' + mid + ');">';
        html+='<img id="stream-playing-' + mid + '" style="margin-top: 7px;margin-left: 10px;display:none;" src="img/equalizer.gif" />';
        html+='<h1 id="stream-url-' + mid + '">' + url + '</h1>';
        html+='<p class="stream-artist" id="stream-artist-' + mid + '"></p>';
        html+=' </a>';
        html+='<a href="#deletePopup" onclick="markDeletion('+mid+')" data-rel="popup" data-theme="c" data-icon="delete" data-position-to="window" data-transition="pop">Delete Station</a>';
        html+='</li>';
    });
    html+='</ul>';
    html+='<div data-role="popup" id="deletePopup" data-theme="c" data-overlay-theme="b" class="ui-content" style="max-width:340px; padding-bottom:2em;">';
    html+='    <h3>Delete Station?</h3>';
    html+='    <p>This will remove the station from the list of streams.</p>';
    html+='    <a data-corners="false" href="#" onclick="deleteStream()" data-rel="back" data-role="button" data-theme="c" data-icon="check" data-inline="true" data-mini="true">Delete</a>';
    html+='    <a data-corners="false" href="#" data-role="button" data-rel="back" data-inline="true" data-mini="true">Cancel</a>';
    html+='</div>';

    $('#streamlist-section').empty();
    $('#streamlist-section').append(html).trigger('create');
    showStreamsView();
    bindSortable();
    refreshStreams();
    loaded();
}

/**
 * Stores the id of the station to delete.
 */
function markDeletion(mid) {
    deletionSelection = mid;
}

/**
 * Deletes the station with the current deletionSelection value.
 */
function deleteStream() {
    loading('Deleting Stream...');
    $.getJSON('/rest/streams/delete/' + deletionSelection, function(result) {
        streams();
    }).error(showErrorState);
}

/**
 * Add the given station to the list list.
 */
function addStream() {
    loading('Adding Stream...');
    var url = $('#station-url-text').val();
    if(url.length > 0) {
        $.getJSON('/rest/streams/add?url=' + url, function(result) {
            streams();
            loaded();
        }).error(showErrorState);
    }
}

/**
 * DND support
 */
function bindSortable() {
    $( "#streamlist" ).sortable();
    $( "#streamlist" ).disableSelection();
    <!-- Refresh list to the end of sort to have a correct display -->
    $( "#streamlist" ).bind( "sortstop", function(event, ui) {
        $('#streamlist:visible').listview('refresh');
        saveOrder();
    });
}

/**
 * Pause or play of the clicked stream
 */
function playOrPauseStream(mid) {
    if(mid == activeStreamId) {
        paused(function(paused) {
            if(paused) {
                playStream(mid);
            }
            else {
                updatePauseIcon(mid);
                $.getJSON('/rest/player/pause', function(data) {
                    //nothing
                }).error(showErrorState);
            }
        });
    }
    else {
        playStream(mid);
    }
}

/**
 * Playback of the stream with the given id.
 */
function playStream(mid) {
    updatePlayIcon(mid);
    $.getJSON('/rest/streams/play/' + mid, function(stream) {
      activeStreamId = mid;
      refreshStream(stream);
    }).error(showErrorState);
}


/**
 * Shows the playing icons
 */
function updatePlayIcon(mid) {
    var items = $('#streamlist').children();
    for(var i=0; i<items.length; i++) {
        var listMid = parseInt(items[i].getAttribute('data-mid'));
        if(listMid != mid) {
             $('#stream-playing-'+listMid).hide();
        }
    }
    var url = $('#stream-url-'+mid).html();
    setNameAndTitle(url, '');
    $('#stream-playing-'+mid).attr('style', 'margin-top: 7px;margin-left: 10px;');
    $('#stream-playing-'+mid).attr('src', 'img/equalizer.gif');
    $('#stream-playing-'+mid).show();
}

/**
 * Shows the paused icons
 */
function updatePauseIcon(mid) {
    updatePlayIcon(mid);
    $('#stream-playing-'+mid).attr('style', 'margin-top: 28px;margin-left: 31px;');
    $('#stream-playing-'+mid).attr('src', 'img/paused.gif');
    $('#stream-playing-'+mid).show();
}

/**
 * Refreshes the ID tag of the streams, if available.
 */
function refreshStreams() {
    $.getJSON('/rest/streams/all', function(data) {
        $.each(data.streams, function(key, stream) {
            refreshStream(stream);
        });
    }).error(showErrorState);
}

/**
 * Station info refresh for the given station.
 */
function refreshStream(stream) {
    var mid = stream.mid;
    var artist = renderArtistInfo(stream);
    $('#stream-artist-' + mid).empty();
    $('#stream-artist-' + mid).html(artist);
}

/**
 * Persists the sort order
 */
function saveOrder() {
    var ids = '';
    var items = $('#streamlist').children();
    for(var i=0; i<items.length; i++) {
        var listMid = parseInt(items[i].getAttribute('data-mid'));
        ids+=listMid+',';
    }
    $.getJSON('/rest/streams/order?order='+ids, function(stream) {
        //nothing
    }).error(showErrorState);
}

/**
 * Renders the artist information or an error message if the stream could not be played.
 */
function renderArtistInfo(stream) {
    var artist = stream.title;
    if(!stream.playable) {
        artist = '<div class="stream-error">- invalid stream url -</div>';
    }
    else if(!artist) {
        artist = '<i>- no stream info available -</i>';
    }
    return artist;
}
