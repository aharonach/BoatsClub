package jaxb.activities;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for boatType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="boatType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Single"/>
 *     &lt;enumeration value="Double"/>
 *     &lt;enumeration value="Coxed_Double"/>
 *     &lt;enumeration value="Pair"/>
 *     &lt;enumeration value="Coxed_Pair"/>
 *     &lt;enumeration value="Four"/>
 *     &lt;enumeration value="Coxed_Four"/>
 *     &lt;enumeration value="Quad"/>
 *     &lt;enumeration value="Coxed_Quad"/>
 *     &lt;enumeration value="Octuple"/>
 *     &lt;enumeration value="Eight"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "boatType")
@XmlEnum
public enum BoatType {

    @XmlEnumValue("Single")
    SINGLE("Single"),
    @XmlEnumValue("Double")
    DOUBLE("Double"),
    @XmlEnumValue("Coxed_Double")
    COXED_DOUBLE("Coxed_Double"),
    @XmlEnumValue("Pair")
    PAIR("Pair"),
    @XmlEnumValue("Coxed_Pair")
    COXED_PAIR("Coxed_Pair"),
    @XmlEnumValue("Four")
    FOUR("Four"),
    @XmlEnumValue("Coxed_Four")
    COXED_FOUR("Coxed_Four"),
    @XmlEnumValue("Quad")
    QUAD("Quad"),
    @XmlEnumValue("Coxed_Quad")
    COXED_QUAD("Coxed_Quad"),
    @XmlEnumValue("Octuple")
    OCTUPLE("Octuple"),
    @XmlEnumValue("Eight")
    EIGHT("Eight");
    private final String value;

    BoatType(String v) {
        value = v;
    }

    public static BoatType fromValue(String v) {
        for (BoatType c : BoatType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    public String value() {
        return value;
    }

}
