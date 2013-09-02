/**
 * The active song id for highlighting the playlist element
 */
activeTrackId = -1;
activeCollectionId = -1;
playerPaused = false;

/**
 * Player functions
 ****************************************
 */
 var intervalId;
 function initPlayer() {
     $('#progressbar').hide();
     $('#player-cover').hide();
     loadActivePlaylist(function(playlist) {
        if(playlist) {
            if(playlist.errorState) {
                showErrorState(playlist.errorState, playlist.errorHint);
            }
            else {
                setActiveCollectionId(playlist.mid);
                showCollection(playlist.mid, function() {
                    paused(function(paused) {
                        refreshUI(playlist, false);
                        setPaused(paused);
                    });
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
    refreshUI(null, false);
}

/**
 * The toolbar play action
 */
function playPressed() {
    paused(function(paused) {
        if(paused) {
           playSong(getActiveTrackId());
        }
        else {
           playCollection();
        }
    });
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

/**
 * Plays the song for the given mid.
 */
function playSong(id) {
    var collectionId = getActiveCollectionId();
    $.getJSON('/rest/player/playsong/' + collectionId + '/' + id, function(data) {
        setPaused(false);
        refreshUI(data, true);
    }).error(showErrorState);
}

function pause() {
    $.getJSON('/rest/player/pause', function(data) {
        setPaused(true);
        if(data) {
            $('#play-icon').attr('src','img/pplay.png');
            $('#play-action').attr('onclick', 'playPressed()');
        }
    }).error(showErrorState);
}

/**
 * Checks if the player is currently paused.
 */
function paused(callback) {
    $.getJSON('/rest/player/paused', function(data) {
       callback(data);
    }).error(showErrorState);
}

function playPrevious() {
    $.getJSON('/rest/player/previous', function(data) {
        if(data.activeSong) {
            selectTrack(data.activeSong.mid);
            playSong(data.activeSong.mid);
        }
    }).error(showErrorState);
}

function playNext() {
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
    playerPaused = p;
    if(p) {
        $('#column-track-' + getActiveTrackId()).html('<span class="paused-icon"/>');
    }
//    else {
//        $('#column-track-' + getActiveTrackId()).html('<span class="playing-icon"/>');
//    }

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
        $('#player-cover-link').attr('onclick', 'showCollection(' + data.mid + ')');
        $('#player-title-label').html(data.activeSong.name);
        $('#player-album-label').html('<a href="#" class="album-data" style="font-size:20px;font-weight:bold;" onclick="artistAlbums(\'' + data.mid + '\')">' + data.artist + '</a>' + ' - ' + data.name);

        if(triggerProgress) {
            $('#progress').attr('style', 'width:0%;');
            triggerProgressBar(data.activeSong.durationMillis, data.activeSong.mid);
        }
    }
    else {
      resetPlayer();
    }
}


/**
 * Trigger the progressbar with the total millis of the active song.
 */
function triggerProgressBar(millis, triggerId) {
    var currentTriggerId = $('#progressbar').attr('data-triggerId');
    if(currentTriggerId == triggerId) {
        return;
    }
    $('#progressbar').attr('data-triggerId', triggerId);
    window.clearInterval(intervalId);
    $('#progressbar').show();
    var percentTotal = 0;
    var millisInterval = (millis/100)
    intervalId = window.setInterval(function() {
       if(!playerPaused) {
        percentTotal+=1;
       }
       $('#progress').attr('style', 'width:' + percentTotal + '%;');
       var hidden = false; //$('#progress').is(":hidden");
       if(percentTotal >= 100 || hidden) {
            $('#progress').attr('style', 'width:0%;');
            window.clearInterval(intervalId);
            if(!isStreamMode()) {
                playNext();
            }
       }
   }, millisInterval);
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





