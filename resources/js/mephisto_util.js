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
    $('#errorState').hide();
}

function showErrorState(msg, hint) {
    enableControls(false);
    setNameAndTitle('System Error', '');
    $('#errorState').show();
    $('#errorState').html('<span class="error-msg">Error executing system call: ' + msg + '</span><br><span class="error-hint">' + hint + '</span>');
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