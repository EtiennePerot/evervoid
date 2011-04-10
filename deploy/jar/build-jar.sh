#!/bin/bash

exclude='/\.svn|/\.settings|/\.project|/javadoc|/deploy|/res/'

echo "Building everVoid jar."
IFS="`echo -en "\n\b"`"
scriptdir="`dirname "$0"`"
orig="`cd "$scriptdir"; pwd`"
orig="/Users/vbonnet/workspaces/eclipse/everVoid/deploy/jar/"
src="$orig/../../"
builddir="$HOME/.evervoid-build/"
echo "Cleaning environment"
rm -rf "$builddir"
mkdir "$builddir"
pushd "$src" &>/dev/null
echo "Copying source code."
for f in `find . | grep -vE "$exclude|^\.$"`; do
	if [ -d "$f" ]; then
		mkdir "$builddir/$f"
	else
		cp "$f" "$builddir/$f"
	fi
done
popd &>/dev/null
mv "$builddir/"*.jar "$builddir/lib/"*.jar "$builddir/src/"
rm -rf "$builddir/lib"
mkdir "$builddir/build"
echo "Compiling."
javac -classpath "`find "$builddir/src" | grep -E '\.jar$' | tr '\n' ':'`" `find "$builddir/src/" | grep -E '\.java$'` -d "$builddir/build"
echo "Building inner JAR package."
mkdir "$builddir/build/main"
pushd "$builddir/build" &>/dev/null
jar c0mf "$orig/MANIFEST-INNER.MF" "$builddir/build/main/main.jar" com
popd &>/dev/null
rm -rf "$builddir/build/com"
echo "Building outer JAR package."
mkdir "$builddir/build/lib"
cp "$builddir/src/"*.jar "$builddir/build/lib/"
mkdir "$builddir/build/doc"
cp "$orig/one-jar/one-jar-license.txt" "$builddir/build/doc/"
cp -r "$orig/one-jar/com" "$builddir/build/com"
pushd "$builddir/build" &>/dev/null
jar cmf "$orig/MANIFEST-OUTER.MF" "$builddir/everVoid.jar" *
popd &>/dev/null
echo "Cleaning build environment."
mv "$builddir/everVoid.jar" "$HOME/everVoid.jar"
rm -rf "$builddir"
echo "JAR build complete. JAR file is available at $HOME/everVoid.jar"
