package ips.gui;

import javax.swing.*;
import java.util.List;

/**
 * It behaves like a normal JTextField but tries to autocomplete your input with the words passed in the constructor.
 */
public class AutocompleteJTextField extends JTextField {

    Autocomplete autocomplete;

    public AutocompleteJTextField(int columns, List<String> words) {
        super(columns);
        autocomplete = new Autocomplete(this, words);
        this.getDocument().addDocumentListener(autocomplete);
    }
}
