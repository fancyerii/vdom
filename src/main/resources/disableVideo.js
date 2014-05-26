phantom.viewportSize = { width: 2000, height: 10000 };

var page = this;
page.settings.resourceTimeout = 10000;
page.onResourceRequested = function(requestData, request) {
	if ((/http:\/\/.+?\.mp4/gi).test(requestData['url'])) {
		console.log('The url of the request is matching. Aborting: ' + requestData['url']); 
		request.abort(); 
	}
	
}; 
//
//page.onError = function(msg, trace) {
//	console.error("page.onError");
//};
//
//page.onResourceTimeout = function(request) {
//    console.log('Response (#' + request.id + '): ' + JSON.stringify(request));
//};
//
//page.onResourceError = function(resourceError) {
//    console.log('Unable to load resource (#' + resourceError.id + 'URL:' + resourceError.url + ')');
//    console.log('Error code: ' + resourceError.errorCode + '. Description: ' + resourceError.errorString);
//};