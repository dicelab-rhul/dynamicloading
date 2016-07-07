package uk.ac.rhul.cs.dice.loader;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import uk.ac.rhul.cs.dice.gawl.interfaces.actions.Action;
import uk.ac.rhul.cs.dice.gawl.interfaces.actions.speech.SpeechWrapper;
import uk.ac.rhul.cs.dice.gawl.interfaces.perception.Perception;

public class VacuumWorldAgentMind {
	private Perception currentPerception;
	private List<SpeechWrapper> receivedSpeeches;
	private List<Action> availableActions;
	
	public Action decide() {
		try {
		    File file = new File("/home/cloudstrife9999/toload.jar");
		    URL url = file.toURI().toURL();

		    URLClassLoader classLoader = new URLClassLoader(new URL[]{url}, this.getClass().getClassLoader());
		    Class<?> classToLoad = Class.forName("uk.ac.rhul.cs.dice.toload.DecisionMaker", true, classLoader);
		    
		    doReflection(classToLoad, "updateAvailableActions", new Object[]{this.availableActions}, List.class);
		    doReflection(classToLoad, "updatePerception", new Object[]{this.currentPerception}, Perception.class);
		    doReflection(classToLoad, "updateReceivedSpeeches", new Object[]{this.receivedSpeeches}, List.class);
		    
		    return getDecision(classToLoad);
		    
		} catch (Exception e) {
		    e.printStackTrace();
		    return null;
		}
	}

	private Action getDecision(Class<?> classToLoad) throws InstantiationException, NoSuchMethodException, IllegalAccessException, InvocationTargetException  {
		Object result = doReflection(classToLoad, "decide");
		
	    if(Action.class.isAssignableFrom(result.getClass())) {
	    	return (Action) result;
	    }
	    else {
	    	throw new IllegalArgumentException("Bad result.");
	    }
	}
	
	private Object doReflection(Class<?> classToLoad, String methodName) throws InstantiationException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Method method = classToLoad.getDeclaredMethod(methodName);
		Object instance = classToLoad.newInstance();
		
		return method.invoke(instance);
	}
	
	private Object doReflection(Class<?> classToLoad, String methodName, Object[] parameters, Class<?>... parametersTypes) throws InstantiationException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Method method = classToLoad.getDeclaredMethod(methodName, parametersTypes);
		Object instance = classToLoad.newInstance();
		
		return method.invoke(instance, parameters);
	}
}