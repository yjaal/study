package leetcode.easy;

/**
 * 14. Longest Common Prefix
 * <p>
 * Write a function to find the longest common prefix string amongst an array of strings.
 * If there is no common prefix, return an empty string "".
 * <p>
 * Example 1:
 * Input: ["flower","flow","flight"]
 * Output: "fl"
 * <p>
 * Example 2:
 * Input: ["dog","racecar","car"]
 * Output: ""
 * Explanation: There is no common prefix among the input strings.
 * <p>
 * Note:
 * All given inputs are in lowercase letters a-z.
 * 注意：这里不是要找共同的子字符串，而是找共同的前缀
 */
public class Solution14 {

	public String longestCommonPrefix(String[] strs) {
		if (strs.length < 1) {
			return "";
		}
		String result = strs[0];
		for (int i = 1; i < strs.length; i++) {
			int j = 0;
			while (strs[i].indexOf(result.substring(0, result.length() - j)) != 0
					&& j < result.length()) {
				j++;
			}
			result = result.substring(0, result.length() - j);
		}
		return result;
	}

	public static void main(String[] args) {
		Solution14 s = new Solution14();
		System.out.println(s.longestCommonPrefix(new String[]{"c", "acc", "ccc"}));
		System.out.println("输出" + s.longestCommonPrefix(new String[]{"dog", "racecar", "car"}) + "end");
	}
}
