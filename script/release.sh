#!/bin/sh

echo "Welcome to the Xing API-Client release script. Please enter the new version name: "
read versionName
if [[ -z "${versionName// }" ]]; then
    echo "Wrong version name. Finishing the script"
    exit
fi

echo "Creating Tag $versionName"
git tag $versionName

echo "Uploading archives"
echo "Please enter Nexus username"
read nexusUsername
echo "Please enter Nexus password"
read nexusPassword

./gradlew clean uploadArchives -PnexusUsername=$nexusUsername -PnexusPassword=$nexusPassword

echo "Generating Javadocs..."
javadoc -d ./javadocs/$versionName -sourcepath api-client/src/main/java -stylesheetfile script/stylesheet.css -subpackages .

echo "Uploading Javadocs"
git fetch upstream
if  ! git checkout gh-pages; then
    git tag --delete $versionName
    exit 1
fi

git add javadocs/$versionName/
git commit -m "Javadocs for version $versionName"
git push
git push origin $versionName
git checkout master

echo "Release done!"
