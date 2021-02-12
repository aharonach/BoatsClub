package jaxb;

import jaxb.activities.Activities;
import jaxb.activities.Timeframe;
import jaxb.boats.Boat;
import jaxb.boats.Boats;
import jaxb.members.Member;
import jaxb.members.Members;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the jaxb package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: jaxb
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Boats }
     */
    public Boats createBoats() {
        return new Boats();
    }

    /**
     * Create an instance of {@link Boat }
     */
    public Boat createBoat() {
        return new Boat();
    }

    /**
     * Create an instance of {@link Members }
     */
    public Members createMembers() {
        return new Members();
    }

    /**
     * Create an instance of {@link Member }
     */
    public Member createMember() {
        return new Member();
    }

    /**
     * Create an instance of {@link Timeframe }
     */
    public Timeframe createTimeframe() {
        return new Timeframe();
    }

    /**
     * Create an instance of {@link Activities }
     */
    public Activities createActivities() {
        return new Activities();
    }

}
