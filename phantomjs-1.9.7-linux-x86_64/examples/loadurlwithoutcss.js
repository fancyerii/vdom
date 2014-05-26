var page = require('webpage').create(),
    system = require('system');

if (system.args.length < 2) {
    console.log('Usage: loadurlwithoutcss.js URL');
    phantom.exit();
}

var address = system.args[1];

page.onResourceRequested = function(requestData, request) {
    if ((/http:\/\/.+?\.gif/gi).test(requestData['url']) || requestData['Content-Type'] == 'image/png') {
        console.log('The url of the request is matching. Aborting: ' + requestData['url']);
        request.abort();
    }
};

page.open(address, function(status) {
    if (status === 'success') {
        page.render('screen-noimg.png');
        phantom.exit();
    } else {
        console.log('Unable to load the address!');
        phantom.exit();
    }
});
