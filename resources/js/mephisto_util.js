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