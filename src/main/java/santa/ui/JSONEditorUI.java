package santa.ui;

import com.google.gson.*;
import context.ContextStore;
import santa.SecretSantaScript;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class JSONEditorUI {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton addButton, removeButton, loadButton, saveButton;
    private JFileChooser fileChooser;

    public JSONEditorUI() {
        // Set up the main frame
        frame = new JFrame("JSON File Editor");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Set up the table
        String[] columns = {"Name", "Email", "Address", "Phone Number"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Set up buttons
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add Participant");
        removeButton = new JButton("Remove Selected");
        loadButton = new JButton("Load JSON");
        saveButton = new JButton("Save JSON");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(saveButton);

        JButton secretSantaButton;
        secretSantaButton = new JButton("Run Secret Santa");
        buttonPanel.add(secretSantaButton);

        // Add ActionListener for the button
        secretSantaButton.addActionListener(e -> {
            try {
                // Define the original file path
                String originalFilePath = ContextStore.get("participants-directory", "src/main/resources/participants.json");
                File originalFile = new File(originalFilePath);

                // Save the current data to the original file
                saveJson(originalFile);

                // Call the main method of SecretSantaScript
                SecretSantaScript.main(new ArrayList<>().toArray(new String[0]));

                // Show success message
                JOptionPane.showMessageDialog(frame, "Secret Santa executed successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                // Show error message if anything goes wrong
                JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });



        // Set up file chooser
        fileChooser = new JFileChooser();

        // Add components to frame
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Set up button actions
        setUpActions();

        // Load default participants.json on startup
        loadDefaultJson();

        // Set the frame visible
        frame.setVisible(true);
    }

    // Method to load the default JSON file
    private void loadDefaultJson() {
        String defaultFilePath = ContextStore.get("participants-directory", "src/main/resources/participants.json");

        File defaultFile = new File(defaultFilePath);

        if (defaultFile.exists()) {
            loadJson(defaultFile);
        } else {
            JOptionPane.showMessageDialog(
                    frame,
                    "Default file not found: " + defaultFilePath,
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void setUpActions() {
        // Add participant
        addButton.addActionListener(e -> {
            if (isLastRowEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill the last added row before adding a new participant.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            } else {
                // Add a new empty row
                tableModel.addRow(new Object[]{"", "", "", ""});

                // Focus the first cell of the newly added row
                int newRow = tableModel.getRowCount() - 1; // Index of the newly added row
                table.editCellAt(newRow, 0); // Set the cell to edit mode
                table.requestFocusInWindow(); // Request focus for the table
            }
        });


        // Remove selected participant
        removeButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                tableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(frame, "No row selected!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Load JSON file
        loadButton.addActionListener(e -> {
            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                loadJson(file);
            }
        });

        // Save JSON file
        saveButton.addActionListener(e -> {
            if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                saveJson(file);
            }
        });
    }

    private boolean isLastRowEmpty() {
        int lastRowIndex = tableModel.getRowCount() - 1;

        if (lastRowIndex < 0) {
            return false; // No rows present, safe to add a new one
        }

        // Check all cells in the last row
        for (int col = 0; col < tableModel.getColumnCount(); col++) {
            Object cellValue = tableModel.getValueAt(lastRowIndex, col);
            if (cellValue != null && !cellValue.toString().trim().isEmpty()) {
                return false; // Found a non-empty cell
            }
        }

        return true; // All cells in the last row are empty
    }

    private void loadJson(File file) {
        try (Reader reader = new FileReader(file)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray participants = jsonObject.getAsJsonArray("participants");

            tableModel.setRowCount(0); // Clear existing data

            for (JsonElement element : participants) {
                JsonObject participant = element.getAsJsonObject();
                tableModel.addRow(new Object[]{
                        participant.get("name").getAsString(),
                        participant.get("email").getAsString(),
                        participant.get("address").getAsString(),
                        participant.get("phoneNumber").getAsString()
                });
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error loading JSON: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveJson(File file) {
        JsonArray participants = new JsonArray();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String name = tableModel.getValueAt(i, 0).toString().trim();
            String email = tableModel.getValueAt(i, 1).toString().trim();
            String address = tableModel.getValueAt(i, 2).toString().trim();
            String phoneNumber = tableModel.getValueAt(i, 3).toString().trim();

            // Skip empty rows
            if (name.isEmpty() && email.isEmpty() && address.isEmpty() && phoneNumber.isEmpty()) {
                continue;
            }

            // Create JSON object for a valid row
            JsonObject participant = new JsonObject();
            participant.addProperty("name", name);
            participant.addProperty("email", email);
            participant.addProperty("address", address);
            participant.addProperty("phoneNumber", phoneNumber);

            participants.add(participant);
        }

        // Ensure at least one valid participant exists
        if (participants.size() == 0) {
            JOptionPane.showMessageDialog(frame, "No valid participants to save!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("participants", participants);

        try (Writer writer = new FileWriter(file)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(jsonObject, writer);
            JOptionPane.showMessageDialog(frame, "JSON saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving JSON: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        new JSONEditorUI();
    }
}

