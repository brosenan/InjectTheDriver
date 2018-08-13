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

	static Map<String, Object> getProperties(Class<?> itf, Map<String, String> env) throws JsonException, DriverFactoryException {
		String s = env.get(envVarName(itf));
		if(s == null) {
			throw new DriverFactoryException("Environment variable " + envVarName(itf) + " missing");
		}
		return (Map<String, Object>) Jsoner.deserialize(s);
	}

	public static Object createDriverFor(Class<?> itf, Map<String, String> env, ClassLoaderFactory f) throws DriverFactoryException {
		Map<String, Object> props;
		try {
			props = getProperties(itf, env);
			if(!props.containsKey("jar")) {
				throw new DriverFactoryException("Properties must contain a 'jar' attribute");				
			}
			if(!props.containsKey("class")) {
				throw new DriverFactoryException("Properties must contain a 'class' attribute");				
			}
			String jarPath = (String)props.get("jar");
			URL jarURL = new File(jarPath).toURI().toURL();
			ClassLoader cl = f.createClassLoader(jarURL);
			Class<?> cls = cl.loadClass((String)props.get("class"));
			Constructor<?> ctor = cls.getConstructor(Map.class);
			ClassLoader pcl = Thread.currentThread().getContextClassLoader();
			try {
				Thread.currentThread().setContextClassLoader(cl);
				return ctor.newInstance(props);				
			} finally {
				Thread.currentThread().setContextClassLoader(pcl);
			}
		} catch (JsonException | MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
			throw new DriverFactoryException(e);
		}
	}
}
