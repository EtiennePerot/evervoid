import os
import shutil
import sys

appname = "everVoid"

dependencies = ["Info.plist", "PkgInfo", "everVoid", "icon.icns", "everVoid.jar"];

fs = os.path.normcase('/')
appdir = appname + ".app" + fs + "Contents" + fs


# check that all dependencies exist
for file in dependencies:
  if not os.path.exists(file):
    print "dependencies were incorrect"
    sys.exit()

# if the .app already exists, we don't want to build a new one
if os.path.exists(appname + ".app"):
  print "old app file exists, removing"
  shutil.rmtree(appname + ".app")

# create .app folder
print "creating .app"
os.makedirs(appdir)

# copy the Info.plist and PkgInfo
shutil.copyfile("Info.plist", appdir + fs + "Info.plist")
shutil.copyfile("PkgInfo", appdir + fs + "PkgInfo")

# make the Resources dir then move icon.icns into it
resourceDir = appdir + fs + "Resources"
os.makedirs(resourceDir)
shutil.copyfile("icon.icns", resourceDir + fs + "icon.icns")

# make the MacOS dir and move the executable into it
macDir = appdir + fs + "MacOS"
os.makedirs(macDir)
executable = macDir + fs + "everVoid"
shutil.copyfile("everVoid", executable)
os.chmod(executable, 511)

# make the java dir
javaDir = resourceDir+ fs + "Java"
os.makedirs(javaDir)
shutil.copy("everVoid.jar", javaDir + fs + "everVoid.jar")

# success
print "build succeeded"

