#!/bin/bash

# create plugin
cd src

javac -cp ~/code/Fiji.app/jars/*:.:lib -d ../bin ch/epfl/cvlab/main/SSVM.java

cd ..

jar -cvf finalPlugin/SSVM_Plugin.jar plugins.config -C bin ch -C executables train -C executables predict 

exit
