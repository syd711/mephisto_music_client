/**
 * Loads the album to a responsive layout
 */
function streams() {
    hidePlaylistView();
    hideCollectionView();
    setNameAndTitle('Streams', '');
    $.getJSON('/rest/streams/all', function(data) {
        showStreams(data);
    });
}

function showStreams(data) {
    var html = '<ul data-role="listview" data-split-icon="gear" data-split-theme="c" data-inset="true">';
    $.each(data, function(key, value) {
        var url = value.url;
        var artist = value.name || '&nbsp;';
        html+='<li><a href="#">';
        html+=' <h1>Wolfgang Amadeus Phoenix</h1>';
        html+=' <p>Phoenix</p></a>';
        html+=' <a href="#purchase" data-rel="popup" data-position-to="window" data-transition="pop">Purchase album</a>';
        html+='</li>';
    });
    html+='</ul>';
    html+='  <div data-role="popup" id="purchase" data-theme="c" data-overlay-theme="b" class="ui-content" style="max-width:340px; padding-bottom:2em;">';
    html+='     <h3>Purchase Album?</h3>';
    html+='     <p>Your download will begin immediately on your mobile device when you purchase.</p>';
    html+='     <a href="index.html" data-role="button" data-rel="back" data-theme="b" data-icon="check" data-inline="true" data-mini="true">Buy: $10.99</a>';
    html+='     <a href="index.html" data-role="button" data-rel="back" data-inline="true" data-mini="true">Cancel</a>';
    html+='  </div>';

    $('#collection').empty();
    $('#collection').append(html).trigger('create');
    showCollectionView();
}