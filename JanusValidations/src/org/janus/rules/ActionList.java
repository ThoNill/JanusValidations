package org.janus.rules;


import java.util.ArrayList;


import org.janus.actions.Action;
import org.janus.data.DataContext;
import org.janus.data.DataDescription;

public class ActionList extends RuleListenerImpl implements Action,
		ValidationRuleListenerList {

	private ArrayList<Action> rulesAndActions = null;

	public ActionList() {
		super();
		this.rulesAndActions = new ArrayList<Action>();
	}

	@Override
	public void configure(DataDescription model) {
		configureRulesAndActions(model);
	}

	public void configureListeners(DataDescription model) {
		super.configure(model);
	}

	public ArrayList<Action> getRulesAndActions() {
		return rulesAndActions;
	}

	public void addRuleOrAction(Action a) {
		rulesAndActions.add(a);
		if (a instanceof BasisValidationRule) {
			((BasisValidationRule) a).addRuleListener(this);
		}
	}

	public void removeRuleOrAction(Action a) {
		rulesAndActions.remove(a);
		if (a instanceof BasisValidationRule) {
			((BasisValidationRule) a).removeRuleListener(this);
		}
	}

	void configureRulesAndActions(DataDescription model) {
		for (Action r : rulesAndActions) {
			r.configure(model);
		}
	}

	@Override
	public void perform(DataContext context) {
		for (Action r : rulesAndActions) {
			r.perform(context);
		}
	}

}