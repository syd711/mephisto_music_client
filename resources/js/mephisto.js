/**
 * Common page loading function
 */
function showAlbums() {
	$('#content').load('albums.html', function(){
	});
}
function showInterprets() {
	$('#content').load('interprets.html', function(){
	});
}
function showTitles() {
	$('#content').load('titles.html', function(){
	});
}
function showGenres() {
	$('#content').load('genres.html', function(){
	});
}