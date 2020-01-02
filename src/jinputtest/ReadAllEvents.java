package jinputtest;

import java.io.File;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

/**
 * This class shows how to use the event queue system in JInput. It will show
 * how to get the controllers, how to get the event queue for a controller, and
 * how to read and process events from the queue.
 * 
 * @author Endolf
 */
public class ReadAllEvents {

	public ReadAllEvents() {
		System.out.println("Starting...");

		//System.setProperty("net.java.games.input.librarypath", new File("/home/pi/code/jinputtest/libs").getAbsolutePath());
		System.setProperty("net.java.games.input.librarypath", new File("libs").getAbsolutePath());

		Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
		if (controllers.length == 0) {
			System.out.println("Found no controllers.");
			System.exit(0);
		}
		System.out.println("Found " + controllers.length + " controllers.");


		while (true) {
			/* Get the available controllers */
			controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();

			for (int i = 0; i < controllers.length; i++) {
				/* Remember to poll each one */
				controllers[i].poll();

				/* Get the controllers event queue */
				EventQueue queue = controllers[i].getEventQueue();

				/* Create an event object for the underlying plugin to populate */
				Event event = new Event();

				/* For each object in the queue */
				while (queue.getNextEvent(event)) {

					/*
					 * Create a string buffer and put in it, the controller name,
					 * the time stamp of the event, the name of the component
					 * that changed and the new value.
					 * 
					 * Note that the timestamp is a relative thing, not
					 * absolute, we can tell what order events happened in
					 * across controllers this way. We can not use it to tell
					 * exactly *when* an event happened just the order.
					 */
					float value = event.getValue();
					//if (value == 0 || value < -0.5 || value > 0.5) {
						StringBuffer buffer = new StringBuffer(controllers[i].getName());
						buffer.append(" at ");
						buffer.append(event.getNanos()).append(", ");
						Component comp = event.getComponent();
						buffer.append(comp.getName()).append(" changed to ");

						/*
						 * Check the type of the component and display an
						 * appropriate value
						 */
						if (comp.isAnalog()) {
							buffer.append(value);
						} else {
							if (value == 1.0f) {
								buffer.append("On (" + value + ")");
							} else {
								buffer.append("Off (" + value + ")");
							}
						}
						System.out.println(buffer.toString());
					//}
				}
			}

			/*
			 * Sleep for 20 milliseconds, in here only so the example doesn't
			 * thrash the system.
			 */
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new ReadAllEvents();
	}
}
