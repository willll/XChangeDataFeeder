package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

	private static Config INSTANCE = null;

	protected Properties prop = new Properties();
	protected InputStream input = null;
	protected String Filename = "config.properties";

	private Config(String filename) throws IOException {
		Filename = filename;
		load();
	}

	private Config() throws IOException {
		load();
	}

	public static Config getInstance() throws IOException {
		if (INSTANCE == null) {
			synchronized (Config.class) {
				if (INSTANCE == null) {
					INSTANCE = new Config();
				}
			}
		}
		return INSTANCE;
	}

	public static Config getInstance(String filename) throws IOException {
		if (INSTANCE == null) {
			synchronized (Config.class) {
				if (INSTANCE == null) {
					INSTANCE = new Config(filename);
				}
			}
		}
		return INSTANCE;
	}

	protected void load() throws IOException {
		input = getClass().getClassLoader().getResourceAsStream(Filename);
		prop.load(input);
	}

	public String get(String key) {
		return prop.getProperty(key, null);
	}
	
	public static Boolean isEnabled(String _in) throws IOException {
		return Boolean.parseBoolean(Config.getInstance().get(_in));
	}
}
