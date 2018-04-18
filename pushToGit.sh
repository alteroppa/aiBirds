#!/bin/sh
git add *
git commit -m "recreate levels"
git push

rm -rf /Users/felix/Library/Application\ Support/Google/Chrome/Default/Application\ Cache
cp -a Application\ Cache /Users/felix/Library/Application\ Support/Google/Chrome/Default/Application\ Cache