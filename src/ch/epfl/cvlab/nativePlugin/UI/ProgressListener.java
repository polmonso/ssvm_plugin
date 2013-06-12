package ch.epfl.cvlab.nativePlugin.UI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.SwingWorker.StateValue;

public class ProgressListener implements PropertyChangeListener {
  
  @Override
  public void propertyChange(final PropertyChangeEvent event) {
//    if(event.getPropertyName().equals("progress")) {
//      searchProgressBar.setIndeterminate(false);
//      searchProgressBar.setValue((Integer) event.getNewValue());
//      break;
//    } else if(event.getPropertyName().equals("state")) {
//      switch ((StateValue) event.getNewValue()) {
//      case DONE:
//        searchProgressBar.setVisible(false);
//        searchCancelAction.putValue(Action.NAME, "Search");
//        try {
//          final int count = searchWorker.get();
//          JOptionPane.showMessageDialog(Application.this, "Found: " + count + " words", "Search Words",
//              JOptionPane.INFORMATION_MESSAGE);
//        } catch (final CancellationException e) {
//          JOptionPane.showMessageDialog(Application.this, "The search process was cancelled", "Search Words",
//              JOptionPane.WARNING_MESSAGE);
//        } catch (final Exception e) {
//          JOptionPane.showMessageDialog(Application.this, "The search process failed", "Search Words",
//              JOptionPane.ERROR_MESSAGE);
//        }
//
//        searchWorker = null;
//        break;
//      case STARTED:
//      case PENDING:
//        searchCancelAction.putValue(Action.NAME, "Cancel");
//        searchProgressBar.setVisible(true);
//        searchProgressBar.setIndeterminate(true);
//        break;
//      }
//      break;
//    }
//  }
  }
}
