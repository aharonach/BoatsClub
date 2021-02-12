package jaxb.activities;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}timeframe" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "timeframe"
})
@XmlRootElement(name = "activities")
public class Activities {

    @XmlElement(required = true)
    protected List<Timeframe> timeframe;

    /**
     * Gets the value of the timeframe property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the timeframe property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTimeframe().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Timeframe }
     */
    public List<Timeframe> getTimeframe() {
        if (timeframe == null) {
            timeframe = new ArrayList<Timeframe>();
        }
        return this.timeframe;
    }

}
