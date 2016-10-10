package ips.gui;

import javax.swing.JTextField;
import java.util.List;

/**
 * It behaves like a normal JTextField but tries to autocomplete your input with the words passed in the constructor.
 */
public class AutocompleteJTextField extends JTextField {


    private static final long serialVersionUID = -5186837131977619392L;
    Autocomplete autocomplete;

    public AutocompleteJTextField(int columns, List<String> words) {
        super(columns);
        autocomplete = new Autocomplete(this, words);
        this.getDocument().addDocumentListener(autocomplete);
    }
}
