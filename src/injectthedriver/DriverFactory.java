package injectthedriver;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.Jsoner;

public class DriverFactory {
	public static class ClassLoaderFactory {

		public ClassLoader createClassLoader(URL url) {
			return new URLClassLoader(new URL[] {url});
		}

	}

	public static Object createDriverFor(Class<?> itf) throws DriverFactoryException {
		return createDriverFor(itf, System.getenv(), new ClassLoaderFactory());
	}

	static String envVarName(Class<?> itf) {
		return itf.getName().toUpperCase().replaceAll("[.]", "_");
	}

	static Map<String, Object> getProperties(Class<?> itf, Map<String, String> env) throws JsonException {
		String s = env.get(envVarName(itf));
		return (Map<String, Object>) Jsoner.deserialize(s);
	}

	public static Object createDriverFor(Class<?> itf, Map<String, String> env, ClassLoaderFactory f) throws DriverFactoryException {
		Map<String, Object> props;
		try {
			props = getProperties(itf, env);
			ClassLoader cl = f.createClassLoader(new File((String)props.get("jar")).toURI().toURL());
			Class<?> cls = cl.loadClass((String)props.get("class"));
			Constructor<?> ctor = cls.getConstructor(Map.class);
			return ctor.newInstance(props);
		} catch (JsonException | MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
			throw new DriverFactoryException(e);
		}
	}
}
