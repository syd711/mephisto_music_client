function initSettings() {
     $.getJSON('/rest/settings/get', function(data) {
        var html='';
        for(i=0; i<data.providers.length; i++) {
            var provider = data.providers[i];
            html+='<input onchange="enableProvider(\'' + provider.name + '\',' + provider.id + ')" type="checkbox" name="' + provider.name + '" id="provider-' + provider.id + '" class="custom" data-theme="c" ';
            if(provider.enabled) {
                html+='checked';
            }
            html+='/>';
            html+='<label for="provider-' + provider.id + '">' + provider.name + '</label>';
        }
        $('#providers').empty();
        $('#providers').append(html).trigger('create');

        html = '';
        html+='<div class="settings-value">Songs: ' + data.songs + '</div>';
        html+='<div class="settings-value">Albums: ' + data.albums + '</div>';

        $('#statistics').empty();
        $('#statistics').append(html).trigger('create');
     });
}

/**
 * Enables or disables a music provider.
 */
function enableProvider(name, id) {
   var enable = $('#provider-' + id).is(':checked');
   if(enable) {
    loading('Enabling Music Provider "' + name + '"');
   }
   else {
    loading('Disabling Music Provider "' + name + '"');
   }
   $.getJSON('/rest/settings/provider/' + id + '/enable/' + enable, function(data) {
       initSettings();
       loaded();
       albums();
   });
}

/**
 * Trigger a reload of all music providers
 */
function reloadProviders() {
    loading('Reloading Music Library');
    $.getJSON('/rest/settings/providers/reload', function(data) {
        loaded();
        albums();
    });
}

/**
 * Trigger a reload of all music providers
 */
function detectProviders() {
    loading('Checking Removable Devices');
    $.getJSON('/rest/settings/providers/detect', function(data) {
        loaded();
    });
}
