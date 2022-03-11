
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import java.util.logging.Logger;

public class SeguroModelo {
    static final String CLASS_NAME = SeguroModelo.class.getSimpleName();
    static final Logger LOG = Logger.getLogger(CLASS_NAME);

    public static void main(String argv[]) {
        if (argv.length != 1) {
            LOG.severe("Falta archivo XML como argumento.");
            System.exit(1);
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(new File(argv[0]));

            doc.getDocumentElement().normalize();

            System.out.println("Costo promedio del seguro por modelo: ");
            promedio(doc);


        } catch (ParserConfigurationException e) {
            LOG.severe(e.getMessage());
        } catch (IOException e) {
            LOG.severe(e.getMessage());
        } catch (SAXException e) {
            LOG.severe(e.getMessage());
        }
    }

    public static void salesState(Document doc, String s) {
        Element root = doc.getDocumentElement();

        NodeList salesData = root.getElementsByTagName("insurance_record");

        int n = salesData.getLength();
        int k = 0;
        double sum = 0.0;
        double prom = 0.0;
        String modelo ="";
        String carro ="";

        for (int index = 0; index < n; index++) {
            Node node = salesData.item(index);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String car = element.getElementsByTagName("car").item(0).getTextContent();
                String model = element.getElementsByTagName("model").item(0).getTextContent();
                String insurance = element.getElementsByTagName("insurance").item(0).getTextContent();

                if (model.equals(s)) {
                    carro = car;
                    modelo = model;
                    sum = Double.parseDouble(insurance) + sum;
                    k++;
                }
            }
        }
        prom = sum /k;
        if (!Double.isNaN(prom))
            System.out.printf(" Modelo: %-5.10s %-15.15s Costo promedio: %,7.2f \n",
                    modelo, carro, prom);
        k = 0;
        sum = 0;
    }

    public static void promedio(Document doc){
        int m = 0;
        boolean n = true;

        while (n == true) {
            salesState(doc, String.valueOf(m));
            m++;
            if (m > 2030){
                n = false;
            }
        }

    }


}