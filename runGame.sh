#!/bin/sh
#/usr/bin/open -a "/Applications/Google Chrome.app" 'http://chrome.angrybirds.com'
cd abV1.32
ant compile && ant jar

cd ..
osascript -e 'tell app "Terminal"
	do script "cd /Users/felix/Documents/git/aiBirds/abV1.32
	java -jar ABServer.jar"
end tell'

sleep 2

osascript -e 'tell app "Terminal"
	do script "cd /Users/felix/Documents/git/aiBirds/abV1.32
	java -jar ABSoftware.jar -nasc"
end tell'
