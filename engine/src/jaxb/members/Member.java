package jaxb.members;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="age" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="comments" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="level" type="{}rowingLevel" />
 *       &lt;attribute name="joined" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="membershipExpiration" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="hasPrivateBoat" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="privateBoatId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="phone" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="email" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="password" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="manager" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "member")
public class Member {

    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "age")
    protected Integer age;
    @XmlAttribute(name = "comments")
    protected String comments;
    @XmlAttribute(name = "level")
    protected RowingLevel level;
    @XmlAttribute(name = "joined")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar joined;
    @XmlAttribute(name = "membershipExpiration")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar membershipExpiration;
    @XmlAttribute(name = "hasPrivateBoat")
    protected Boolean hasPrivateBoat;
    @XmlAttribute(name = "privateBoatId")
    protected String privateBoatId;
    @XmlAttribute(name = "phone")
    protected String phone;
    @XmlAttribute(name = "email", required = true)
    protected String email;
    @XmlAttribute(name = "password", required = true)
    protected String password;
    @XmlAttribute(name = "manager")
    protected Boolean manager;

    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the age property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getAge() {
        return age;
    }

    /**
     * Sets the value of the age property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setAge(Integer value) {
        this.age = value;
    }

    /**
     * Gets the value of the comments property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets the value of the comments property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setComments(String value) {
        this.comments = value;
    }

    /**
     * Gets the value of the level property.
     *
     * @return possible object is
     * {@link RowingLevel }
     */
    public RowingLevel getLevel() {
        return level;
    }

    /**
     * Sets the value of the level property.
     *
     * @param value allowed object is
     *              {@link RowingLevel }
     */
    public void setLevel(RowingLevel value) {
        this.level = value;
    }

    /**
     * Gets the value of the joined property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getJoined() {
        return joined;
    }

    /**
     * Sets the value of the joined property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setJoined(XMLGregorianCalendar value) {
        this.joined = value;
    }

    /**
     * Gets the value of the membershipExpiration property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getMembershipExpiration() {
        return membershipExpiration;
    }

    /**
     * Sets the value of the membershipExpiration property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setMembershipExpiration(XMLGregorianCalendar value) {
        this.membershipExpiration = value;
    }

    /**
     * Gets the value of the hasPrivateBoat property.
     *
     * @return possible object is
     * {@link Boolean }
     */
    public Boolean isHasPrivateBoat() {
        return hasPrivateBoat;
    }

    /**
     * Sets the value of the hasPrivateBoat property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setHasPrivateBoat(Boolean value) {
        this.hasPrivateBoat = value;
    }

    /**
     * Gets the value of the privateBoatId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPrivateBoatId() {
        return privateBoatId;
    }

    /**
     * Sets the value of the privateBoatId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPrivateBoatId(String value) {
        this.privateBoatId = value;
    }

    /**
     * Gets the value of the phone property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the value of the phone property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPhone(String value) {
        this.phone = value;
    }

    /**
     * Gets the value of the email property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Gets the value of the password property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets the value of the manager property.
     *
     * @return possible object is
     * {@link Boolean }
     */
    public Boolean isManager() {
        return manager;
    }

    /**
     * Sets the value of the manager property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setManager(Boolean value) {
        this.manager = value;
    }

}
