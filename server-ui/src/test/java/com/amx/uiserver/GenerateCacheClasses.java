package com.amx.uiserver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.amx.jax.ui.config.WebSecurityConfig;
import com.amx.utils.FileUtil;
//import com.sun.codemodel.JClassAlreadyExistsException;
//import com.sun.codemodel.JCodeModel;
//import com.sun.codemodel.JDefinedClass;
//import com.sun.codemodel.JMethod;
//import com.sun.codemodel.JMod;
//import com.sun.codemodel.JType;
//
//import spoon.Launcher;
//import spoon.reflect.CtModel;
//import spoon.reflect.declaration.CtType;

public class GenerateCacheClasses { // Noncompliant

	public static void main(String[] args) {
		// JCodeModel codeModel = new JCodeModel();
		// try {
		// JDefinedClass definedClass = codeModel._class("com.amx.jax.cache.Sample");
		// codeModel.build(new File("./src/main/java/"));
		// } catch (JClassAlreadyExistsException e) {
		// // ...
		// } catch (IOException e) {
		// // ...
		// }
		//
		// try {
		// getClasses("com.amx.jax");
		// } catch (ClassNotFoundException | IOException e) {
		// e.printStackTrace();
		// }

		// Launcher launcher = new Launcher();
		// launcher.addInputResource(System.getProperty("user.dir") + "/src/main/java");
		// launcher.getEnvironment().setAutoImports(true);
		// launcher.getEnvironment().setNoClasspath(true);
		// launcher.buildModel();
		// CtModel model = launcher.getModel();
		// for (CtType<?> string : model.getAllTypes()) {
		// CacheBoxEnabled isThere =
		// string.getActualClass().getAnnotation(CacheBoxEnabled.class);
		// if (isThere != null) {
		// System.out.println("===" + string.getActualClass().getSimpleName());
		// createClass(string.getActualClass());
		// }
		// }
	}

	public static void createClass(Class<?> class1) {
		// JCodeModel codeModel = new JCodeModel();
		// try {
		// JDefinedClass definedClass = codeModel._class("com.amx.jax.cache." +
		// class1.getSimpleName());
		// definedClass.annotate(Component.class);
		// definedClass._extends(class1);
		// Method[] methods = class1.getDeclaredMethods();
		// for (Method method : methods) {
		// // definedClass.method(JMod.PUBLIC, method.getGenericReturnType(),
		// // method.getName());
		// JType jtype = codeModel.ref(method.getReturnType());
		// JMethod jmethod = definedClass.method(JMod.PUBLIC, jtype, method.getName());
		// System.out.println("====" + method.getGenericReturnType().getTypeName());
		// // method.get
		// // method.getName();
		// }
		// codeModel.build(new File("./src/main/java/"));
		// } catch (JClassAlreadyExistsException e) {
		// // ...
		// } catch (IOException e) {
		// // ...
		// }
	}

	@SuppressWarnings("rawtypes")
	public static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		URL pathS = FileUtil.getExternalResource("src/main/java/");
		Enumeration<URL> resources = ClassLoader.getSystemResources(System.getProperty("user.dir") + "/src/main/java");
		System.out.println("===" + pathS + resources);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
			System.out.println("======" + directory + "====" + packageName);
		}
		return (Class[]) classes.toArray(new Class[classes.size()]);
	}

	public static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".java")) {
				classes.add(
						Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}
}
