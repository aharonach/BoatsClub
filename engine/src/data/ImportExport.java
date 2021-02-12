package data;

import controllers.Activities;
import controllers.Boats;
import controllers.Rowers;
import engine.BCEngine;
import entities.Rower;
import jaxb.activities.Timeframe;
import jaxb.boats.Boat;
import jaxb.boats.BoatType;
import jaxb.members.Member;
import jaxb.members.Members;
import jaxb.members.RowingLevel;
import org.xml.sax.SAXException;
import exceptions.InvalidInputException;
import exceptions.RecordAlreadyExistsException;
import exceptions.RecordNotFoundException;
import wrappers.ActivityWrapper;
import wrappers.BoatWrapper;
import wrappers.RowerWrapper;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ImportExport {

    private final static String JAXB_PACKAGE = "jaxb";
    private final static String SCHEME_LOCATION_PREFIX = "resources/";

    public ImportExport() {}

    private Object importFromFile(String filepath, String scheme) throws IOException, JAXBException, SAXException {
        File file = new File(filepath);

        if (!file.exists() || file.isDirectory()) {
            throw new IOException("File is not valid.");
        }

        return createUnmarshaller(new FileInputStream(file), scheme);
    }

    private Object importFromString(String xmlString, String scheme) throws JAXBException, SAXException {
        InputStream targetStream = new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8));
        return createUnmarshaller(targetStream, scheme);
    }

    private File exportToFile(String filepath) throws IOException {
        File file = new File(filepath);

        if (file.exists() && !file.canWrite()) {
            throw new IOException("File is not valid.");
        }

        return file;
    }

    private String readContentFromFile(File file) throws IOException {
        if (!file.exists() || file.isDirectory()) {
            throw new IOException("File is not exists. please try again.");
        }

        StringBuilder fileContents = new StringBuilder((int)file.length());

        try (Scanner scanner = new Scanner(file)) {
            while(scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine()).append(System.lineSeparator());
            }
            return fileContents.toString();
        }
    }

    public List<String> importProcess(String entityType, String fileContents, boolean override) {
        List<String> feedback = new ArrayList<>();
        try {
            switch (entityType) {
                case "rowers":
                    Members members = (Members) importFromString(fileContents, "members");
                    importMembers(feedback, members, override);
                    break;
                case "boats":
                    jaxb.boats.Boats boats = (jaxb.boats.Boats) importFromString(fileContents, "boats");
                    importBoats(feedback, boats, override);
                    break;
                case "activities":
                    jaxb.activities.Activities activities = (jaxb.activities.Activities) importFromString(fileContents, "activities");
                    importActivities(feedback, activities);
                    break;
            }
        } catch (SAXException e) {
            feedback.add(e.getMessage());
        } catch (JAXBException e) {
            if (e.getLinkedException() != null) {
                feedback.add(e.getLinkedException().getMessage());
            } else {
                feedback.add(e.getMessage());
            }
        }
        return feedback;
    }

    /**
     * Import entities
     */
    private void importMembers(List<String> feedback, Members members, boolean override) {
        int count = 0;
        RowerWrapper wrapper;
        Rowers controller = (Rowers) BCEngine.instance().getController("rowers");
        for (Member member : members.getMember()) {
            try {
                int id = getIdFromElement(member.getId(), override);
                if (member.getName() == null || member.getName().equals("")) {
                    throw new InvalidInputException("Rower name is empty");
                }
                if (member.getLevel() == null) {
                    throw new InvalidInputException("Rower level is empty");
                }
                if (member.getAge() == null) {
                    throw new InvalidInputException("Rower age is empty");
                }
                if (member.getEmail() == null || member.getEmail().equals("")) {
                    throw new InvalidInputException("Rower email is empty");
                }
                if (member.getPassword() == null || member.getPassword().equals("")) {
                    throw new InvalidInputException("Rower password is empty");
                }
                wrapper = new RowerWrapper(
                        id,
                        member.getName(),
                        member.getAge(),
                        member.getMembershipExpiration().toGregorianCalendar().toZonedDateTime().toLocalDateTime(),
                        Rower.Level.valueOf(member.getLevel().value().toUpperCase()),
                        member.getPrivateBoatId() != null,
                        member.getPrivateBoatId() != null ? Integer.parseInt(member.getPrivateBoatId()) : null,
                        nullValueToEmptyString(member.getPhone()),
                        member.getComments(),
                        member.isManager() != null ? member.isManager() : false,
                        member.getEmail(),
                        member.getPassword(),
                        member.getJoined().toGregorianCalendar().toZonedDateTime().toLocalDateTime()
                );

                if (override) {
                    controller.update(id, wrapper);
                } else {
                    controller.add(wrapper);
                }
                count++;
            } catch (RecordAlreadyExistsException | RecordNotFoundException | InvalidInputException | IllegalArgumentException e) {
                feedback.add("Error: " + e.getMessage());
            }
        }
        feedback.add("\nSummary:\n" + count + " Rowers " + (override ? "updated" : "imported") + ".");
    }

    private void importBoats(List<String> feedback, jaxb.boats.Boats boats, boolean override) {
        int count = 0;
        BoatWrapper wrapper;
        Boats controller = (Boats) BCEngine.instance().getController("boats");
        for (Boat boat : boats.getBoat()) {
            int id = getIdFromElement(boat.getId(), override);
            try {
                if (boat.getType() == null) {
                    throw new InvalidInputException("Boat type is empty.");
                }
                if (boat.getName() == null || boat.getName().equals("")) {
                    throw new InvalidInputException("Boat name is empty.");
                }
                wrapper = new BoatWrapper(
                        id,
                        boat.getName(),
                        entities.Boat.Type.valueOf(boat.getType().value()),
                        boat.isPrivate() != null,
                        boat.isWide() != null,
                        boat.isCostal() != null,
                        boat.isOutOfOrder() != null
                );

                if (override) {
                    controller.update(id, wrapper);
                } else {
                    controller.add(wrapper);
                }
                count++;
            } catch (RecordAlreadyExistsException | RecordNotFoundException | InvalidInputException | IllegalArgumentException e) {
                feedback.add("Error: " + e.getMessage());
            }
        }
        feedback.add("\nSummary:\n" + count + " Boats " + (override ? "updated" : "imported") + ".");
    }

    private void importActivities(List<String> feedback, jaxb.activities.Activities activities) {
        int count = 0;
        ActivityWrapper wrapper;
        Activities controller = (Activities) BCEngine.instance().getController("activities");
        for (Timeframe timeframe : activities.getTimeframe()) {
            try {
                if (timeframe.getName() == null || timeframe.getName().equals("")) {
                    throw new InvalidInputException("Activity name is empty");
                }
                if (timeframe.getEndTime() == null || timeframe.getEndTime().equals("")) {
                    throw new InvalidInputException("Activity end time is empty");
                }
                if (timeframe.getStartTime() == null || timeframe.getStartTime().equals("")) {
                    throw new InvalidInputException("Activity start time is empty");
                }
                wrapper = new ActivityWrapper(
                        timeframe.getName(),
                        LocalTime.parse(timeframe.getStartTime()),
                        LocalTime.parse(timeframe.getEndTime()),
                        timeframe.getBoatType() != null ?
                                entities.Boat.Type.valueOf(timeframe.getBoatType().value()) : null
                );
                controller.add(wrapper);
                count++;
            } catch (RecordAlreadyExistsException | InvalidInputException | IllegalArgumentException e) {
                feedback.add("Error: " + e.getMessage());
            }
        }
        feedback.add("\nSummary:\n" + count + " Activities imported.");
    }

    /**
     * Export entities
     */

    public String exportProcess(String entityType) {
        String exported = null;
        try {
            File file = exportToFile(entityType.concat(String.valueOf(System.currentTimeMillis())).concat(".xml")); // create temp file
            switch (entityType) {
                case "rowers":
                    exportMembers(file);
                    break;
                case "boats":
                    exportBoats(file);
                    break;
                case "activities":
                    exportActivities(file);
                    break;
            }
            exported = readContentFromFile(file);
            System.out.println("exported: " + exported);
            file.delete();
        } catch (JAXBException e) {
            System.out.println("here 1");
            if (e.getLinkedException() != null) {
                System.out.println("here 2");
                System.out.println("Error export: " + e.getLinkedException().getMessage());
            } else {
                System.out.println("here 3");
                System.out.println("Error export: " + e.getMessage());
            }
        } catch (IOException | SAXException e) {
            System.out.println("here 4");
            System.out.println("Error export: " + e.getMessage());
        }
        return exported;
    }

    private void exportMembers(File file) throws JAXBException, SAXException {
        Rowers controller = (Rowers) BCEngine.instance().getController("rowers");
        Rower[] rowers = controller.getList();
        if (rowers.length == 0)
            return;

        Marshaller marshaller = createMarshaller("members");
        Members members = new Members();
        List<Member> membersList = members.getMember();
        for (Rower rower : rowers) {
            try {
                Member member = new Member();
                member.setId(Integer.toString(rower.getId()));
                member.setName(rower.getName());
                member.setAge(rower.getAge());
                if (!rower.getNotes().equals("")) {
                    member.setComments(rower.getNotes());
                }
                member.setLevel(RowingLevel.valueOf(rower.getLevel().name()));
                member.setJoined(DatatypeFactory.newInstance().newXMLGregorianCalendar(rower.getJoined().toString()));
                member.setMembershipExpiration(DatatypeFactory.newInstance().newXMLGregorianCalendar(String.valueOf(rower.getExpired())));
                member.setHasPrivateBoat(rower.hasPrivateBoat());
                if (rower.getPrivateBoat() != null) {
                    member.setPrivateBoatId(Integer.toString(rower.getPrivateBoat()));
                }
                member.setPhone(rower.getPhoneNumber());
                member.setEmail(rower.getEmailAddress());
                member.setPassword(rower.getPassword());
                member.setManager(rower.isManager());
                membersList.add(member);
            } catch (DatatypeConfigurationException | IllegalArgumentException e) {
                System.out.println("Error export: " + e.getMessage());
            }
        }
        marshaller.marshal(members, file);
    }

    private void exportBoats(File file) throws JAXBException, SAXException {
        Boats controller = (Boats) BCEngine.instance().getController("boats");
        entities.Boat[] boatsInDB = controller.getList();
        if (boatsInDB.length == 0)
            return;

        Marshaller marshaller = createMarshaller("boats");
        jaxb.boats.Boats boats = new jaxb.boats.Boats();
        List<Boat> boatsList = boats.getBoat();
        for (entities.Boat boat : boatsInDB) {
            try {
                Boat boatEl = new Boat();
                boatEl.setId(Integer.toString(boat.getId()));
                boatEl.setName(boat.getName());
                boatEl.setType(BoatType.valueOf(boat.getType().name().toUpperCase()));
                boatEl.setPrivate(boat.isPrivate());
                boatEl.setWide(boat.isWide());
                boatEl.setHasCoxswain(boat.getType().hasCoxswain());
                boatEl.setCostal(boat.isCoastal());
                boatEl.setOutOfOrder(boat.isDisabled());
                boatsList.add(boatEl);
            } catch (IllegalArgumentException e) {
                System.out.println("Error export: " + e.getMessage());
            }
        }
        marshaller.marshal(boats, file);
    }

    private void exportActivities(File file) throws JAXBException, SAXException {
        Activities controller = (Activities) BCEngine.instance().getController("activities");
        entities.Activity[] activitiesInDB = controller.getList();
        if (activitiesInDB.length == 0)
            return;

        Marshaller marshaller = createMarshaller("activities");
        jaxb.activities.Activities activities = new jaxb.activities.Activities();
        List<Timeframe> timeframes = activities.getTimeframe();
        for (entities.Activity activity : activitiesInDB) {
            try {
                Timeframe timeframe = new Timeframe();
                timeframe.setName(activity.getTitle());
                timeframe.setStartTime(activity.getStartTime().toString());
                timeframe.setEndTime(activity.getEndTime().toString());
                if (activity.getBoatType() != null) {
                    timeframe.setBoatType(jaxb.activities.BoatType.valueOf(activity.getBoatType().name().toUpperCase()));
                }
                timeframes.add(timeframe);
            } catch (IllegalArgumentException e) {
                System.out.println("here 0");
                System.out.println("Error export: " + e.getMessage());
            }
        }
        marshaller.marshal(activities, file);
    }

    private Object createUnmarshaller(InputStream in, String scheme) throws JAXBException, SAXException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_PACKAGE);
        Unmarshaller u = jc.createUnmarshaller();
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(new File(SCHEME_LOCATION_PREFIX + scheme.concat(".xsd")));
        u.setSchema(schema);
        return u.unmarshal(in);
    }

    private Marshaller createMarshaller(String scheme) throws JAXBException, SAXException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_PACKAGE);
        Marshaller m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(new File(SCHEME_LOCATION_PREFIX + scheme.concat(".xsd")));
        m.setSchema(schema);
        return m;
    }

    private String nullValueToEmptyString(String value) {
        return value == null ? "" : value;
    }

    private int getIdFromElement(String id, boolean override) throws IllegalArgumentException {
        if (override) {
            if (id == null || id.equals("") || id.isEmpty()) {
                throw new IllegalArgumentException("Id field is missing.");
            }
            return Integer.parseInt(id);
        }
        return 0;
    }
}
