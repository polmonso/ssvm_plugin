package ch.epfl.cvlab.nativePlugin.UI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuBar;

import ch.epfl.cvlab.core.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JScrollPane;

public class SwingGUI extends JFrame implements Observer {
  
  private static final long serialVersionUID = 1L;
  private JTextField configPath;
  private String lastDir = Utils.defaultLastDir;
  private final File currentDirectory = null;
  private AlgorithmSubject subject;
  
  private static AlgorithmWorker algorithmWorker;
  
  private final JTextArea configText;
  private final JTextArea log;
  private final JTextArea progressText;
  private final JComboBox modelComboBox;
  
  private final JButton btnTrain = new JButton("Train");
  private final JButton btnPredict = new JButton("Predict");


  
  /**
 * @param algorithmSubject
 */
public SwingGUI(AlgorithmSubject algorithmSubject) {
    
    //this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    
    this.subject = algorithmSubject;
    subject.setModelList(new ArrayList<URI>(Utils.listModels()));

    JPanel config = new JPanel();
    getContentPane().add(config, BorderLayout.CENTER);
    
    JScrollPane configTextPane = new JScrollPane();
    
    JScrollPane progressPane = new JScrollPane();
    
    JScrollPane logPane = new JScrollPane();
            
    log = new JTextArea();
    logPane.setViewportView(log);
    log.setText("");
    
    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);
    
    JLabel lblConfigPath = new JLabel("Config path:");
    lblConfigPath.setHorizontalAlignment(SwingConstants.LEFT);
    
    configPath = new JTextField();
    configPath.setHorizontalAlignment(SwingConstants.LEFT);
    configPath.setColumns(10);
    
