package ch.epfl.cvlab.core;

import java.io.BufferedReader;
import java.io.File;
import java.net.URI;
import java.util.concurrent.CancellationException;

import javax.swing.SwingWorker;

import ch.epfl.cvlab.libraryLoader.SystemCalls;
import ch.epfl.cvlab.nativePlugin.UI.Utils;

public class AlgorithmWorker extends SwingWorker<Integer, String> {
  
  AlgorithmSubject subject;
  private String[] extraArgs = {};
  
  
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
    BufferedReader is = SystemCalls.executeCommand(subject.getSelectedBinary(), new File(subject.getConfig()).getCanonicalPath(), extraArgs);
    String line;
    while((line = is.readLine()) != null){
      System.out.println("[" + binName + "] " + line);
    }
    System.out.println("[CMD Thread] Done. Model list: " + Utils.listModels().toString());
    
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
