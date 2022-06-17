package cz.cvut.fel.sit.battleship.GameConfiguration;

import cz.cvut.fel.sit.battleship.ShipTypes.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;


public class Parser {
    public void configParseFile(String mode, GameMode gamemode) {
        File configFile = new File(getClass().getResource("/config.xml").getFile());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;

        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        Document doc = null;

        try {
            assert builder != null;
            doc = builder.parse(configFile);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        doc.getDocumentElement().normalize();

        NodeList nodes = doc.getElementsByTagName(mode + "Mode");
        for (int j = 0; j < nodes.getLength(); j++) {
            Node node = nodes.item(j);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                gamemode.size = Integer.parseInt(
                        element.getElementsByTagName(mode + "BoardSideSize").item(0).getTextContent());

                int shipArraySize = Integer.parseInt(
                        element.getElementsByTagName(mode + "ShipArraySize").item(0).getTextContent());

                gamemode.ships = new Ship[shipArraySize];

                NodeList nodeListShips = element.getElementsByTagName(mode + "ShipsArray");

                for (int i = 0; i < nodeListShips.getLength(); i++){

                    Node nodeShip = nodeListShips.item(i);
                    if (nodeShip.getNodeType() == Node.ELEMENT_NODE) {
                        Element elementShip = (Element) nodeShip;

                        NodeList ships =  elementShip.getElementsByTagName("ship");

                        for (int q = 0; q < ships.getLength(); q++){

                            Node ship = ships.item(q);
                            if (ship.getNodeType() == Node.ELEMENT_NODE) {
                                Element shipElement = (Element) ship;

                                switch (shipElement.getTextContent()) {
                                    case "Convoy" -> gamemode.ships[q] = new Convoy();
                                    case "Destroyer" -> gamemode.ships[q] = new Destroyer();
                                    case "Cruiser" -> gamemode.ships[q] = new Cruiser();
                                    case "Battleship" -> gamemode.ships[q] = new Battleship();
                                    case "AircraftCarrier" -> gamemode.ships[q] = new AircraftCarrier();
                                }
                            }
                        }
                        break;
                    }

                }

            }
        }
    }
}
