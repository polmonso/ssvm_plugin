cd src

javac -cp C:\Users\monso\code\fiji64\Fiji.app\jars\*;.;lib -d ../bin ch\epfl\cvlab\main\SSVM.java

cd ..

jar -cvf finalPlugin\SSVM_Plugin.jar plugins.config -C bin ch -C bin train.exe -C bin predict.exe -C bin cudart64_50_35.dll -C bin npp64_50_35.dll 
