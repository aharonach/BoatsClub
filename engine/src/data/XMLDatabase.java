package data;

import entities.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLDatabase {
    private final String directory = System.getProperty("user.dir").replace("bin", "webapps") + "/boatsclub-resources";
    private final String xmlPath = "database.xml";
    private final Database database;
    private File file;
    private Document xmlFile;
    private Map<String, Element> entities;

    public XMLDatabase(Database database) {
        this.database = database;
        initXml();
    }

    /**
     * JUST FOR START, IT'S NOT WORKING YET
     */
    private void initXml() {
        file = new File(directory.concat("/" + xmlPath));
        entities = new HashMap<>();

        if (!file.exists()) {
            createXmlFile();
            return;
        }

        // read records to document reference
        readXmlFile();
        saveXml();
    }

    private void readXmlFile() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            xmlFile = dBuilder.parse(file);
            xmlFile.getDocumentElement().normalize();

            Element databaseElement = xmlFile.getDocumentElement();

            NodeList entitiesNodes = databaseElement.getChildNodes();

            for (int i = 0; i < entitiesNodes.getLength(); i++) {
                Node entityNode = entitiesNodes.item(i);

                if (entityNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element entityElement = (Element) entityNode;
                    String entityType = entityElement.getTagName();
                    entities.put(entityType, entityElement);

                    NodeList records = entityElement.getChildNodes();

                    // for each records in entity element
                    for (int j = 0; j < records.getLength(); j++) {
                        Node nodeRecord = records.item(j);

                        if (nodeRecord.getNodeType() == Node.ELEMENT_NODE) {
                            Element recordElement = (Element) nodeRecord;
                            loadRecord(entityType, recordElement);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createXmlFile() {
        try {
            buildSkeleton();
            saveXml();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildSkeleton() {
        try {
            // create dir if not exists
            File dir = new File(directory);
            if (!dir.exists()) {
                dir.mkdir();
                file = new File(directory.concat("/" + xmlPath));
            }

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            xmlFile = docBuilder.newDocument();
            Element rootElement = xmlFile.createElement("database");
            xmlFile.appendChild(rootElement);

            for (String entityType : Database.getEntityTypes()) {
                Element entity = xmlFile.createElement(entityType);
                rootElement.appendChild(entity);
                entities.put(entityType, entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveXml() {
        try {
            // remove empty lines
            XPath xp = XPathFactory.newInstance().newXPath();
            NodeList nl = (NodeList) xp.evaluate("//text()[normalize-space(.)='']", xmlFile, XPathConstants.NODESET);

            for (int i = 0; i < nl.getLength(); ++i) {
                Node node = nl.item(i);
                node.getParentNode().removeChild(node);
            }

            // write to file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 4);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(xmlFile);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Save record from xml to memory maps in Database.java
     *
     * @param entityType entity type
     * @param record     the record element from xml
     */
    public void loadRecord(String entityType, Element record) {
        int id = getNumber(record.getAttribute("id"));
        switch (entityType) {
            case "boats":
                loadBoat(id, record);
                break;
            case "rowers":
                loadRower(id, record);
                break;
            case "activities":
                loadActivity(id, record);
                break;
            case "orders":
                loadOrder(id, record);
                break;
        }
    }

    public void deleteRecordElement(int id, Entity record) {
        try {
            String recordType = record.getClass().getSimpleName().toLowerCase(),
                    entityType = record.getEntityType();

            XPath xpath = XPathFactory.newInstance().newXPath();
            XPathExpression expression = xpath.compile("//database/" + entityType + "/" + recordType + "[contains" +
                    "(@id,'" + id + "')]");
            Node recordNode = (Node) expression.evaluate(xmlFile, XPathConstants.NODE);
            recordNode.getParentNode().removeChild(recordNode);
            saveXml();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    public void addRecordElement(Integer id, Entity record) {
        Element recordsList = entities.get(record.getEntityType());

        Element recordElement = xmlFile.createElement(record.getClass().getSimpleName().toLowerCase());
        String value;
        Object valueObject;

        // Add fields as attributes for element
        recordElement.setAttribute("id", id.toString());
        for (Field field : record.getClass().getDeclaredFields()) {
            try {
                if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                field.setAccessible(true);
                valueObject = field.get(record);

                if (valueObject == null) {
                    value = "";
                } else {
                    value = valueObject.toString();
                }

                if (field.getType().getSimpleName().equals("List")) {
                    value = arrayListToString(value);
                }

                recordElement.setAttribute(field.getName(), value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        recordsList.appendChild(recordElement);
        saveXml();
    }

    public void updateRecordElement(int id, Entity record) {
        deleteRecordElement(id, record);
        addRecordElement(id, record);
    }

    protected void loadBoat(int id, Element record) {
        Boat.Type type = Boat.Type.valueOf(record.getAttribute("type"));
        String name = record.getAttribute("name");
        boolean isPrivate = getBoolean(record.getAttribute("isPrivate"));
        boolean isWide = getBoolean(record.getAttribute("isWide"));
        boolean isCoastal = getBoolean(record.getAttribute("isCoastal"));
        boolean isDisabled = getBoolean(record.getAttribute("isDisabled"));

        Boat boat = new Boat(type, name, isPrivate, isWide, isCoastal, isDisabled);
        boat.setId(id);
        database.get("boats").put(id, boat);
    }

    protected void loadRower(int id, Element record) {
        String name = record.getAttribute("name");
        Integer age = Integer.parseInt(record.getAttribute("age"));
        LocalDateTime expired = LocalDateTime.parse(record.getAttribute("expired"));
        Rower.Level level = Rower.Level.valueOf(record.getAttribute("level"));
        boolean hasPrivateBoat = getBoolean(record.getAttribute("hasPrivateBoat"));
        Integer privateBoat = getNumber(record.getAttribute("privateBoat"));
        String phoneNumber = record.getAttribute("phoneNumber");
        String notes = record.getAttribute("notes");
        boolean isManager = getBoolean(record.getAttribute("isManager"));
        String emailAddress = record.getAttribute("emailAddress");
        String password = record.getAttribute("password");
        LocalDateTime joined = LocalDateTime.parse(record.getAttribute("joined"));

        Rower rower = new Rower(name, level, age, joined, expired, phoneNumber, emailAddress, password,
                hasPrivateBoat, isManager, notes);
        rower.setId(id);
        rower.setPrivateBoat(privateBoat);
        database.get("rowers").put(id, rower);
    }

    protected void loadActivity(int id, Element record) {
        String title = record.getAttribute("title");
        LocalTime startTime = LocalTime.parse(record.getAttribute("startTime"));
        LocalTime endTime = LocalTime.parse(record.getAttribute("endTime"));
        String boatTypeAttr = record.getAttribute("boatType");
        Boat.Type boatType;
        if (boatTypeAttr.equals("")) {
            boatType = null;
        } else {
            boatType = Boat.Type.valueOf(boatTypeAttr);
        }

        Activity activity = new Activity(title, startTime, endTime, boatType);
        activity.setId(id);
        database.get("activities").put(id, activity);
    }

    protected void loadOrder(int id, Element record) {
        // get rowers
        List<Integer> rowers = new ArrayList<>();
        String[] rowersString = record.getAttribute("rowers").split(",");
        for (String rowerId : rowersString) {
            Integer rowerIdNum = getNumber(rowerId);
            if (rowerIdNum != null) {
                rowers.add(rowerIdNum);
            }
        }

        // get boat types
        List<Boat.Type> boatTypes = new ArrayList<>();
        String[] boatTypesString = record.getAttribute("boatTypes").split(",");
        for (String boatType : boatTypesString) {
            boatTypes.add(Boat.Type.valueOf(boatType));
        }

        Integer registerRower = Integer.parseInt(record.getAttribute("registerRower"));
        LocalDate activityDate = LocalDate.parse(record.getAttribute("activityDate"));
        LocalTime activityStartTime = LocalTime.parse(record.getAttribute("activityStartTime"));
        LocalTime activityEndTime = LocalTime.parse(record.getAttribute("activityEndTime"));
        LocalDateTime registerDate = LocalDateTime.parse(record.getAttribute("registerDate"));
        String activityTitle = record.getAttribute("activityTitle");
        Boolean approvedRequest = getBoolean(record.getAttribute("approvedRequest"));
        Integer boatId = getNumber(record.getAttribute("boat"));

        Order order = new Order(registerRower, rowers, boatTypes, activityDate,
                activityStartTime,
                activityEndTime,
                registerDate,
                activityTitle);
        order.setId(id);
        order.setApprovedRequest(approvedRequest);
        if (approvedRequest && boatId != null) {
            order.setBoat(boatId);
        }
        database.get("orders").put(id, order);
    }

    /**
     * Utilities methods
     */

    public boolean getBoolean(String value) {
        return value.equalsIgnoreCase("true");
    }

    public Integer getNumber(String value) {
        if (value.isEmpty()) {
            return null;
        }
        return Integer.parseInt(value);
    }

    public String arrayListToString(String list) {
        return list.replace(" ", "").replace("]", "").replace("[", "");
    }
}
