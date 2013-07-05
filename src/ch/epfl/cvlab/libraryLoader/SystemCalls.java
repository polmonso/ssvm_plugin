package ch.epfl.cvlab.libraryLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.ZipException;

import ch.epfl.cvlab.nativePlugin.UI.Utils;

public class SystemCalls {

    private ArrayList<URI> executables;
      
    public SystemCalls(String[] binariesList){
      
      try {
        executables = BinaryExtractor.extractBinaries(binariesList);
      } catch (ZipException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (URISyntaxException e) {  
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
    }
    
    public URI getExecutableURI(int index){
      return executables.get(index);
    }
     
    public static Process executeCommand(URI executable, String configFilenamePath, String modelFilenamePath) throws IOException{
      if(modelFilenamePath == null){
        return executeCommand(executable, configFilenamePath);
      }
      String[] cmd = {new File(executable).getAbsolutePath(), "-c", configFilenamePath, "-w", modelFilenamePath};
      return executeCommand(cmd);      
      
    }
    
    public static Process executeCommand(URI executable, String configFilenamePath) throws IOException{
      
      String cmd[] = {new File(executable).getAbsolutePath() + " " + configFilenamePath};
      return executeCommand(cmd);
      
    }
    
    public static Process executeCommand(URI executable, String configFilenamePath, String[] extraArgs) throws IOException{
      
      String[] cmd = {new File(executable).getAbsolutePath(), configFilenamePath};
      cmd = Utils.concat(cmd, extraArgs);
      return executeCommand(cmd);
      
    }
    
    private static Process executeCommand(String[] cmd) throws IOException{
            
      System.out.println("Executing process with command array: " + Arrays.toString(cmd));    
      Process p = Runtime.getRuntime().exec(cmd); //cmd will be tokenized
     
      return p;
      
  }
    
    
    
}
