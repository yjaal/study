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
		int[] nums = new int[]{4, 0, 2, 3};
		System.out.println(s.maxProduct1(nums));
	}

	public int maxProduct(int[] nums) {
		int len = nums.length;
		if (len == 1) {
			return nums[0];
		}
		int maxPre = nums[0], minPre = nums[0], max = nums[0], maxCur, minCur;
		for (int i = 1; i < nums.length; i++) {
			maxCur = Math.max(nums[i], Math.max(maxPre * nums[i], minPre * nums[i]));
			minCur = Math.min(nums[i], Math.min(maxPre * nums[i], minPre * nums[i]));
			maxPre = maxCur;
			minPre = minCur;
			max = Math.max(max, maxCur);
		}
		return max;
	}

	/**
	 * 复杂度较低解法
	 * 之所以要循环两次，是因为可能会有负值的情况
	 * 如： -2 3 0 4
	 * 第一次循环完后max = 4
	 * 第二次循环完max = 3
	 * 最后结果为4
	 */
	public int maxProduct1(int[] nums) {
		int a = 1;
		int max = nums[0];

		for (int num : nums) {
			a = a * num;
			if (max < a) {
				max = a;
			}
			if (num == 0) {
				a = 1;
			}

		}
		a = 1;
		for (int i = nums.length - 1; i >= 0; i--) {
			a = a * nums[i];
			if (max < a) {
				max = a;
			}
			if (nums[i] == 0) {
				a = 1;
			}
		}
		return max;
	}

}
