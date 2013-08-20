/**
 * The active song id for highlighting the playlist element
 */
activeTrackId = -1;
activeCollectionId = -1;
paused = false;

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
     }).error(showErrorState);

     loadActivePlaylist(function(playlist) {
        if(playlist) {
            if(playlist.errorState) {
                showErrorState(playlist.errorState, playlist.errorHint);
            }
            else {
                setActiveCollectionId(playlist.mid);
                showCollection(playlist.mid, function() {
                    refreshUI(playlist, false);
                });
            }

        }
        else {
            applyInitialView();
        }
     });
 }

/**
 * Retrieves the active playlist from the player.
 */
function loadActivePlaylist(callback) {
    $.getJSON('/rest/player/playlist', function(data) {
        callback(data);
    }).error(showErrorState);
}

/**
 * Invoked for setting changes.
 */
function stopPlayer() {
    activeTrackId = -1;
    activeCollectionId = -1;
    paused = false;
    refreshUI(null, false);
}

/**
 * The toolbar play action
 */
function playPressed() {
    if(paused) {
        playSong(getActiveTrackId());
    }
    else {
        playCollection();
    }
}


/**
 * The playlist play action
 */
function playCollection() {
    var collectionId = getCollectionId();
    $.getJSON('/rest/player/play/' + collectionId, function(data) {
        refreshUI(data, true);
    }).error(showErrorState);
}

function playSong(id) {
    var triggerProgress = !paused;
    setPaused(false);
    var collectionId = getActiveCollectionId();
    $.getJSON('/rest/player/playsong/' + collectionId + '/' + id, function(data) {
        refreshUI(data, triggerProgress);
    }).error(showErrorState);
}

function pause() {
    setPaused(true);
    $.getJSON('/rest/player/pause', function(data) {
        if(data) {
            $('#play-icon').attr('src','img/pplay.png');
            $('#play-action').attr('onclick', 'playPressed()');
        }
    }).error(showErrorState);
}

function playPrevious() {
    setPaused(false);
    $.getJSON('/rest/player/previous', function(data) {
        if(data.activeSong) {
            selectTrack(data.activeSong.mid);
            playSong(data.activeSong.mid);
        }
    }).error(showErrorState);
}

function playNext() {
    setPaused(false);
    $.getJSON('/rest/player/next', function(data) {
        if(data.activeSong) {
            selectTrack(data.activeSong.mid);
            playSong(data.activeSong.mid);
        }
        else {
            refreshUI(data, false);
        }
    }).error(showErrorState);
}

function setActiveTrackId(mid) {
    activeTrackId = mid;
}

function getActiveTrackId() {
    return activeTrackId;
}

function setActiveCollectionId(mid) {
    activeCollectionId = mid;
}

function getActiveCollectionId() {
    return activeCollectionId;
}

function setPaused(p) {
    paused = p;
    if(paused) {
        $('#column-track-' + getActiveTrackId()).html('<span class="paused-icon"/>');
    }
//    else {
//        $('#column-track-' + getActiveTrackId()).html('<span class="playing-icon"/>');
//    }

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
       if(!paused) {
        percentTotal+=1;
       }
       $('#progress').attr('style', 'width:' + percentTotal + '%;');
       var hidden = false; //$('#progress').is(":hidden");
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
    //check error state first
    if(data && data.errorState) {
        showErrorState(data.errorState, playlist.errorHint);
        return;
    }

    if(data && data.activeSong) {
        selectTrack(data.activeSong.mid);
        $('#column-track-' + data.activeSong.mid).html('<span class="playing-icon"/>');

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
            $('#progress').attr('style', 'width:0%;');
            triggerProgressBar(data.activeSong.durationMillis);
        }
    }
    else {
      resetPlayer();
    }
}

/**
 * Resets all UI elements of the player bar.
 */
function resetPlayer() {
    selectTrack(-1);
    $('#progressbar').hide();
    //$('#player-cover').hide();
    //$('#player-cover').attr('src', '');
    $('#player-title-label').html('');
    //$('#player-album-label').html('');
    $('#play-icon').attr('src','img/pplay.png');
    $('#play-action').attr('onclick', 'playPressed()');
    $('#progress').attr('style', 'width:0%;');
}

/**
 * Event listener for the volume slider
 */
function applyVolumeSliderListener() {
	$('.ui-slider').on('mouseup', function() {
		var slider_value = $("#volume-slider").val();
		$.getJSON('/rest/player/volume/set/' + slider_value, function(data) {
			//nothing
		}).error(showErrorState);
	});

	$.getJSON('/rest/player/volume/get', function(data) {
        $('#volume-slider').val(data).slider("refresh");
	}).error(showErrorState);
}





