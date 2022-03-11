
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

public class VentasMensualesParticular {
    static final String CLASS_NAME = VentasMensualesParticular.class.getSimpleName();
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

            System.out.println("Ventas totales de cada mes de una marca en particular: ");
            VentasMesParticular(doc);


        } catch (ParserConfigurationException e) {
            LOG.severe(e.getMessage());
        } catch (IOException e) {
            LOG.severe(e.getMessage());
        } catch (SAXException e) {
            LOG.severe(e.getMessage());
        }
    }

    public static void VentasMesParticular(Document doc){
        int m = 0;
        boolean n = true;
        String date[] = new String[2];
        String fecha = "";
        date [0] = "2021";
        date [1] = "1";

        for (int j = 0; j <= 1; j++){
            for(int i = 1; i < 13; i++){
                if (Integer.parseInt(date[1]) < 9){
                    date[1] = "0" + String.valueOf(i);
                }else{
                    date[1] = String.valueOf(i);
                }
                fecha = date[0] +"-"+ date[1];
                salesState(doc, fecha, "Toyota");
            }
            date[0] = "2022";
            date [1] = "1";
        }
    }


    public static void salesState(Document doc, String s, String marca) {
        Element root = doc.getDocumentElement();

        NodeList salesData = root.getElementsByTagName("insurance_record");

        int n = salesData.getLength();
        int k = 0;
        double sum = 0.0;
        String coche = "";
        String fecha = "";
        String com = "";
        String con = "";
        con = String.valueOf(s.charAt(5)) +String.valueOf(s.charAt(6));

        for (int index = 0; index < n; index++) {
            Node node = salesData.item(index);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String car = element.getElementsByTagName("car").item(0).getTextContent();
                String insurance = element.getElementsByTagName("insurance").item(0).getTextContent();
                String date = element.getElementsByTagName("contract_date").item(0).getTextContent();
                com = String.valueOf(date.charAt(5)) +String.valueOf(date.charAt(6));

                if ( com.equals(con) && car.equals(marca) ){
                    coche = car;
                    fecha = s;
                    sum = Double.parseDouble(insurance) + sum;
                    k++;
                }
            }
        }

        System.out.printf(" Marca: %-10.10s Fecha: %-15.15s Ventas totales: %,7.2f \n", coche, fecha, sum);
        k = 0;
        sum = 0;
    }
}
