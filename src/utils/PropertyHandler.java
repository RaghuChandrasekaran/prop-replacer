package utils;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class PropertyHandler {
	private PropertiesConfiguration configuration;

	public PropertyHandler(File filename) throws ConfigurationException {
		this.configuration = new PropertiesConfiguration(filename);
		this.configuration.setEncoding("ISO-8859-1");
	}

	public void setProperty(String key, String value) throws ConfigurationException {
		this.configuration.setProperty(key, value);
	}

	public void clearProperty(String key) {
		this.configuration.clearProperty(key);
	}

	public void saveFile() throws ConfigurationException {
		this.configuration.save();
	}

	public boolean containsProperty(String key) {
		return this.configuration.containsKey(key);
	}
}
