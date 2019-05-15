package leetcode.easy;

/**
 * 35. 搜索插入位置
 * <p>
 * 给定一个排序数组和一个目标值，在数组中找到目标值，并返回其索引。如果目标值不存在于数组中，返回它将会被按顺序插入的位置。
 * 你可以假设数组中无重复元素。
 * <p>
 * 示例 1:
 * <p>
 * 输入: [1,3,5,6], 5
 * 输出: 2
 * 示例 2:
 * <p>
 * 输入: [1,3,5,6], 2
 * 输出: 1
 * 示例 3:
 * <p>
 * 输入: [1,3,5,6], 7
 * 输出: 4
 * 示例 4:
 * <p>
 * 输入: [1,3,5,6], 0
 * 输出: 0
 */
public class Solution35 {
	public static void main(String[] args) {
		Solution35 s = new Solution35();
		int[] nums = new int[]{1, 3, 5, 6};
		int target = 7;
		int res = s.searchInsert(nums, target);
		System.out.println(res);
	}

	public int searchInsert(int[] nums, int target) {
		int len = nums.length;
		if (len == 0) {
			return -1;
		}
		int i = 0;
		for (; i < len; i++) {
			if (nums[i] >= target) {
				break;
			}
		}
		return i;
	}

	//最佳答案，采用二分法
	public int searchInsert1(int[] nums, int target) {
		if (nums.length == 0)
			return -1;
		int left = 0;
		int right = nums.length - 1;
		while (left <= right) {
			int mid = (left + right) / 2;
			if (target == nums[(left + right) / 2])
				return (left + right) / 2;
			if (target < nums[mid]) {
				right = mid - 1;
			} else {
				left = mid + 1;
			}
		}
		return left;
	}
}
