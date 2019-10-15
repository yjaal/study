package leetcode.medium;

import java.util.ArrayList;
import java.util.List;

/**
 * 6. Z 字形变换
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 *
 * 将一个给定字符串根据给定的行数，以从上往下、从左到右进行 Z 字形排列。
 * 比如输入字符串为 "LEETCODEISHIRING" 行数为 3 时，排列如下：
 *
 * L   C   I   R
 * E T O E S I I G
 * E   D   H   N
 * 之后，你的输出需要从左往右逐行读取，产生出一个新的字符串，比如："LCIRETOESIIGEDHN"。
 *
 * 请你实现这个将字符串进行指定行数变换的函数：
 * string convert(string s, int numRows);
 *
 * 示例 1:
 * 输入: s = "LEETCODEISHIRING", numRows = 3
 * 输出: "LCIRETOESIIGEDHN"
 *
 * 示例 2:
 * 输入: s = "LEETCODEISHIRING", numRows = 4
 * 输出: "LDREOEIIECIHNTSG"
 * 解释:
 *
 * L     D     R
 * E   O E   I I
 * E C   I H   N
 * T     S     G
 */
public class Solution6 {

	public static void main(String[] args) {
		String s = "PAYPALISHIRING";
		String res = new Solution6().convert(s, 4);
		System.out.println(res);
	}

	public String convert(String s, int numRows) {
		if (null == s || s.length() == 0 || numRows <= 1 || s.length() < numRows) {
			return s;
		}
		List<List<String>> res = new ArrayList<>(numRows);
		int num = 0;
		for(int i = 0; i < numRows; i++) {
			res.add(i, new ArrayList<>());
		}
		int flag = 1;
		for(int i = 0; i < s.length(); i++) {
			if (num < 0) {
				//向下
				flag = 1;
				num += 2;
			}
			if (num >= numRows) {
				//向上
				flag = -1;
				num -= 2;
			}
			res.get(num).add(s.charAt(i) + "");
			num += flag;
		}
		StringBuilder sb = new StringBuilder();
		res.forEach(e -> sb.append(String.join("", e)));
		return sb.toString();
	}
}
