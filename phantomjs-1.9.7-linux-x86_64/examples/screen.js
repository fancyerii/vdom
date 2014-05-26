var page = require('webpage').create(),
    system = require('system');

if (system.args.length < 2) {
    console.log('Usage: loadurlwithoutcss.js URL');
    phantom.exit();
}

var address = system.args[1];


page.open(address, function(status) {
    if (status === 'success') {
        page.render('screen.png');
        phantom.exit();
    } else {
        console.log('Unable to load the address!');
        phantom.exit();
    }
});
