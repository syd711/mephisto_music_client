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