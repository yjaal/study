package aio.server;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 */
public class Calculator {
	private final static ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");

	public static Object cal(String expression) throws ScriptException {
		return jse.eval(expression);
	}
}
