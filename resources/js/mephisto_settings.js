/**
 * Global Settings
 */
$.mobile.page.prototype.options.domCache = false;
$.ajaxSetup({ cache: false });
STREAM_MODE = "1";
MUSIC_MODE = "0";
playbackMode = MUSIC_MODE;

/**
 * Loads the provider info and the statistic
 */
function initSettings() {
     $.getJSON('/rest/settings/get', function(data) {
        var html='';
        for(i=0; i<data.providers.length; i++) {
            var provider = data.providers[i];
            html+='<input onchange="enableProvider(\'' + provider.name + '\',' + provider.id + ')" type="checkbox" name="' + provider.name + '" id="provider-' + provider.id + '" class="custom" data-theme="c" ';
            if(provider.enabled) {
                html+=' checked';
            }
            if(!provider.connected) {
                html+=' disabled';
            }
            html+='/>';
            html+='<label for="provider-' + provider.id + '">' + provider.name + '</label>';
        }
        $('#providers').empty();
        $('#providers').append(html).trigger('create');

        html = '';
        html+='<div class="settings-value">Songs: ' + data.songs + '</div>';
        html+='<div class="settings-value">Albums: ' + data.albums + '</div>';
        html+='<div class="settings-value">Streams: ' + data.streams + '</div>';

        $('#statistics').empty();
        $('#statistics').append(html).trigger('create');

        var state = localStorage.getItem("mephisto-play-mode");
        if(state === STREAM_MODE) {
            $("#playback-streams").attr("checked",true).checkboxradio("refresh");
        }
        else {
            $("#playback-music").attr("checked",true).checkboxradio("refresh");
        }
     }).error(showErrorState);
}


/**
 * Depending on the stored state, the play is switched to stream mode or player mode.
 */
function applyInitialView() {
    var state = localStorage.getItem("mephisto-play-mode") || MUSIC_MODE;
    switchToState(state);
}

/**
 * Toggles the playback mode of the app.
 */
function switchToState(state) {
    playbackMode = state;
    stopPlayer();

    if(state == MUSIC_MODE) {
        loading('Switching to Music Mode...');
        $('#music-settings-section').show();
        enableControls(true);
        albums();
    }
    else if(state == STREAM_MODE) {
        loading('Switching to Stream Mode...');
        $('#music-settings-section').hide();
        enableControls(false);
        streams();
    }
    localStorage.setItem("mephisto-play-mode", state);

    initSettings();
}

/**
 * Returns true if the modus is stream.
 */
function isStreamMode() {
    return playbackMode == STREAM_MODE;
}

/**
 * Enables or disables a music provider.
 */
function enableProvider(name, id) {
    stopPlayer();
    var enable = $('#provider-' + id).is(':checked');
    if(enable) {
        loading('Enabling Music Provider "' + name + '"');
    }
    else {
        loading('Disabling Music Provider "' + name + '"');
    }
    enableControls(false);
    $.getJSON('/rest/settings/provider/' + id + '/enable/' + enable, function(data) {
        enableControls(true);
        initSettings();
        albums();
   });
}

/**
 * Trigger a reload of all music providers
 */
function reloadProviders() {
    loading('Reloading Music Library');
    stopPlayer();
    enableControls(false);
    setNameAndTitle('Reloading Music Library...', '');
    $.getJSON('/rest/settings/providers/reload', function(data) {
        enableControls(true);
        loaded();
        initSettings();
        albums();
    }).error(showErrorState);
}

/**
 * Trigger a reload of all music providers
 */
function detectProviders() {
    loading('Checking Removable Devices');
    $.getJSON('/rest/settings/providers/detect', function(data) {
        loaded();
        initSettings();
        if(data) {
            enableControls(false);
            enableControls(true);
            albums();
        }
    }).error(showErrorState);
}

