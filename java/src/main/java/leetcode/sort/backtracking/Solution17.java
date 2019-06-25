package leetcode.sort.backtracking;

import java.util.ArrayList;
import java.util.List;

/**
 * 17. 电话号码的字母组合
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * 给定一个仅包含数字 2-9 的字符串，返回所有它能表示的字母组合。
 * <p>
 * 给出数字到字母的映射如下（与电话按键相同）。注意 1 不对应任何字母。
 * 1(!@#), 2(abc), 3(def), 4(ghi), 5(jkl), 6(mno)
 * 7(pqrs), 8(tuv), 9(wxyz),
 * <p>
 * 示例:
 * 输入："23"
 * 输出：["ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"].
 * <p>
 * 说明: 尽管上面的答案是按字典序排列的，但是你可以任意选择答案输出的顺序。
 */
public class Solution17 {
	public static void main(String[] args) {
		Solution17 s = new Solution17();
		System.out.println(s.letterCombinations("23").toString());
	}

	List<String> res = new ArrayList<>();

	public List<String> letterCombinations(String digits) {

		if (null == digits || digits.length() == 0) {
			return res;
		}
		String[] strs = new String[]{"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};
		for (int i = 0; i < digits.length(); i++) {
			char[] chars = strs[Integer.valueOf(digits.charAt(i) + "")].toCharArray();
			helper(chars);
		}
		return res;
	}

	public void helper(char[] chars) {
		if (res.size() == 0) {
			for (char ch : chars) {
				res.add(ch + "");
			}
		} else {
			List<String> tmpRes = new ArrayList<>();
			for (String str : res) {
				for (char ch : chars) {
					tmpRes.add(str + ch);
				}
			}
			res = tmpRes;
		}
	}
}
