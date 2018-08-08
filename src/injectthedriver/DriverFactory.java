package injectthedriver;

import java.io.Serializable;
import java.util.Map;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.Jsoner;

public class DriverFactory {
	public static Object createDriverFor(Class<?> itf) {
		return null;
	}

	static String envVarName(Class<?> itf) {
		return itf.getName().toUpperCase().replaceAll("[.]", "_");
	}

	static Map<String, Object> getProperties(Class<Serializable> itf, Map<String, String> env) throws JsonException {
		String s = env.get(envVarName(itf));
		return (Map<String, Object>) Jsoner.deserialize(s);
	}
}
