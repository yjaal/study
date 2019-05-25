package leetcode.sort.dynamic;

import java.util.Arrays;
import java.util.List;

/**
 * 139. 单词拆分
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * 给定一个非空字符串 s 和一个包含非空单词列表的字典 wordDict，判定 s 是否可以被空格拆分为一个或多个在字典中出现的单词。
 * 说明：
 * 拆分时可以重复使用字典中的单词。
 * 你可以假设字典中没有重复的单词。
 * <p>
 * 示例 1：
 * 输入: s = "leetcode", wordDict = ["leet", "code"]
 * 输出: true
 * 解释: 返回 true 因为 "leetcode" 可以被拆分成 "leet code"。
 * <p>
 * 示例 2：
 * 输入: s = "applepenapple", wordDict = ["apple", "pen"]
 * 输出: true
 * 解释: 返回 true 因为 "applepenapple" 可以被拆分成 "apple pen apple"。
 * 注意你可以重复使用字典中的单词。
 * <p>
 * 示例 3：
 * 输入: s = "catsandog", wordDict = ["cats", "dog", "sand", "and", "cat"]
 * 输出: false
 */
public class Solution139 {

	public static void main(String[] args) {
		Solution139 solution = new Solution139();
		String s = "applepenapple";
		List<String> wordDict = Arrays.asList("apple", "pen");
		System.out.println(solution.wordBreak(s, wordDict));
	}

	/**
	 * 记住套路，一个一个遍历，然后看前面i个字符串能否被拆分
	 */
	public boolean wordBreak(String s, List<String> wordDict) {
		boolean[] dp = new boolean[s.length() + 1];
		dp[0] = true;
		for (int i = 1; i <= s.length(); i++) {
			for (int j = 0; j < i; j++) {
				if (dp[j] && wordDict.contains(s.substring(j, i))) {
					dp[i] = true;
					break;
				}
			}
		}
		return dp[s.length()];
	}

	/**
	 * 优化，截取的字符串不能大于字典中最长字符串
	 */
	public boolean wordBreak1(String s, List<String> wordDict) {
		if (s == null || s.length() == 0) {
			return false;
		}
		int maxLength = Integer.MIN_VALUE;
		for (String x : wordDict) {
			if (x.length() > maxLength) {
				maxLength = x.length();
			}
		}
		boolean[] isCon = new boolean[s.length() + 1];
		isCon[0] = true;
		for (int i = 1; i <= s.length(); i++) {
			for (int j = i; j >= 1 && i - j <= maxLength; j--) {
				if (isCon[j - 1] && wordDict.contains(s.substring(j - 1, i))) {
					isCon[i] = true;
					break;
				}
			}
		}
		return isCon[s.length()];
	}
}
