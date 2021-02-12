package jaxb.activities;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="startTime" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="endTime" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="boatType" type="{}boatType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "timeframe")
public class Timeframe {

    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "startTime", required = true)
    protected String startTime;
    @XmlAttribute(name = "endTime", required = true)
    protected String endTime;
    @XmlAttribute(name = "boatType")
    protected BoatType boatType;

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
     * Gets the value of the startTime property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Sets the value of the startTime property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setStartTime(String value) {
        this.startTime = value;
    }

    /**
     * Gets the value of the endTime property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Sets the value of the endTime property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEndTime(String value) {
        this.endTime = value;
    }

    /**
     * Gets the value of the boatType property.
     *
     * @return possible object is
     * {@link BoatType }
     */
    public BoatType getBoatType() {
        return boatType;
    }

    /**
     * Sets the value of the boatType property.
     *
     * @param value allowed object is
     *              {@link BoatType }
     */
    public void setBoatType(BoatType value) {
        this.boatType = value;
    }

}
