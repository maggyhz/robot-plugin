package hudson.plugins.robot.tokens;

import hudson.Extension;
import hudson.FilePath;
import hudson.model.AbstractBuild;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.plugins.robot.RobotBuildAction;
import hudson.plugins.robot.model.RobotResult;
import org.jenkinsci.plugins.tokenmacro.DataBoundTokenMacro;
import org.jenkinsci.plugins.tokenmacro.MacroEvaluationException;

import java.io.IOException;

@Extension(optional = true)
public class RobotFailTokenMacro extends DataBoundTokenMacro {

	@Parameter
	public boolean onlyCritical;

	@Override
	public String evaluate(AbstractBuild<?, ?> context, TaskListener listener,
			String macroName) throws MacroEvaluationException, IOException,
			InterruptedException {
		return evaluate(context, context.getWorkspace(), listener, macroName);
	}

	@Override
	public String evaluate(Run<?, ?> context, FilePath workspace, TaskListener listener, String macroName) throws MacroEvaluationException {
		RobotBuildAction action = context.getAction(RobotBuildAction.class);
		if(action!=null){
			RobotResult result = action.getResult();
			if(onlyCritical)
				return Long.toString(result.getCriticalFailed());
			else
				return Long.toString(result.getOverallFailed());
		}
		return "";
	}

	@Override
	public boolean acceptsMacroName(String macroName) {
		return macroName.equals("ROBOT_FAILED");
	}

}