    JButton btnSave = new JButton("Save");
    btnSave.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        try {
          PrintWriter out = new PrintWriter(configPath.getText());
          out.println(configText.getText());
          out.close();
      } catch (FileNotFoundException e1) {
          // TODO handle filenotfound better, create it for example
          System.err.println("File does not exist");
          log.append("File does not exist");
          e1.printStackTrace();
      }
      }
    });

    final JProgressBar progressBar = new JProgressBar();    

    final PropertyChangeListener progressListener = new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent event){
        if(event.getPropertyName().equals("progress")) {
          progressBar.setValue((Integer) event.getNewValue());
        }
      }
    };
    
    algorithmWorker = new AlgorithmWorker(subject);  
    algorithmWorker.addPropertyChangeListener(progressListener);
    
    /*
    btnTrain.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        System.out.println("running train ");
        subject.selectTrainBinary();
        AlgorithmWorker algorithmWorker = new AlgorithmWorker(subject);
        algorithmWorker.execute();
      }
    });
    */
    
    /*
    btnPredict.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        System.out.println("running run");
        subject.selectPredictBinary();
        String model = new File(subject.getModel()).getAbsolutePath();
        String[] extraArgs = {Constants.predictFlag,model};
        AlgorithmWorker algorithmWorker = new AlgorithmWorker(subject);
        algorithmWorker.setExtraArgs(extraArgs);

        algorithmWorker.execute();
      }
    });
    */
    
    JButton btnOutput = new JButton("Output");
    
    JLabel lblTitle = new JLabel("CVLab SSVM mitochondria predictor");
    
    JLabel lblResults = new JLabel("Results");
    
    JLabel lblModel = new JLabel("Model");
    
    modelComboBox = new JComboBox();
    modelComboBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox)e.getSource();
        int modelIndex = cb.getSelectedIndex();
        if(modelIndex >= 0)
            subject.setModel(subject.getModelList().get(modelIndex));
      }
    });
    
    JLabel lblProgressMessages = new JLabel("Progress Messages");
    
    JLabel lblProgress = new JLabel("Progress");
    
    JButton btnConfig = new JButton("...");
    btnConfig.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        JFileChooser fileChooser = new JFileChooser(new File("."));
        int returnVal = fileChooser.showOpenDialog(e.getComponent());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          subject.setConfig(file.toURI());
          String config;
          try {
            config = new Scanner( new File(configPath.getText()), Charset.defaultCharset().displayName() ).useDelimiter("\\A").next();
            subject.setConfigText(config);
          } catch (FileNotFoundException e1) {
            subject.log("File: " + file.getAbsolutePath() + " not found");
            e1.printStackTrace();
          }
        } else {
          subject.log("Open command cancelled by user");
        }
      }
    });
    btnConfig.setEnabled(true);
    btnConfig.setToolTipText("Open config file");
    btnConfig.setVerticalAlignment(SwingConstants.CENTER);
    
    JButton btnAbort = new JButton("Abort");
    btnAbort.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        algorithmWorker.cancel(true);
      }
    });
    
    
    GroupLayout gl_config = new GroupLayout(config);
    gl_config.setHorizontalGroup(
      gl_config.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_config.createSequentialGroup()
          .addContainerGap()
          .addGroup(gl_config.createParallelGroup(Alignment.LEADING)
            .addComponent(logPane, GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE)
            .addGroup(gl_config.createSequentialGroup()
              .addGroup(gl_config.createParallelGroup(Alignment.LEADING)
                .addComponent(configPath, GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addGroup(gl_config.createSequentialGroup()
                  .addComponent(btnOutput)
                  .addPreferredGap(ComponentPlacement.RELATED)
                  .addGroup(gl_config.createParallelGroup(Alignment.LEADING)
                    .addGroup(gl_config.createSequentialGroup()
                      .addComponent(lblProgress)
                      .addPreferredGap(ComponentPlacement.RELATED, 132, Short.MAX_VALUE))
                    .addComponent(progressBar, GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)))
                .addGroup(gl_config.createSequentialGroup()
                  .addGap(12)
                  .addComponent(btnSave)
                  .addPreferredGap(ComponentPlacement.RELATED)
                  .addComponent(btnTrain)
                  .addPreferredGap(ComponentPlacement.RELATED)
                  .addComponent(btnPredict))
                .addComponent(configTextPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addGroup(gl_config.createSequentialGroup()
                  .addGroup(gl_config.createParallelGroup(Alignment.LEADING)
                    .addComponent(lblTitle)
                    .addGroup(gl_config.createSequentialGroup()
                      .addComponent(lblConfigPath)
                      .addPreferredGap(ComponentPlacement.RELATED)
                      .addComponent(btnConfig)))
                  .addGap(55)))
              .addPreferredGap(ComponentPlacement.RELATED)
              .addGroup(gl_config.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_config.createSequentialGroup()
                  .addComponent(btnAbort)
                  .addPreferredGap(ComponentPlacement.RELATED, 191, Short.MAX_VALUE))
                .addGroup(gl_config.createSequentialGroup()
                  .addComponent(lblResults)
                  .addGap(18)
                  .addComponent(lblModel)
                  .addPreferredGap(ComponentPlacement.RELATED)
                  .addComponent(modelComboBox, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE))
                .addComponent(lblProgressMessages)
                .addComponent(progressPane, GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))))
          .addContainerGap())
    );
    gl_config.setVerticalGroup(
      gl_config.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_config.createSequentialGroup()
          .addContainerGap()
          .addComponent(lblTitle)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addGroup(gl_config.createParallelGroup(Alignment.LEADING)
            .addGroup(gl_config.createSequentialGroup()
              .addGroup(gl_config.createParallelGroup(Alignment.BASELINE)
                .addComponent(lblConfigPath)
                .addComponent(btnConfig))
              .addPreferredGap(ComponentPlacement.RELATED)
              .addComponent(configPath, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
            .addGroup(gl_config.createSequentialGroup()
              .addGroup(gl_config.createParallelGroup(Alignment.BASELINE)
                .addComponent(lblResults)
                .addComponent(lblModel)
                .addComponent(modelComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
              .addPreferredGap(ComponentPlacement.RELATED)
              .addComponent(lblProgressMessages)))
          .addGap(18)
          .addGroup(gl_config.createParallelGroup(Alignment.LEADING)
            .addComponent(progressPane, GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
            .addComponent(configTextPane, GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE))
          .addPreferredGap(ComponentPlacement.UNRELATED)
          .addGroup(gl_config.createParallelGroup(Alignment.TRAILING)
            .addGroup(gl_config.createSequentialGroup()
              .addGroup(gl_config.createParallelGroup(Alignment.BASELINE)
                .addComponent(btnSave)
                .addComponent(btnTrain)
                .addComponent(btnPredict))
              .addGap(12)
              .addGroup(gl_config.createParallelGroup(Alignment.TRAILING)
                .addGroup(gl_config.createSequentialGroup()
                  .addComponent(lblProgress)
                  .addGap(3)
                  .addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
                .addComponent(btnOutput)))
            .addComponent(btnAbort))
          .addGap(18)
          .addComponent(logPane, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
          .addContainerGap())
    );
    
    progressText = new JTextArea();
    progressPane.setViewportView(progressText);
    
    configText = new JTextArea();
    configTextPane.setViewportView(configText);
    
    config.setLayout(gl_config);
    this.pack();
    
  }
  
  public void init(){
      
      //TODO if the number of binaries were to change, change buttons to comboboxes
               
          SSVMMouseAdapter trainMouseAdapter = new SSVMMouseAdapter(subject, subject.getTrainBinary(), algorithmWorker);
          btnTrain.addMouseListener(trainMouseAdapter);
          
          SSVMMouseAdapter predictMouseAdapter = new SSVMMouseAdapter(subject, subject.getPredictBinary(), algorithmWorker);
          btnPredict.addMouseListener(predictMouseAdapter);
          
  }
  
  public void showConfigSubject(){

    configPath.setText(new File(subject.getConfig()).getPath());
    configText.setText(subject.getConfigText());
    log.setText(subject.getLog());
    modelComboBox.removeAllItems();
  
    for(Iterator<URI> it = subject.getModelList().iterator(); it.hasNext();){
      //FIXME Swing is not thread safe: fix potential concurrency violation! (EDT is doing it so it might be ok)
      modelComboBox.addItem(new File(it.next()).getName());
    }

}
  
  @Override
  public void update(Observable config, Object ignored) {
      if (config instanceof AlgorithmSubject) {
          this.subject = (AlgorithmSubject) config;
          showConfigSubject();
          System.out.println("[GUI] ConfigSubject Updated");
      }
  }
}
