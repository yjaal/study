package leetcode.sort.dynamic;

/**
 * 213. 打家劫舍 II
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * 你是一个专业的小偷，计划偷窃沿街的房屋，每间房内都藏有一定的现金。这个地方所有的房屋都围成一圈，
 * 这意味着第一个房屋和最后一个房屋是紧挨着的。同时，相邻的房屋装有相互连通的防盗系统，
 * 如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
 * <p>
 * 给定一个代表每个房屋存放金额的非负整数数组，计算你在不触动警报装置的情况下，能够偷窃到的最高金额。
 * <p>
 * 示例 1:
 * 输入: [2,3,2]
 * 输出: 3
 * 解释: 你不能先偷窃 1 号房屋（金额 = 2），然后偷窃 3 号房屋（金额 = 2）, 因为他们是相邻的。
 * <p>
 * 示例 2:
 * 输入: [1,2,3,1]
 * 输出: 4
 * 解释: 你可以先偷窃 1 号房屋（金额 = 1），然后偷窃 3 号房屋（金额 = 3）。
 * 偷窃到的最高金额 = 1 + 3 = 4 。
 */
public class Solution213 {
	public static void main(String[] args) {
		Solution213 s = new Solution213();
		int[] nums = new int[]{2, 1, 1, 2};
		System.out.println(s.rob(nums));
	}

	public int rob(int[] nums) {
		if (nums == null || nums.length == 0) {
			return 0;
		}
		if (nums.length == 1) {
			return nums[0];
		}
		int len = nums.length;
		// 拿第一个不拿最后一个
		int[] dp1 = new int[len];
		dp1[0] = nums[0];
		dp1[1] = Math.max(dp1[0], dp1[1]);
		// 不拿第一个拿最后一个
		int[] dp2 = new int[len];
		dp2[0] = 0;
		dp2[1] = nums[1];
		for (int i = 2; i < nums.length; i++) {
			dp1[i] = Math.max(nums[i] + dp1[i - 2], dp1[i - 1]);
			dp2[i] = Math.max(nums[i] + dp2[i - 2], dp2[i - 1]);
		}
		//分两种情况
		return Math.max(dp1[len - 2], dp2[len - 1]);
	}
}
