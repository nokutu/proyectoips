package ips;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;

/**
 * This class adds autocompletion to a text field.
 * <p>
 * You must use the following code:
 * <p>
 * <pre>
 *     {@code
 *     JButton textField = new JTextField(20);
 *     Autocomplete autocomplete = new Autocomplete(textField, Arrays.asList("string1", "string2"));
 *     textField.getDocument().addDocumentListener(autocomplete);
 *     }
 * </pre>
 */
public class Autocomplete implements DocumentListener {

    private static enum Mode {
        INSERT,
        COMPLETION
    }

    ;

    private JTextField textField;
    private final List<String> keywords;
    private Mode mode = Mode.INSERT;

    public Autocomplete(JTextField textField, List<String> keywords) {
        this.textField = textField;
        this.keywords = keywords;
        Collections.sort(keywords);
    }

    @Override
    public void changedUpdate(DocumentEvent ev) {
    }

    @Override
    public void removeUpdate(DocumentEvent ev) {
    }

    @Override
    public void insertUpdate(DocumentEvent ev) {
        if (ev.getLength() != 1)
            return;

        int pos = ev.getOffset();
        String content = null;
        try {
            content = textField.getText(0, pos + 1);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        // Find where the word starts

        // Too few chars
        if (pos < 1)
            return;

        assert content != null;
        String prefix = content.toLowerCase();
        int n = Collections.binarySearch(keywords, prefix);
        if (n < 0 && -n <= keywords.size()) {
            String match = keywords.get(-n - 1);
            if (match.startsWith(prefix)) {
                // A completion is found

                // We cannot modify Document from within notification,
                // so we submit a task that does the change later
                SwingUtilities.invokeLater(new CompletionTask(match, pos + 1));
            }
        } else {
            // Nothing found
            mode = Mode.INSERT;
        }
    }

    public class CommitAction extends AbstractAction {
        /**
         *
         */
        private static final long serialVersionUID = 5794543109646743416L;

        @Override
        public void actionPerformed(ActionEvent ev) {
            if (mode == Mode.COMPLETION) {
                int pos = textField.getSelectionEnd();
                StringBuffer sb = new StringBuffer(textField.getText());
                sb.insert(pos, " ");
                textField.setText(sb.toString());
                textField.setCaretPosition(pos + 1);
                mode = Mode.INSERT;
            } else {
                textField.replaceSelection("\t");
            }
        }
    }

    private class CompletionTask implements Runnable {
        private String completion;
        private int position;

        CompletionTask(String completion, int position) {
            this.completion = completion;
            this.position = position;
        }

        public void run() {
            StringBuffer sb = new StringBuffer(textField.getText());
            sb.insert(position, completion);
            textField.setText(sb.toString());
            textField.setCaretPosition(position + completion.length());
            textField.moveCaretPosition(position);
            mode = Mode.COMPLETION;
        }
    }

}