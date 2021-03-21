const path = require('path');
const fs = require('fs');
const util = require('util');

try {

	const appVersion = require('./package.json').version;
	
	const writeVersionFile = util.promisify(fs.writeFile);
	
	const versionFilePath = path.join(__dirname + '/dist/version.txt');
	const src = `${appVersion}`;

	writeVersionFile(versionFilePath, src);
	
	console.log("Done.\n");

} catch(error) {
	console.log("Failed while preparing application version file.");
	console.log(error);
}