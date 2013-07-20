/**
 * Player functions
 ****************************************
 */

/**
 * The toolbar play action
 */
function playPressed() {
    var id = 0;
    var midEl = $('#collection-mid');
    if(midEl) {
        id = midEl.attr('data-mid');
    }
    play(id);
}


/**
 * The playlist play action
 */
function play(id) {
    $.getJSON('/rest/player/play/' + id, function(data) {
        if(data) {
            //update actions
            $('#play-icon').attr('src','img/pause.png');
            $('#play-action').attr('onclick', 'pause()');

            //update UI
            var src = data.artUrl;
            triggerProgressBar(data.activeSong.durationMillis);
            $('#player-cover').show();
            $('#player-cover').attr('src', src);
            $('#player-title-label').html(data.activeSong.name);
            $('#player-album-label').html(data.artist + ' - ' + data.name);
        }
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

function triggerProgressBar(millis) {
    window.clearInterval(true);
    $('#progressbar').show();
    var count = 0;
    window.setInterval(function() {
       count++;
       $('#progress').attr('style', 'width:' + count + '%;');
   }, 1000);
}



