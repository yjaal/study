package leetcode.sort.dynamic;

/**
 * 91. 解码方法
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * 一条包含字母 A-Z 的消息通过以下方式进行了编码：
 * 'A' -> 1
 * 'B' -> 2
 * ...
 * 'Z' -> 26
 * 给定一个只包含数字的非空字符串，请计算解码方法的总数。
 * <p>
 * 示例 1:
 * 输入: "12"
 * 输出: 2
 * 解释: 它可以解码为 "AB"（1 2）或者 "L"（12）。
 * <p>
 * 示例 2:
 * 输入: "226"
 * 输出: 3
 * 解释: 它可以解码为 "BZ" (2 26), "VF" (22 6), 或者 "BBF" (2 2 6) 。
 */
public class Solution91 {
	public static void main(String[] args) {
		String s = "121341";
		System.out.println(s.substring(1, s.length()));
	}

	/**
	 * 这里做复杂了
	 * 其实i位置的字符组合首先要从i - 1的组合继承过来
	 * 然后再看这两个位置组合而成的数字是否>=10且<=26，这部分就是新增的
	 */
	public int numDecodings(String s) {
		//记录组合最后字符为单个时的种类
		int[] dp = new int[s.length()];
		//记录组合最后字符为两个时的种类
		int[] dp1 = new int[s.length()];
		dp[0] = s.charAt(0) == '0' ? 0 : 1;
		for (int i = 1; i < s.length(); i++) {
			if (s.charAt(i - 1) != '0') {
				if (s.charAt(i) == '0') {
					//前面字符不为0，后面字符为0
					dp[i] = 0;
					if (s.charAt(i - 1) - '2' <= 0) {
						dp1[i] = dp[i - 1];
					}
				} else {
					//前面字符不为0，后面字符不为0
					if (Integer.valueOf(s.substring(i - 1, i + 1)) <= 26) {
						dp[i] = dp[i - 1] + dp1[i - 1];
						dp1[i] = dp[i - 1];
					} else {
						dp[i] = dp[i - 1] + dp1[i - 1];
						dp1[i] = 0;
					}
				}
			} else {
				///前面字符为0
				if (s.charAt(i) != '0') {
					dp[i] = dp[i - 1] + dp1[i - 1];
				}
				dp1[i] = 0;
			}
		}
		return dp[s.length() - 1] + dp1[s.length() - 1];
	}

	/**
	 * 最优解
	 * 其实i位置的字符组合首先要从i - 1的组合继承过来
	 * 然后再看这两个位置组合而成的数字是否>=10且<=26，这部分就是新增的
	 */
	public int numDecodings1(String s) {
		int[] nums = new int[s.length() + 1];
		nums[0] = 1;
		nums[1] = s.charAt(0) != '0' ? 1 : 0;
		for (int i = 2; i <= s.length(); i++) {
			//如果前面一个字符不为0，那先从前面继承过来
			if (s.charAt(i - 1) != '0') {
				nums[i] = nums[i - 1];
			}
			//再看前面的数和自己组成的数是否大于等于10且小于等于26
			int twoDigits = (s.charAt(i - 2) - '0') * 10 + s.charAt(i - 1) - '0';
			if (twoDigits >= 10 && twoDigits <= 26) {
				nums[i] += nums[i - 2];
			}
		}
		return nums[s.length()];
	}
}
