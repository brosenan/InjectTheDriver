package injectthedriver;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.Jsoner;

class DriverFactoryTest {

	@Test
	void testEnvVarName() {
		assertEquals("JAVA_IO_SERIALIZABLE", DriverFactory.envVarName(Serializable.class));
	}
	
	@Test
	void testJSON() throws JsonException {
		String s = "{\"foo\": \"bar\"}";
		Map<String, Object> m = (Map<String, Object>) Jsoner.deserialize(s);
		assertEquals("bar", (String)m.get("foo"));	
	}
	
	@Test
	void testGetProperties() throws JsonException {
		Map<String, String> env = new HashMap<String, String>();
		env.put("JAVA_IO_SERIALIZABLE", "{\"foo\": \"bar\", \"pi\": 3.14}");
		Map<String, Object> r  = DriverFactory.getProperties(Serializable.class, env);
		assertEquals("bar", r.get("foo"));
	}
	
}
