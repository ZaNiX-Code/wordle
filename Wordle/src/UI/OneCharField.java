package UI;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

public class OneCharField extends JTextField {
    public OneCharField() {
        super(1); // Set the preferred size to 1 character

        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateTextField(e.getDocument());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateTextField(e.getDocument());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateTextField(e.getDocument());
            }

            private void updateTextField(Document doc) {
                if (doc.getLength() > 1) {
                    // If more than one character is typed, remove the extra characters
                    try {
                        doc.remove(1, doc.getLength() - 1);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
}
