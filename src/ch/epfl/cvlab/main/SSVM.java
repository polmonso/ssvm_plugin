package ch.epfl.cvlab.main;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.zip.ZipException;

import ch.epfl.cvlab.core.AlgorithmSubject;
import ch.epfl.cvlab.nativePlugin.UI.SwingGUI;
import ij.IJ;
import ij.plugin.PlugIn;

public class SSVM implements PlugIn {
  
  private SwingGUI gui;
  private AlgorithmSubject subject;


  public SSVM(){
    subject = new AlgorithmSubject(null);
    gui = new SwingGUI(subject);
  }

  public void run(String ignored) {

    try {
      
        subject.loadBinaries();
        subject.addObserver(gui);
        System.out.println(subject.countObservers() + " observers added");
        subject.notifyObservers();
        gui.setVisible(true);
        
        //TODO handle defaultInit exceptions particularly      
    } catch (Exception exp) {
        exp.printStackTrace();
        IJ.error(exp.getMessage());
        return;
    }      
    
  }
    
  /**
   * @param args
   */
  public static void main(String[] args) {
    
    SSVM ssvm = new SSVM();
    ssvm.run(null);

  }

}
