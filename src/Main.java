import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Main extends JFrame {

    public Main() {
        setSize(1500, 700);
        setTitle("Zad3Systemy");
    }

    public static final String filePath = "src/katalog.txt";

    public static String[] headers = new String[15];
    public static Object[][] data = new Object[24][15];

    public static JButton bImportTxt = new JButton("Importuj dane z txt");
    public static JButton bExportTxt = new JButton("Eksportuj dane do txt");

    public static JButton bImportXml = new JButton("Importuj dane z xml");
    public static JButton bExportXml = new JButton("Eksportuj dane do xml");

    static void importData() throws IOException {
        BufferedReader fileReader = null;
        String line;
        try {
            fileReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(filePath), StandardCharsets.UTF_8));
        } catch (
                FileNotFoundException e) {
            System.out.println("Blad przy otwieraniu pliku!");

        }
        String[] words;
        assert fileReader != null;
        fileReader.readLine();

        for (int i = 0; i < 24; i++) {
            line = fileReader.readLine();
            words = line.split(";", -1);
            for (int j = 0; j <= 14; j++) {
                data[i][j] = words[j];
                if (data[i][j] == null || data[i][j].equals("")) {
                    data[i][j] = "Brak informacji";
                }
            }
        }
    }

    static void getHeaders() throws IOException {
        BufferedReader fileReader = null;
        String line;

        try {
            fileReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(filePath), StandardCharsets.UTF_8));

        } catch (
                FileNotFoundException e) {
            System.out.println("Blad przy otwieraniu pliku!");
        }

        if (fileReader != null) {
            line = fileReader.readLine();
            String[] words = line.split(";");
            int i = 0;
            for (String word : words) {
                headers[i] = word;
                i++;
            }
        }
    }

    static void exportToFileTxt() {
        PrintWriter zapis = null;
        try {
            zapis = new PrintWriter("src/wynik.txt", StandardCharsets.UTF_8);
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }

        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 15; j++) {
                assert zapis != null;
                zapis.print(data[i][j] + ";");
            }
            if (i < 23) {
                zapis.print("\n");
            }
        }
        zapis.close();
    }

    static void exportToFileXml() {

    }

    static void importTxtFileXml() {

    }

    public static void main(String[] args) throws IOException {

        Main window = new Main();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getHeaders();

        bExportTxt.setBounds(10, 450, 150, 50);
        bExportTxt.setBackground(Color.ORANGE);
        bExportTxt.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        bImportTxt.setBounds(170, 450, 150, 50);
        bImportTxt.setBackground(Color.BLUE);
        bImportTxt.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JTable table = new JTable(data, headers) {
            @Override
            public void setValueAt(Object aValue, int row, int column) {
                super.setValueAt(aValue, row, column);
                if (aValue.toString().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(window
                            , "Pole nie może być puste!");
                } else if ((column == 4 || column == 10) && aValue.toString().trim().length() != 3) {
                    JOptionPane.showMessageDialog(window
                            , "Tekst musi miec 3 znaki!");
                } else if (column == 1 && !aValue.toString().endsWith("\"")) {
                    JOptionPane.showMessageDialog(window
                            , "Pole musi się kończyć na \"");
                } else if (column == 2 & !aValue.toString().matches("[0-9]+x[0-9]+")) {
                    JOptionPane.showMessageDialog(window
                            , "Wprowadź wartość według wzoru, np. 1920x1080");
                } else {
                    super.setValueAt(aValue, row, column);
                    data[row][column] = aValue;
                }
                System.out.println(aValue + " | " + row + " | " + column);
            }
        };

        table.setBounds(30, 40, 400, 500);
        JScrollPane sp = new JScrollPane(table);
        window.add(bExportTxt);
        window.add(bImportTxt);
        window.add(sp);
        window.setVisible(true);

        bImportTxt.addActionListener(e -> {
            try {
                importData();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            window.repaint();
        });
        bExportTxt.addActionListener(e -> exportToFileTxt());
    }
}

