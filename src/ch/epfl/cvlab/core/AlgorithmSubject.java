package ch.epfl.cvlab.core;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.zip.ZipException;

import ch.epfl.cvlab.libraryLoader.BinaryExtractor;

/*
 * Note to clients: notifying state changes are notified by the state.
 */
public class AlgorithmSubject extends Observable {

    private String configText;
    private URI config;
    
    private String log;
    private ArrayList<URI> modelList;
    private ArrayList<URI> binariesList;
      
    private int progress = 0;
    private URI selectedModelURI;
    private URI selectedBinary;
    
    public AlgorithmSubject(ArrayList<URI> binariesList){
         this(binariesList, "", new File("invalid").toURI());    
    }
    
    public AlgorithmSubject(ArrayList<URI> binariesList, String configText, URI config) {
        modelList = new ArrayList<URI>();
        this.binariesList = binariesList;
        this.configText = configText;
        this.config = config;
        setChanged();
    }
    
    public void loadBinaries() throws ZipException, URISyntaxException, IOException{
      String[] binariesNamesList = {"train", "predict"};
      binariesList = BinaryExtractor.extractBinaries(binariesNamesList);
    }
    
    public String getConfigText() {
        return configText;
    }
    
    public void setConfigText(String configText) {
        this.configText = configText;
        log("set config text");
        setChanged();
        notifyObservers();
    }
    
    public URI getConfig() {
        return config;
    }
    
    public void setConfig(URI config) {
        this.config = config;
        log("Selected config " + config);
        setChanged();
        notifyObservers();
    }
    
    public int getProgress() {
        return progress;
    }
    
    public void setProgress(int progress) {
        this.progress = progress;
        setChanged();
        notifyObservers();
    }

    public String getLog() {
      return log;
    }

    public void log(String log) {
      System.out.println(log + '\n');
      this.log += log+'\n';
    }

    public ArrayList<URI> getModelList() {
      return modelList;
    }

    public void setModelList(ArrayList<URI> modelList) {
      this.modelList = modelList;
      setChanged();
      notifyObservers();
    }
    
    public void addModelToList(URI model) {
      this.modelList.add(model);
      setChanged();
      notifyObservers();
    }

    public void setModel(URI model) {
      this.selectedModelURI = model;
      setChanged();
      notifyObservers();
    }

    public URI getSelectedBinary() {
      return selectedBinary;
    }

    public void setSelectedBinary(URI binaryName) {
      if(binariesList.contains(binaryName))
        this.selectedBinary = binaryName;
      else
        log("Binary " + binaryName + " not found.");     
    }
    
    public void setBinary(String binaryName){
      for(int i = 0; i < binariesList.size(); i++){
        String binaryNameAux = new File(binariesList.get(i)).getName();
        if(binaryName.equals(binaryNameAux)){
          log("Binary " + binaryName + " found.");
          this.setSelectedBinary(binariesList.get(i));
        }
      }
      log("Binary " + binaryName + " not found.");     
    }
    
    //TODO use combobox with all valid binaries in jar
    public void selectTrainBinary(){
      if(binariesList.size() >= 1)
        this.setSelectedBinary(binariesList.get(0));
    }
    public void selectPredictBinary(){
      if(binariesList.size() >= 2)
        this.setSelectedBinary(binariesList.get(1));
    }
    
    public URI getModel(){
      return selectedModelURI;
    }

        
}
