package de.inmediasp.tutorial.addressbook.type;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AddressList {
    @XmlElement(required = true, name="address")
	protected List<Address> addresses;
	
	public AddressList() {
	}

	public AddressList(List<Address> addresses) {
		this.addresses= addresses;
	}

	public List<Address> getAddresses() {
		return addresses;
	}
}