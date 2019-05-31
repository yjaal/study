package leetcode.sort.dynamic;

/**
 * 300. 最长上升子序列
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * 给定一个无序的整数数组，找到其中最长上升子序列的长度。
 * <p>
 * 示例:
 * 输入: [10,9,2,5,3,7,101,18]
 * 输出: 4
 * 解释: 最长的上升子序列是 [2,3,7,101]，它的长度是 4。
 * <p>
 * 说明:
 * 可能会有多种最长上升子序列的组合，你只需要输出对应的长度即可。
 * 你算法的时间复杂度应该为 O(n2) 。
 * <p>
 * 进阶: 你能将算法的时间复杂度降低到 O(n log n) 吗?
 */
public class Solution300 {

	public static void main(String[] args) {
		Solution300 s = new Solution300();
		System.out.println(s.lengthOfLIS(new int[]{10, 9, 2, 5, 3, 7, 101, 18}));
	}

	public int lengthOfLIS(int[] nums) {
		if (null == nums || nums.length == 0) {
			return 0;
		}
		int len = nums.length;
		int[] dp = new int[len];
		dp[0] = 1;
		for (int i = 1; i < len; i++) {
			int max = Integer.MIN_VALUE;
			for (int j = i - 1; j >= 0; j--) {
				//后大于前
				if (nums[i] > nums[j]) {
					max = Math.max(max, dp[j] + 1);
				}
			}
			dp[i] = max;
		}
		return dp[len - 1];
	}
}
