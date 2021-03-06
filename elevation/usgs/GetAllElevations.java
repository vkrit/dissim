
package elevation.usgs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="X_Value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Y_Value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Elevation_Units" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "xValue",
    "yValue",
    "elevationUnits"
})
@XmlRootElement(name = "getAllElevations")
public class GetAllElevations {

    @XmlElement(name = "X_Value")
    protected String xValue;
    @XmlElement(name = "Y_Value")
    protected String yValue;
    @XmlElement(name = "Elevation_Units")
    protected String elevationUnits;

    /**
     * Gets the value of the xValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXValue() {
        return xValue;
    }

    /**
     * Sets the value of the xValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXValue(String value) {
        this.xValue = value;
    }

    /**
     * Gets the value of the yValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getYValue() {
        return yValue;
    }

    /**
     * Sets the value of the yValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setYValue(String value) {
        this.yValue = value;
    }

    /**
     * Gets the value of the elevationUnits property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getElevationUnits() {
        return elevationUnits;
    }

    /**
     * Sets the value of the elevationUnits property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setElevationUnits(String value) {
        this.elevationUnits = value;
    }

}
