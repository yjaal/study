package leetcode.easy;

import java.util.Objects;
import java.util.Stack;

/**
 * 20. Valid Parentheses
 * Given a string containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.
 * An input string is valid if:
 * Open brackets must be closed by the same type of brackets.
 * Open brackets must be closed in the correct order.
 * Note that an empty string is also considered valid.
 * <p>
 * Example 1:
 * Input: "()"
 * Output: true
 * <p>
 * Example 2:
 * Input: "()[]{}"
 * Output: true
 * <p>
 * Example 3:
 * Input: "(]"
 * Output: false
 * <p>
 * Example 4:
 * Input: "([)]"
 * Output: false
 * <p>
 * Example 5:
 * Input: "{[]}"
 * Output: true
 */
public class Solution20 {
	public static void main(String[] args) {
		Solution20 s = new Solution20();
		System.out.println(s.isValid1("([)]"));
	}

	public boolean isValid(String s) {
		Stack stack = new Stack();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
				case ')':
					if (stack.size() > 0 && Objects.equals(stack.peek(), '(')) {
						stack.pop();
					} else {
						stack.push(ch);
					}
					break;
				case '}':
					if (stack.size() > 0 && Objects.equals(stack.peek(), '{')) {
						stack.pop();
					} else {
						stack.push(ch);
					}
					break;
				case ']':
					if (stack.size() > 0 && Objects.equals(stack.peek(), '[')) {
						stack.pop();
					} else {
						stack.push(ch);
					}
					break;
				default:
					stack.push(ch);
					break;
			}
		}
		return stack.size() == 0;
	}

	//最佳答案
	public boolean isValid1(String s) {
		char[] map = new char[128];
		map[')'] = '(';
		map[']'] = '[';
		map['}'] = '{';
		char[] str = s.toCharArray();
		int top = -1;

		//top总是指向还没有比较的位置，如果这个位置和当前获取到的字符能配对，
		//那么top就减，不能配对就加，最终如果top只回到了-1那么就表示能完全配对
		for (char c : str) {
			if (c == '(' || c == '[' || c == '{') {
				str[++top] = c;
			} else if (top >= 0 && map[c] == str[top]) {
				top--;
			} else {
				return false;
			}
		}
		return top < 0;
	}
}
