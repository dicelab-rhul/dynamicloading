package uk.ac.rhul.cs.dice.loader;

import uk.ac.rhul.cs.dice.gawl.interfaces.actions.Action;

public class Main {
	private Main(){}
	
	public static void main(String[] args) {
		VacuumWorldAgentMind mind = new VacuumWorldAgentMind();
		Action action = mind.decide();
		System.out.println(action.getClass().getName());
	}
}