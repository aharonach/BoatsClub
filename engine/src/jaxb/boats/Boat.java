package jaxb.boats;

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
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="type" use="required" type="{}BoatType" />
 *       &lt;attribute name="private" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="wide" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="hasCoxswain" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="costal" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="outOfOrder" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "boat")
public class Boat {

    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "type", required = true)
    protected BoatType type;
    @XmlAttribute(name = "private")
    protected Boolean _private;
    @XmlAttribute(name = "wide")
    protected Boolean wide;
    @XmlAttribute(name = "hasCoxswain")
    protected Boolean hasCoxswain;
    @XmlAttribute(name = "costal")
    protected Boolean costal;
    @XmlAttribute(name = "outOfOrder")
    protected Boolean outOfOrder;

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
     * Gets the value of the type property.
     *
     * @return possible object is
     * {@link BoatType }
     */
    public BoatType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value allowed object is
     *              {@link BoatType }
     */
    public void setType(BoatType value) {
        this.type = value;
    }

    /**
     * Gets the value of the private property.
     *
     * @return possible object is
     * {@link Boolean }
     */
    public Boolean isPrivate() {
        return _private;
    }

    /**
     * Sets the value of the private property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setPrivate(Boolean value) {
        this._private = value;
    }

    /**
     * Gets the value of the wide property.
     *
     * @return possible object is
     * {@link Boolean }
     */
    public Boolean isWide() {
        return wide;
    }

    /**
     * Sets the value of the wide property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setWide(Boolean value) {
        this.wide = value;
    }

    /**
     * Gets the value of the hasCoxswain property.
     *
     * @return possible object is
     * {@link Boolean }
     */
    public Boolean isHasCoxswain() {
        return hasCoxswain;
    }

    /**
     * Sets the value of the hasCoxswain property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setHasCoxswain(Boolean value) {
        this.hasCoxswain = value;
    }

    /**
     * Gets the value of the costal property.
     *
     * @return possible object is
     * {@link Boolean }
     */
    public Boolean isCostal() {
        return costal;
    }

    /**
     * Sets the value of the costal property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setCostal(Boolean value) {
        this.costal = value;
    }

    /**
     * Gets the value of the outOfOrder property.
     *
     * @return possible object is
     * {@link Boolean }
     */
    public Boolean isOutOfOrder() {
        return outOfOrder;
    }

    /**
     * Sets the value of the outOfOrder property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setOutOfOrder(Boolean value) {
        this.outOfOrder = value;
    }

}
