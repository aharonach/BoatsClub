package jaxb.members;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for rowingLevel.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="rowingLevel">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Beginner"/>
 *     &lt;enumeration value="Intermediate"/>
 *     &lt;enumeration value="Advanced"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "rowingLevel")
@XmlEnum
public enum RowingLevel {

    @XmlEnumValue("Beginner")
    BEGINNER("Beginner"),
    @XmlEnumValue("Intermediate")
    INTERMEDIATE("Intermediate"),
    @XmlEnumValue("Advanced")
    ADVANCED("Advanced");
    private final String value;

    RowingLevel(String v) {
        value = v;
    }

    public static RowingLevel fromValue(String v) {
        for (RowingLevel c : RowingLevel.values()) {
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
