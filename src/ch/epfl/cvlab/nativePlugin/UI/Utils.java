package ch.epfl.cvlab.nativePlugin.UI;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.LinkedList;

public class Utils {
  
  //TODO this might work only on linux, replace by .\\ did not work
  public static String defaultLastDir = ".";
  
  public static String lastDir = ".";
  
  public static String modelDir = "./parameter_vector0";
  
  public static <T> T[] concat(T[] first, T[] second) {
    T[] result = Arrays.copyOf(first, first.length + second.length);
    System.arraycopy(second, 0, result, first.length, second.length);
    return result;
  }
  
  public static LinkedList<URI> listModels(){
       
    File folder = new File(modelDir);
    File[] listOfFiles = folder.listFiles();
    if(listOfFiles == null){
      System.err.println(folder.getAbsolutePath() + " is not a directory or does not exist");
      folder = new File(".");
      listOfFiles = folder.listFiles();
      if(listOfFiles == null){
        System.err.println(folder.getAbsolutePath() + " is not a directory or does not exist");
        return null;
      }
    }
    String fileName;
    LinkedList<URI> modelsList = new LinkedList<URI>();

    for (int i=0; i<listOfFiles.length; i++){
      if (listOfFiles[i].isFile()){
        fileName = listOfFiles[i].getName();
        if(fileName.endsWith(".txt") && (fileName.startsWith("model") || fileName.startsWith("iteration"))){
          modelsList.add(listOfFiles[i].toURI());
          System.out.println("Found model: " + fileName);
        }
      }
    }
    return modelsList;
    
  }
  
  public static String loadTextFile
    (Frame f, String title, String defDir, String fileType) {
      FileDialog fd = new FileDialog(f, title, FileDialog.LOAD);
      fd.setFile(fileType);
      fd.setDirectory(defDir);
      fd.setLocation(50, 50);
      fd.setVisible(true);
      defDir = fd.getDirectory();
      return fd.getDirectory() + 
          System.getProperty("file.separator") + fd.getFile();
  }
  
  public static String saveTextFile
    (Frame f, String title, String defDir, String fileType) {
      FileDialog fd = new FileDialog(f, title, FileDialog.SAVE);
      fd.setFile(fileType);
      fd.setDirectory(defDir);
      fd.setLocation(50, 50);
      fd.setVisible(true);
      return fd.getDirectory() + 
              System.getProperty("file.separator") + fd.getFile();
  }
  
}
