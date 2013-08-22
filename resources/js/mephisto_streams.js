/**
 * Loads the streams layout
 */
function streams() {
    hidePlaylistView();
    hideCollectionView();
    setNameAndTitle('Streams', '');
    $.getJSON('/rest/streams/all', function(data) {
        showStreams(data);
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
        var artist = value.title || '&nbsp;';
        html+='<li data-mid="'+mid+'"><a href="#" onclick="playStream(' + mid + ');">';
        html+='<img id="stream-playing-' + mid + '" style="margin-top: 7px;margin-left: 10px;display:none;" src="img/equalizer.gif" />';
        html+='<h1 id="stream-url-' + mid + '">' + url + '</h1>';
        html+='<p class="stream-artist">' + artist + '</p>';
        html+=' </a>';
        html+='<a href="#purchase" data-rel="popup" data-theme="c" data-icon="delete" data-position-to="window" data-transition="pop">Purchase album</a>';
        html+='</li>';
    });
    html+='</ul>';

    $('#collection').empty();
    $('#collection').append(html).trigger('create');
    showCollectionView();
    bindSortable();
}

/**
 * DND support
 */
function bindSortable() {
    $( "#streamlist" ).sortable();
    $( "#streamlist" ).disableSelection();
    <!-- Refresh list to the end of sort to have a correct display -->
    $( "#streamlist" ).bind( "sortstop", function(event, ui) {
      $('#streamlist').listview('refresh');
        saveOrder();
    });
}

/**
 * Playback of the clicked stream
 */
function playStream(mid) {
    var url = $('#stream-url-'+mid).html();
    setNameAndTitle(url, '');
    updatePlayIcon(mid);
    updatePauseIcon(mid);
//    $.getJSON('/rest/streams/play/' + mid, function(stream) {
//
//    }).error(showErrorState);
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
