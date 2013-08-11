/**
 * Shows the busy indicator.
 */
function loading(msg) {
    $.mobile.loading( 'show', {
		text: msg,
		textVisible: true,
		theme: 'a',
		html: ""
	});
}

function loaded() {
    $.mobile.loading("hide");
}

function getCollectionId() {
    var id = 0;
    var midEl = $('#collection-mid');
    if(midEl) {
        id = midEl.attr('data-mid');
    }
    return id;
}


function hideErrorState() {
    $('#footer-bar').show();
    $('#search-bar').show();
    $('#errorState').hide();
}

function showErrorState(jqXHR, textStatus, errorThrown) {
    enableControls(false);
    setNameAndTitle('System Error', '');
    $('#errorState').show();
    $('#errorState').html('<span class="error-msg">HTTP Error Code: ' + errorThrown + '</span><br><span class="error-hint">' + jqXHR.responseText + '</span>');
}

/**
 * Hides input components during system calls and errors
 */
function enableControls(enable) {
    if(enable) {
        $('#footer-bar').show();
        $('#search-bar').show();
    }
    else {
        hidePlaylistView();
        hideCollectionView();
        $('#footer-bar').hide();
        resetPlayer();
        $('#search-bar').hide();
    }
}