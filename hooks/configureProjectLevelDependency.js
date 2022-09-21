const fs = require("fs");
const path = require("path");
const extract = require('extract-zip')

async function addProjectLevelDependency(platformRoot) {

    const projectSettingsFile = path.join(platformRoot, "settings.gradle");

    console.log("settings.gradle --- " + projectSettingsFile);
    let fileContents = fs.readFileSync(projectSettingsFile, "utf8");
    idx = fileContents.indexOf('include ":app"');
    if (idx >= 0) {

        let replacement = 'include ":app"\n\n';
        replacement += "include ':sceneform'\n";
        replacement += "project(':sceneform').projectDir=new File('sceneformsrc/sceneform')\n\n";
        replacement += "include ':sceneformux'\n";
        replacement += "project(':sceneformux').projectDir=new File('sceneformux/ux')"; 

        fileContents = fileContents.replace('include ":app"', replacement);
        fs.writeFileSync(projectSettingsFile, fileContents, "utf8");

        console.log("updated " + projectSettingsFile + " to include dependency AR dependencies");
    } else {
        console.error("unable to insert AR dependency");
    }

    try {
        await extract(platformRoot + "/arcore-sdk-1.16.zip", { dir: platformRoot })
        console.log('Extraction of arcore-sdk-1.16.zip completed')
      } catch (err) {
        console.log("Error on extract arcore-sdk-1.16.zip");
      }
}

module.exports = context => {
    "use strict";
    const platformRoot = path.join(context.opts.projectRoot, "platforms/android");

    return new Promise((resolve, reject) => {
        addProjectLevelDependency(platformRoot);
        resolve();
    });
};