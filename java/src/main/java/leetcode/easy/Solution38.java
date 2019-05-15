package leetcode.easy;

import java.util.Objects;

/**
 * 38. 报数
 * 报数序列是一个整数序列，按照其中的整数的顺序进行报数，得到下一个数。其前五项如下：
 * <p>
 * 1.     1    one 1 --> 11
 * 2.     11   two 1 --> 21
 * 3.     21   one 2 one 1 --> 1211
 * 4.     1211 one 1 one 2 two 1 --> 111221
 * 5.     111221 three 1 two 2 one 1 --> 312211
 * 1 被读作  "one 1"  ("一个一") , 即 11。
 * 11 被读作 "two 1s" ("两个一"）, 即 21。
 * 21 被读作 "one 2",  "one 1" （"一个二" ,  "一个一") , 即 1211。
 * <p>
 * 给定一个正整数 n（1 ≤ n ≤ 30），输出报数序列的第 n 项。
 * <p>
 * 注意：整数顺序将表示为一个字符串。
 * <p>
 * <p>
 * <p>
 * 示例 1:
 * <p>
 * 输入: 1
 * 输出: "1"
 * 示例 2:
 * <p>
 * 输入: 4
 * 输出: "1211"
 */
public class Solution38 {

	public static void main(String[] args) {
		Solution38 s = new Solution38();
		String res = s.countAndSay(5);
		System.out.println(res);
	}

	public String countAndSay(int n) {
		String start = "1";
		for(int i = 1; i < n; i++) {
			start = read(start);
		}
		return start;
	}

	public String read(String pre) {
		int i = 0;
		StringBuilder sb = new StringBuilder();
		int len = pre.length();
		while (i < len) {
			Character ch = pre.charAt(i++);
			int count = 1;
			if (i < pre.length() && Objects.equals(ch, pre.charAt(i))) {
				while (i < pre.length() && Objects.equals(ch, pre.charAt(i))) {
					count++;
					i++;
				}
			}
			sb.append(count);
			sb.append(ch);
		}
		return sb.toString();
	}
}
