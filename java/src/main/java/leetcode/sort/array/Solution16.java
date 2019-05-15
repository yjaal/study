package leetcode.sort.array;


import java.util.Arrays;

/**
 * 16. 最接近的三数之和
 * <p>
 * 给定一个包括 n 个整数的数组 nums 和 一个目标值 target。找出 nums 中的三个整数，
 * 使得它们的和与 target 最接近。返回这三个数的和。假定每组输入只存在唯一答案。
 * <p>
 * 例如，给定数组 nums = [-1，2，1，-4], 和 target = 1.
 * 与 target 最接近的三个数的和为 2. (-1 + 2 + 1 = 2).
 */
public class Solution16 {
	public static void main(String[] args) {
		Solution16 s = new Solution16();
		int[] nums = new int[]{-3, -2, -5, 3, -4};
		System.out.println(s.threeSumClosest(nums, -1));
	}

	public int threeSumClosest(int[] nums, int target) {
		int res = 0;
		int diff = Integer.MAX_VALUE;
		Arrays.sort(nums);
		for (int i = 0; i < nums.length - 2; i++) {
			if (i > 0 && nums[i] == nums[i - 1]) continue;
			int low = i + 1, high = nums.length - 1;
			while (low < high) {
				int sum = nums[i] + nums[low] + nums[high];
				int diffTmp = Math.abs(target - sum);
				if (diffTmp < diff) {
					res = sum;
					diff = diffTmp;
				}
				if (sum > target) {
					while (low < high && nums[high] == nums[high - 1]) high--;
					high--;
				} else if (sum < target) {
					while (low < high && nums[low] == nums[low + 1]) low++;
					low++;
				} else {
					return sum;
				}
			}
		}
		return res;
	}
}
