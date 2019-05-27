package leetcode.sort.dynamic;

/**
 * 152. 乘积最大子序列
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * 给定一个整数数组 nums ，找出一个序列中乘积最大的连续子序列（该序列至少包含一个数）。
 * <p>
 * 示例 1:
 * 输入: [2,3,-2,4]
 * 输出: 6
 * 解释: 子数组 [2,3] 有最大乘积 6。
 * <p>
 * 示例 2:
 * 输入: [-2,0,-1]
 * 输出: 0
 * 解释: 结果不能为 2, 因为 [-2,-1] 不是子数组。
 */
public class Solution152 {
	public static void main(String[] args) {
		Solution152 s = new Solution152();
		int[] nums = new int[]{2, 3, -2, 4};
		System.out.println(s.maxProduct(nums));
	}

	public int maxProduct(int[] nums) {
		if (nums.length == 1) {
			return nums[0];
		}
		int[] dpMax = new int[nums.length];
		int[] dpMin = new int[nums.length];
		int max = Math.max(Integer.MIN_VALUE, nums[0]);
		dpMax[0] = nums[0] == 0 ? 1 : nums[0];
		dpMin[0] = nums[0] == 0 ? 1 : nums[0];
		for (int i = 1; i < nums.length; i++) {
			if (nums[i] == 0) {
				max = Math.max(dpMax[i], max);
				dpMax[i] = 1;
				dpMin[i] = 1;
			} else if (nums[0] > 0) {
				dpMax[i] = dpMax[i - 1] * nums[i];
				dpMin[i] = dpMin[i - 1] * nums[i];
				max = dpMax[i];
			} else {
				//全部小于0
				if (dpMax[i - 1] < 0) {
					dpMax[i] = dpMin[i - 1] * nums[i];
					dpMin[i] = dpMax[i - 1];
					max = Math.max(dpMax[i], max);
				} else if (dpMin[i - 1] > 0) {
					//或者全部大于0
					dpMax[i] = dpMin[i - 1];
					dpMin[i] = dpMax[i - 1] * nums[i];
				} else {
					dpMax[i] = Math.max(dpMax[i - 1] * nums[i], dpMin[i - 1] * nums[i]);
					dpMax[i] = Math.min(dpMax[i - 1] * nums[i], dpMin[i - 1] * nums[i]);
					max = dpMax[i];
				}
			}
		}
		return max;
	}
}
