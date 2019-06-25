package leetcode.sort.stack;

import java.util.Stack;

/**
 * 150. 逆波兰表达式求值
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * 根据逆波兰表示法，求表达式的值。
 * 有效的运算符包括 +, -, *, / 。每个运算对象可以是整数，也可以是另一个逆波兰表达式。
 * <p>
 * 说明：
 * 整数除法只保留整数部分。
 * 给定逆波兰表达式总是有效的。换句话说，表达式总会得出有效数值且不存在除数为 0 的情况。
 * <p>
 * 示例 1：
 * 输入: ["2", "1", "+", "3", "*"]
 * 输出: 9
 * 解释: ((2 + 1) * 3) = 9
 * <p>
 * 示例 2：
 * 输入: ["4", "13", "5", "/", "+"]
 * 输出: 6
 * 解释: (4 + (13 / 5)) = 6
 * <p>
 * 示例 3：
 * 输入: ["10", "6", "9", "3", "+", "-11", "*", "/", "*", "17", "+", "5", "+"]
 * 输出: 22
 * 解释:
 * ((10 * (6 / ((9 + 3) * -11))) + 17) + 5
 * = ((10 * (6 / (12 * -11))) + 17) + 5
 * = ((10 * (6 / -132)) + 17) + 5
 * = ((10 * 0) + 17) + 5
 * = (0 + 17) + 5
 * = 17 + 5
 * = 22
 */
public class Solution150 {
	public static void main(String[] args) {
		String[] tokens = new String[]{"4", "13", "5", "/", "+" };
		Solution150 s = new Solution150();
		System.out.println(s.evalRPN(tokens));
	}

	public int evalRPN(String[] tokens) {
		Stack<Integer> stack = new Stack<>();
		int i = 0;
		while (i < tokens.length) {
			String str = tokens[i];
			Integer next;
			Integer pre;
			switch (str) {
				case "+":
					stack.push(stack.pop() + stack.pop());
					break;
				case "-":
					next = stack.pop();
					pre = stack.pop();
					stack.push(pre - next);
					break;
				case "*":
					stack.push(stack.pop() * stack.pop());
					break;
				case "/":
					next = stack.pop();
					pre = stack.pop();
					stack.push(pre / next);
					break;
				default:
					stack.push(Integer.valueOf(str));
					break;
			}
			i++;
		}
		return stack.pop();
	}
}
