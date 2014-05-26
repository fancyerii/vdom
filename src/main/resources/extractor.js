var tree = "";
function getStyle(el, styleProp) {
	var camelize = function(str) {
		return str.replace(/\-(\w)/g, function(str, letter) {
			return letter.toUpperCase();
		});
	};

	if (el.currentStyle) {
		return el.currentStyle[camelize(styleProp)];
	} else if (document.defaultView && document.defaultView.getComputedStyle) {
		return document.defaultView.getComputedStyle(el, null)
				.getPropertyValue(styleProp);
	} else {
		return el.style[camelize(styleProp)];
	}
}
function traverse(node, depth) {
	// Get the name of the node
	var line = node.nodeName.toLowerCase();
	if (line == 'script' || line == 'style' || line == '#comment') {
		return;
	}
	for (i = 0; i < depth; i++) {
		tree += '\t';
	}
	if (node.nodeType == 3) {
		tree += "0";
	} else {
		tree += "1";
	}
	if (node.getBoundingClientRect) {
		var rect = node.getBoundingClientRect();
		tree += "[" + rect.left + "," + rect.top + "," + rect.width + ","
				+ rect.height + "]";
	}
	// 影响速度？自己计算？
	else if (node.nodeType == 3) {
		var span = document.createElement('span');
		node.parentNode.insertBefore(span, node);
		span.appendChild(node);
		var rect = span.getBoundingClientRect();
		tree += "[" + rect.left + "," + rect.top + "," + rect.width + ","
				+ rect.height + "]";
		fontSize=getStyle(span, 'font-size');
		tree +="["+fontSize+"]";
	}
	// If it is a #TEXT node
	if (node.nodeType == 3) {
		// Get the text
		var text = "" + node.nodeValue;

		// Clean it up
		text = text.replace(/[\t\r\n]/g, " ");
		// text = text.replace(/ +/g, " ");

		// And add it to the line
		tree += text + "\n";
	} else {
		tree += "<" + line + ">\n";
	}

	if (node.nodeType != 3) {
		// Get an array with the child nodes
		var children = node.childNodes;

		// Traverse each of the child nodes
		for (var i = 0; i < children.length; i++)
			traverse(children[i], depth + 1);
	}

	if (node.nodeType == 3) {

	} else {
		for (i = 0; i < depth; i++) {
			tree += '\t';
		}
		tree += "1</" + line + ">\n";
	}
}

traverse(document.body, 0);
return tree;