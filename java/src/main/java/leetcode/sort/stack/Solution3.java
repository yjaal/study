package leetcode.sort.stack;

/**
 * 3. 无重复字符的最长子串
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * 给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。
 * <p>
 * 示例 1:
 * 输入: "abcabcbb"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
 * <p>
 * 示例 2:
 * 输入: "bbbbb"
 * 输出: 1
 * 解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
 * <p>
 * 示例 3:
 * 输入: "pwwkew"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
 * 请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
 */
public class Solution3 {

	public static void main(String[] args) {
		Solution3 s = new Solution3();
		System.out.println("最大长度: " + s.lengthOfLongestSubstring("asjrgapa"));
		System.out.println("最大长度: " + s.lengthOfLongestSubstring("abcabcbb"));
	}

	public int lengthOfLongestSubstring(String s) {
		if (null == s || s.length() == 0) {
			return 0;
		}
		int max = 1;
		String cur = s.substring(0, 1);
		int len = s.length();
		for (int i = 1, j = 0; i < len; i++) {
			String iStr = s.substring(i, i + 1);
			if (cur.contains(iStr)) {
				j = s.indexOf(iStr, j) + 1;
			}
			cur = s.substring(j, i + 1);
			max = Math.max(max, cur.length());
		}
		return max;
	}


	/**
	 * 最优解
	 */
	public int lengthOfLongestSubstring1(String s) {
		int n = s.length(), a = 0;
		int[] index = new int[128];
		int i = 0;
		for (int j = 0; j < n; j++) {
			i = Math.max(index[s.charAt(j)], i);
			a = Math.max(a, j - i + 1);
			index[s.charAt(j)] = j + 1;
		}
		return a;
	}
}
