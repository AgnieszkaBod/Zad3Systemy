import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class Main extends JFrame {

    public Main() {
        setSize(1500, 700);
        setTitle("Zad3Systemy");
    }

    public static final Main window = new Main();
    public static final String filePath = "src/katalog.txt";

    public static String[] headers = new String[15];
    public static Object[][] data = new Object[24][15];

    public static final JButton bImportTxt = new JButton("Importuj dane z txt");
    public static final JButton bExportTxt = new JButton("Eksportuj dane do txt");

    public static final JButton bImportXml = new JButton("Importuj dane z xml");
    public static final JButton bExportXml = new JButton("Eksportuj dane do xml");

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

    static void exportToTxt() {
        PrintWriter zapis = null;
        try {
            zapis = new PrintWriter("src/wyniki.txt", StandardCharsets.UTF_8);
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

    static void exportToXml() throws ParserConfigurationException, TransformerException, FileNotFoundException {
        DocumentBuilder builder;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element laptops = document.createElement("laptops");
        laptops.setAttribute("moddate", String.valueOf(new Date()));

        int j;
        for (int i = 0; i < 24; i++) {
            j = 0;
            Element laptop = document.createElement("laptop");
            laptop.setAttribute("id", String.valueOf(i + 1));

            //PRODUCENT
            Element manufacturer = document.createElement("manufacturer");

            manufacturer.setTextContent((String) data[i][j]);

            j++;

            //EKRAN
            Element screen = document.createElement("screen");

            Element size = document.createElement("size");
            size.setTextContent((String) data[i][j]);
            j++;

            Element resolution = document.createElement("resolution");
            resolution.setTextContent((String) data[i][j]);
            j++;

            Element type = document.createElement("type");
            type.setTextContent((String) data[i][j]);
            j++;

            screen.setAttribute("touch", (String) data[i][j]);
            j++;

            //PROCESOR
            Element processor = document.createElement("processor");

            Element name = document.createElement("name");
            name.setTextContent((String) data[i][j]);
            j++;

            Element physical_cores = document.createElement("physical_cores");
            physical_cores.setTextContent((String) data[i][j]);
            j++;

            Element clock_speed = document.createElement("clock_speed");
            clock_speed.setTextContent((String) data[i][j]);
            j++;

            //RAM
            Element ram = document.createElement("ram");
            ram.setTextContent((String) data[i][j]);
            j++;

            //DYSK
            Element disc = document.createElement("disc");

            Element storage = document.createElement("storage");
            storage.setTextContent((String) data[i][j]);
            j++;

            disc.setAttribute("type", (String) data[i][j]);
            j++;

            //GPU
            Element graphic_card = document.createElement("graphic_card");

            Element nameGPU = document.createElement("name");
            nameGPU.setTextContent((String) data[i][j]);
            j++;

            Element memory = document.createElement("memory");
            memory.setTextContent((String) data[i][j]);
            j++;


            Element os = document.createElement("os");
            os.setTextContent((String) data[i][j]);
            j++;


            Element disc_reader = document.createElement("disc_reader");
            disc_reader.setTextContent((String) data[i][j]);

            laptop.appendChild(manufacturer);

            screen.appendChild(size);
            screen.appendChild(resolution);
            screen.appendChild(type);
            laptop.appendChild(screen);

            processor.appendChild(name);
            processor.appendChild(physical_cores);
            processor.appendChild(clock_speed);
            laptop.appendChild(processor);

            laptop.appendChild(ram);

            disc.appendChild(storage);
            laptop.appendChild(disc);

            graphic_card.appendChild(nameGPU);
            graphic_card.appendChild(memory);
            laptop.appendChild(graphic_card);

            laptop.appendChild(os);

            laptop.appendChild(disc_reader);

            laptops.appendChild(laptop);
        }

        document.appendChild(laptops);

        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty(OutputKeys.METHOD, "xml");
        t.transform(new DOMSource(document), new StreamResult(new FileOutputStream("laptops.xml")));

    }

    static void importFromXml() throws SAXException, ParserConfigurationException {
        File file = new File("laptops.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = null;
        try {
            document = documentBuilder.parse(file);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(window
                    , "Nie znaleziono pliku xml");
        }
        document.getDocumentElement().normalize();
        System.out.println("Root element: " + document.getDocumentElement().getNodeName());
        NodeList nodeList = document.getElementsByTagName("laptop");

        int j;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            j = 0;
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                data[i][j] = element.getElementsByTagName("manufacturer").item(0).getTextContent();
                j++;
                data[i][j] = element.getElementsByTagName("size").item(0).getTextContent();
                j++;
                data[i][j] = element.getElementsByTagName("resolution").item(0).getTextContent();
                j++;
                data[i][j] = element.getElementsByTagName("type").item(0).getTextContent();
                j++;
                data[i][j] = element.getElementsByTagName("screen").item(0).getAttributes().getNamedItem("touch").getNodeValue();
                j++;
                data[i][j] = element.getElementsByTagName("name").item(0).getTextContent();
                j++;
                data[i][j] = element.getElementsByTagName("physical_cores").item(0).getTextContent();
                j++;
                data[i][j] = element.getElementsByTagName("clock_speed").item(0).getTextContent();
                j++;
                data[i][j] = element.getElementsByTagName("ram").item(0).getTextContent();
                j++;
                data[i][j] = element.getElementsByTagName("storage").item(0).getTextContent();
                j++;
                data[i][j] = element.getElementsByTagName("disc").item(0).getAttributes().getNamedItem("type").getNodeValue();
                j++;
                data[i][j] = element.getElementsByTagName("name").item(1).getTextContent();
                j++;
                data[i][j] = element.getElementsByTagName("memory").item(0).getTextContent();
                j++;
                data[i][j] = element.getElementsByTagName("os").item(0).getTextContent();
                j++;
                data[i][j] = element.getElementsByTagName("disc_reader").item(0).getTextContent();
            }
        }
    }

    public static void main(String[] args) throws IOException {

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getHeaders();

        bExportTxt.setBounds(10, 450, 150, 50);
        bExportTxt.setBackground(Color.ORANGE);
        bExportTxt.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        bImportTxt.setBounds(170, 450, 150, 50);
        bImportTxt.setBackground(Color.BLUE);
        bImportTxt.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        bExportXml.setBounds(330, 450, 150, 50);
        bExportXml.setBackground(Color.CYAN);
        bExportXml.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        bImportXml.setBounds(490, 450, 150, 50);
        bImportXml.setBackground(Color.GREEN);
        bImportXml.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JTable table = new JTable(data, headers) {
            @Override
            public void setValueAt(Object aValue, int row, int column) {
                super.setValueAt(aValue, row, column);
                if (aValue.toString().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(window
                            , "Pole nie może być puste!");
                } else if ((column == 4 || column == 10) && aValue.toString().trim().length() != 3) {                    JOptionPane.showMessageDialog(window
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
        window.add(bExportXml);
        window.add(bImportXml);
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
        bExportTxt.addActionListener(e -> exportToTxt());

        bImportXml.addActionListener(Main::importXml);
        bExportXml.addActionListener(Main::exportXml);
    }

    private static void importXml(ActionEvent e) {
        try {
            importFromXml();
            window.repaint();
        } catch (SAXException | ParserConfigurationException ex) {
            ex.printStackTrace();
        }
    }

    private static void exportXml(ActionEvent e) {
        try {
            exportToXml();
        } catch (ParserConfigurationException | TransformerException | FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}

