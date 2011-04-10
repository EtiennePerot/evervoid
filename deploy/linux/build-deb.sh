#!/bin/bash

resexclude='/\.svn'

echo "Building everVoid."
IFS="`echo -en "\n\b"`"
scriptdir="`dirname "$0"`"
orig="`cd "$scriptdir"; pwd`"
echo "Generating a JAR package first."
bash "$orig/../jar/build-jar.sh"
echo "Building Debian package."
src="$orig/../../"
builddir="$HOME/.evervoid-builddeb/"
arch="`dpkg --print-architecture`"
timestamp="`date "+%a, %d %b %Y %H:%m:%S %Z"`"
version="`cat "$src/version.txt"`"
package="$HOME/evervoid_$version-1_$arch.deb"
rm -rf "$builddir" "$package" &> /dev/null
mkdir "$builddir"
cp "$orig/debian-binary" "$builddir/"
echo "Creating control files."
mkdir "$builddir/control"
echo "evervoid ($version) unstable; urgency=low

  * See http://evervoid.biringa.com/

 -- everVoid <deploy@evervoid.biringa.com> $timestamp">"$builddir/control/changelog.Debian"
echo "Package: evervoid
Version: $version-1
Architecture: $arch
Depends: openjdk-6-jre
Maintainer: everVoid <deploy@evervoid.biringa.com>
Section: games
Priority: standard
Homepage: http://evervoid.biringa.com/
Description: A turn-based strategy game in space.
 everVoid is a turn-based strategy game set in space. Conquer the galaxy!">"$builddir/control/control"
cp "$orig/copyright" "$builddir/control/"
cp "$orig/dirs" "$builddir/control/"
cp "$orig/rules" "$builddir/control/"
echo "Creating data files."
mkdir "$builddir/data"
mkdir -p "$builddir/data/usr/games"
cp "$orig/everVoid" "$builddir/data/usr/games/"
mkdir -p "$builddir/data/usr/share/applications"
cp "$orig/everVoid.desktop" "$builddir/data/usr/share/applications/"
mkdir -p "$builddir/data/usr/share/games/everVoid"
cp "$HOME/everVoid.jar" "$builddir/data/usr/share/games/everVoid/"
mkdir -p "$builddir/data/usr/share/games/everVoid/res"
chmod +x "$builddir/data/usr/games/everVoid" "$builddir/data/usr/share/applications/everVoid.desktop" "$builddir/data/usr/share/games/everVoid/everVoid.jar"
pushd "$src/res" &>/dev/null
for f in `find . | grep -vE "$resexclude|^\.$"`; do
	if [ -d "$f" ]; then
		mkdir "$builddir/data/usr/share/games/everVoid/res/$f"
	else
		cp "$f" "$builddir/data/usr/share/games/everVoid/res/$f"
	fi
done
popd &>/dev/null
echo "Creating control archive."
pushd "$builddir/control" &>/dev/null
tar pczf "$builddir/control.tar.gz" *
popd &>/dev/null
rm -rf  "$builddir/control"
echo "Creating data archive."
pushd "$builddir/data" &>/dev/null
tar pczf "$builddir/data.tar.gz" *
popd &>/dev/null
rm -rf  "$builddir/data"
echo "Creating .deb archive."
pushd "$builddir" &>/dev/null
ar rcs "$package" "debian-binary" "control.tar.gz" "data.tar.gz"
popd &>/dev/null
rm -rf  "$builddir"
echo "All done, package is available at $package"
