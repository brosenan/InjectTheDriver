package injectthedriver;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.Jsoner;

import injectthedriver.DriverFactory.ClassLoaderFactory;

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
	void testGetProperties() throws JsonException, DriverFactoryException {
		Map<String, String> env = new HashMap<String, String>();
		env.put("JAVA_IO_SERIALIZABLE", "{\"foo\": \"bar\", \"pi\": 3.14}");
		Map<String, Object> r  = DriverFactory.getProperties(Serializable.class, env);
		assertEquals("bar", r.get("foo"));
	}
	
	@Test
	void testCreateDriverFor() throws MalformedURLException, DriverFactoryException, ClassNotFoundException, JsonException {
		ClassLoaderFactory f = mock(ClassLoaderFactory.class);
		ClassLoader cl = mock(ClassLoader.class);
		URL jarUrl = new URL("http://example.com/my.jar");
		when(f.createClassLoader(jarUrl)).thenReturn(cl);
		doReturn(MockClass.class).when(cl).loadClass("foo.Bar");
		
		Map<String, String> env = new HashMap<String, String>();
		String json = "{\"jar\": \"http://example.com/my.jar\", \"class\": \"foo.Bar\", \"foo\": \"bar\"}";
		Map<String, Object> props = (Map<String, Object>) Jsoner.deserialize(json);
		env.put("JAVA_IO_SERIALIZABLE", json);
		Object driver = DriverFactory.createDriverFor(Serializable.class, env, f);
		
		verify(f).createClassLoader(jarUrl);
		verify(cl).loadClass("foo.Bar");
		
		assertThat(driver, instanceOf(MockClass.class));
		assertEquals(((MockClass)driver).props, props);
	}
	
	@Test
	void integrationTest() throws IOException, DriverFactoryException {
		String config  = "{\"jar\": \"https://github.com/brosenan/InjectTheDriver/raw/master/aottest-0.1.0-SNAPSHOT-standalone.jar\", \"class\": \"aottest.Nat\"}";
		Map<String, String> env = new HashMap<>();
		env.put("JAVA_UTIL_ITERATOR", config);
		Iterator<Integer> nat = (Iterator<Integer>)DriverFactory.createDriverFor(Iterator.class, env, new DriverFactory.ClassLoaderFactory());
	}
	
}
