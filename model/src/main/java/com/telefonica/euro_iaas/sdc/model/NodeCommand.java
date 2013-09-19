package com.telefonica.euro_iaas.sdc.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;


@Entity
public class NodeCommand {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@SuppressWarnings("unused")
	@Version
	private Long v;

	@ManyToOne
	private OS os;
	@Column(nullable = false, length = 128)
	private String key;
	@Column(nullable = false, length = 128)
	private String value;

	/**
	 * Default constructor
	 */
	public NodeCommand() {

	}

	/**
	 * <p>
	 * Constructor for NodeCommand.
	 * </p>
	 * 
	 * @param os
	 *            a {@link com.telefonica.euro_iaas.sdc.model.OS} object.
	 * @param key
	 *            a {@link java.lang.String} object.
	 * @param value
	 *            a {@link java.lang.String} object.
	 */
	public NodeCommand(OS os, String key, String value) {
		this.os = os;
		this.key = key;
		this.value = value;
	}

	/**
	 * <p>
	 * Getter for the field <code>os</code>.
	 * </p>
	 * 
	 * @return the os
	 */
	public OS getOS() {
		return os;
	}

	/**
	 * <p>
	 * Setter for the field <code>os</code>.
	 * </p>
	 * 
	 * @param os
	 *            the os to set
	 */
	public void setOS(OS os) {
		this.os = os;
	}

	/**
	 * <p>
	 * Getter for the field <code>key</code>.
	 * </p>
	 * 
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * <p>
	 * Setter for the field <code>key</code>.
	 * </p>
	 * 
	 * @param name
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * <p>
	 * Getter for the field <code>value</code>.
	 * </p>
	 * 
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * <p>
	 * Setter for the field <code>value</code>.
	 * </p>
	 * 
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((os == null) ? 0 : os.hashCode());
		result = prime * result + ((v == null) ? 0 : v.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeCommand other = (NodeCommand) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (os == null) {
			if (other.os != null)
				return false;
		} else if (!os.equals(other.os))
			return false;
		if (v == null) {
			if (other.v != null)
				return false;
		} else if (!v.equals(other.v))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
