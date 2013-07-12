package ch.epfl.cvlab.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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

    private static final String SETTINGS_FILE_NAME = "settings.ini";
    
    private String configText;
    private URI config;
    
    private String log = "";
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
      
      String osname = System.getProperty("os.name").toLowerCase();
      String arch = System.getProperty("os.arch").toLowerCase();
      String[] binariesNamesList = new String[2];
      String[] dependencies = new String[2];
      if(osname.contains("windows")){
          binariesNamesList[0] = "train.exe";
          binariesNamesList[1] = "predict.exe";
          //FIXME eliminate dependency to cuda dll
          dependencies[0] = "cudart64_50_35.dll";
          dependencies[1] = "npp64_50_35.dll";
          BinaryExtractor.extractBinaries(dependencies);
      }else if(osname.contains("nix") || osname.contains("nux") || osname.contains("aix")){
          binariesNamesList[0] = "train";
          binariesNamesList[1] = "predict";
      }else if(osname.contains("mac")){
          binariesNamesList[0] = "train";
          binariesNamesList[1] = "predict";
      }else{
           throw new IOException("Unsupported OS or architecture");
      }
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
      setChanged();
      notifyObservers();
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
    
    public URI getTrainBinary(){
        return binariesList.get(0);
    }
    
    public URI getPredictBinary(){
        return binariesList.get(1);
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
    
    public void saveSettings() throws IOException{
        File file = new File(SETTINGS_FILE_NAME);
        if(!file.exists()){
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(new File(config).getAbsolutePath());
        bw.close();
    }
    
    public void loadSettings() throws IOException{
        
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(SETTINGS_FILE_NAME));
        } catch (FileNotFoundException e) {
            System.err.println("Settings file not found");
            return;
        }
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
                
                processSettingsLine(line);
            }
            String everything = sb.toString();
            System.out.println("Loaded Settings:\n" + everything);
                       
        } finally {
            br.close();
        }
    }
    
    public void processSettingsLine(String line) {
        throw new UnsupportedOperationException();
    }
        
}
