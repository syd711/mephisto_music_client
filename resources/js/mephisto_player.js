/**
 * Player functions
 ****************************************
 */
 var intervalId;
 function initPlayer() {
     $('#volume-slider').hide();
     $('#progressbar').hide();
     $('#player-cover').hide();
     $.getJSON('/rest/player/volumeenabled', function(data) {
        if(data) {
            applyVolumeSliderListener();
        }
        else {
            $('#volume-table').hide();
        }
     });

     loadActivePlaylist(function(playlist) {
        if(playlist) {
            showCollection(playlist.mid);
            refreshUI(playlist, false);
        }
        else {
            albums();
        }
     });
 }

/**
 * Retrieves the active playlist from the player.
 */
function loadActivePlaylist(callback) {
    $.getJSON('/rest/player/playlist', function(data) {
        callback(data);
    });
}

/**
 * Invoked for setting changes.
 */
function stopPlayer() {
    refreshUI(null, false);
}

/**
 * The toolbar play action
 */
function playPressed() {
    var id = getCollectionId();
    playCollection();
}


/**
 * The playlist play action
 */
function playCollection() {
    var collectionId = getCollectionId();
    $.getJSON('/rest/player/play/' + collectionId, function(data) {
        refreshUI(data, true);
    });
}

function playSong(id) {
    var collectionId = getCollectionId();
    $.getJSON('/rest/player/playsong/' + collectionId + '/' + id, function(data) {
        refreshUI(data, true);
    });
}

function pause() {
    $.getJSON('/rest/player/pause', function(data) {
        if(data) {
            $('#play-icon').attr('src','img/pplay.png');
            $('#play-action').attr('onclick', 'playPressed()');
        }
    });
}

function playPrevious() {
    $.getJSON('/rest/player/previous', function(data) {
        refreshUI(data, true);
    });
}

function playNext() {
    $.getJSON('/rest/player/next', function(data) {
        refreshUI(data, true);
    });
}

/**
 * Trigger the progressbar with the total millis of the active song.
 */
function triggerProgressBar(millis) {
    window.clearInterval(intervalId);
    $('#progressbar').show();
    var percentTotal = 0;
    var millisInterval = (millis/100)
    intervalId = window.setInterval(function() {
       percentTotal+=1;
       $('#progress').attr('style', 'width:' + percentTotal + '%;');
       var hidden = $('#progress').is(":hidden");
       if(percentTotal >= 100 || hidden) {
            $('#progress').attr('style', 'width:0%;');
            window.clearInterval(intervalId);
            playNext();
       }
   }, millisInterval);
}

/**
 * Updates the play toolbar
 */
function refreshUI(data, triggerProgress) {
    if(data && data.activeSong) {
        //update actions
        $('#play-icon').attr('src','img/pause.png');
        $('#play-action').attr('onclick', 'pause()');

        //update UI
        var src = data.artUrl;
        $('#player-cover').show();
        $('#player-cover').attr('src', src);
        $('#player-title-label').html(data.activeSong.name);
        $('#player-album-label').html('<a href="#" class="album-data" style="font-size:20px;font-weight:bold;" onclick="artistAlbums(\'' + data.mid + '\')">' + data.artist + '</a>' + ' - ' + data.name);

        if(triggerProgress) {
            triggerProgressBar(data.activeSong.durationMillis);
        }
    }
    else {
        $('#progressbar').hide();
        $('#player-cover').hide();
        $('#player-cover').attr('src', '');
        $('#player-title-label').html('');
        $('#player-album-label').html('');
        $('#progress').attr('style', 'width:0%;');
    }
}

/**
 * Event listener for the volume slider
 */
function applyVolumeSliderListener() {
	$('.ui-slider').on('mouseup', function() {
		$.mobile.loading('show');
		var slider_value = $("#volume-slider").val();
		$.getJSON('/rest/player/volume/set/' + slider_value, function(data) {
			$.mobile.loading('hide');
		});
	});

	$.getJSON('/rest/player/volume/get', function(data) {
        $('#volume-slider').val(data).slider("refresh");
	});
}





