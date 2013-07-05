package ch.epfl.cvlab.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.concurrent.CancellationException;

import javax.swing.SwingWorker;

import ch.epfl.cvlab.libraryLoader.SystemCalls;
import ch.epfl.cvlab.nativePlugin.UI.Utils;

import java.util.*;
import java.io.*;

public class AlgorithmWorker extends SwingWorker<Integer, String> {
  
  AlgorithmSubject subject;
  private String[] extraArgs = {};
  
  private class StreamGobbler extends Thread
  {
//      InputStream is;
//      String type;
//      
//      StreamGobbler(InputStream is, String type)
//      {
//          this.is = is;
//          this.type = type;
//      }
//      
//      public void run()
//      {
//          try
//          {
//              InputStreamReader isr = new InputStreamReader(is);
//              BufferedReader br = new BufferedReader(isr);
//              String line=null;
//              while ( (line = br.readLine()) != null)
//                  System.out.println(type + ">" + line);    
//              } catch (IOException ioe)
//                {
//                  ioe.printStackTrace();  
//                }
//      }
  }
  
  public AlgorithmWorker(AlgorithmSubject subject){
    this.subject = subject;
  }
  
  //TODO implement running several AlgorithmWorkers
  public AlgorithmWorker(AlgorithmSubject subject, String[] args){
    this(subject);
    this.extraArgs = args;
  }
  
  @Override
  protected Integer doInBackground() throws Exception {
    //TODO error here getSelectedBinary returns null
    System.out.println("[Worker " + subject.getSelectedBinary() + "] executing binary with config: " + subject.getConfig());
    String binName = new File(subject.getSelectedBinary()).getName();
    System.out.println("[Worker " + binName + "] executing binary with " + subject.getConfig().getPath());
    Process p = SystemCalls.executeCommand(subject.getSelectedBinary(), new File(subject.getConfig()).getCanonicalPath(), extraArgs);
  //TODO read error stream as well (return process for example)
    
    // print output and error messages
    //StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "[" + binName + "] " + "ERROR:");            
    //StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "[" + binName + "] " + "OUTPUT:");
    //errorGobbler.start();
    //outputGobbler.start();

    BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
//    BufferedReader is = new BufferedReader(new InputStreamReader(p.getErrorStream()));
    
    int progress = 0;
    String line;
    while((line = is.readLine()) != null){
      //TODO read error input stream as well
      subject.log("[Worker " + binName + "] " + line);
      //TODO implement proper progress by reading input stream data
      progress++;
      if(progress % 1 == 0)
          setProgress(progress);
    }
    System.out.println("[Worker " + binName + "] Done. Model list: " + Utils.listModels().toString());
    
    subject.setModelList(new ArrayList<URI>(Utils.listModels()));
        
    return 0;
  }

  @Override
  protected void done(){
    try{
      int result = get();
      subject.log("Result: " + result);
    } catch (final CancellationException e) {
      subject.log("The process was cancelled");
    } catch (final Exception e) {
      subject.log("The process failed");
    }
  }

  public void setExtraArgs(String[] extraArgs) {
    this.extraArgs = extraArgs;    
  }
  
}
