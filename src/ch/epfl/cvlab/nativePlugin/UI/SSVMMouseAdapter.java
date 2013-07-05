package ch.epfl.cvlab.nativePlugin.UI;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

import ch.epfl.cvlab.core.AlgorithmSubject;
import ch.epfl.cvlab.core.AlgorithmWorker;

public class SSVMMouseAdapter extends MouseAdapter {
   
    URI binary;
    AlgorithmSubject subject;
    AlgorithmWorker algorithmWorker;
    String[] extraArgs = {};
    
    SSVMMouseAdapter(AlgorithmSubject subject, URI binary, AlgorithmWorker algorithmWorker){
        
        this.subject = subject;
        this.binary = binary;
        this.algorithmWorker = algorithmWorker;
    }
    
    SSVMMouseAdapter(AlgorithmSubject subject, URI binary, AlgorithmWorker algorithmWorker, String[] extraArgs){
        
        this.subject = subject;
        this.binary = binary;
        this.extraArgs = extraArgs;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
      
      if(algorithmWorker.isDone()){
          algorithmWorker = new AlgorithmWorker(subject);
      }
      subject.setSelectedBinary(binary);
      algorithmWorker.setExtraArgs(extraArgs);
      algorithmWorker.execute();
    }
}
