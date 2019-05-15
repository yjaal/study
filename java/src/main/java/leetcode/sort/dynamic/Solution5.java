package leetcode.sort.dynamic;

/**
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * 5. 最长回文子串
 * <p>
 * 给定一个字符串 s，找到 s 中最长的回文子串。你可以假设 s 的最大长度为 1000。
 * <p>
 * 示例 1：
 * 输入: "babad"
 * 输出: "bab"
 * 注意: "aba" 也是一个有效答案。
 * <p>
 * 示例 2：
 * 输入: "cbbd"
 * 输出: "bb"
 */
public class Solution5 {
	public static void main(String[] args) {
		Solution5 s = new Solution5();
		System.out.println("结果为： " + s.longestPalindrome("babad"));

	}

	/**
	 * 动态规划
	 */
	public String longestPalindrome(String s) {
		if (s == null || s.length() <= 1) {
			return s;
		}
		int len = s.length();
		boolean[][] dp = new boolean[len][len];
		String maxStr = "";
		int max = 0;
		for (int j = 0; j < len; j++) {
			for (int i = 0; i <= j; i++) {
				dp[i][j] = (s.charAt(i) == s.charAt(j)) && ((j - i <= 2 || dp[i + 1][j - 1]));
				if (dp[i][j] && max < (j - i + 1)) {
					max = j - i + 1;
					maxStr = s.substring(i, j + 1);
				}
			}
		}
		return maxStr;
	}


	private String res = "";

	/**
	 * 中心扩散法
	 */
	public String longestPalindrome1(String s) {
		if (s == null || s.length() <= 1) {
			return s;
		}
		int len = s.length();
		for (int i = 0; i < len; i++) {
			//扩散的时候要么用单数扩散，要么以偶数扩散
			//也就是说初始字符串长度要么为1，要么为2
			func(s, i, i);
			func(s, i, i + 1);
		}
		return res;
	}

	private void func(String s, int left, int right) {
		while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
			left--;
			right++;
		}
		/**
		 * 以left = 2, right =2 举例，当i = 0, right=4的时候不满足条件
		 * 所以取left + 1到right的子字符串
		 */
		String cur = s.substring(left + 1, right);
		if (cur.length() > res.length()) {
			res = cur;
		}
	}
}
