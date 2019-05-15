package leetcode.sort.array;

import java.util.Arrays;

/**
 * 34. 在排序数组中查找元素的第一个和最后一个位置
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * 给定一个按照升序排列的整数数组 nums，和一个目标值 target。找出给定目标值在数组中的开始位置和结束位置。
 * 你的算法时间复杂度必须是 O(log n) 级别。
 * 如果数组中不存在目标值，返回 [-1, -1]。
 * <p>
 * 示例 1:
 * 输入: nums = [5,7,7,8,8,10], target = 8
 * 输出: [3,4]
 * <p>
 * 示例 2:
 * 输入: nums = [5,7,7,8,8,10], target = 6
 * 输出: [-1,-1]
 */
public class Solution34 {
	public static void main(String[] args) {
		Solution34 s = new Solution34();
		int[] nums = new int[]{3, 3, 3};
		System.out.println(Arrays.toString(s.searchRange(nums, 3)));
	}

	/**
	 * 这种方式的时间复杂度为O(n)
	 */
	public int[] searchRange1(int[] nums, int target) {
		if (nums == null || nums.length == 0) {
			return new int[]{-1, -1};
		}
		int i = 0, j = nums.length - 1;
		while (i <= j && target != nums[i]) {
			i++;
		}
		if (i > j) {
			return new int[]{-1, -1};
		}
		while (i <= j && target != nums[j]) {
			j--;
		}
		if (i > j) {
			return new int[]{-1, -1};
		}
		return new int[]{i, j};
	}

	/**
	 * 二分法，但是这里没有考虑这是一个已排序数组（后面进行了优化）
	 */
	public int[] searchRange(int[] nums, int target) {
		if (nums == null || nums.length == 0) {
			return new int[]{-1, -1};
		}
		return searchRange(nums, target, 0, nums.length - 1);
	}

	private int[] searchRange(int[] nums, int target, int left, int right) {
		if (left > right) {
			return new int[]{-1, -1};
		}
		int middle = (right + left) / 2;
		int[] res = new int[]{-1, -1};
		int[] lefts;
		int[] rights;
		if (target != nums[middle]) {
			if (target > nums[middle]) {
				lefts = new int[]{-1, -1};
				rights = searchRange(nums, target, middle + 1, right);
			} else {
				lefts = searchRange(nums, target, left, middle - 1);
				rights = new int[]{-1, -1};
			}
			if (lefts[0] == -1 && rights[0] != -1) {
				res = rights;
			} else if (lefts[0] != -1 && rights[0] == -1) {
				res = lefts;
			}
		} else {
			lefts = searchRange(nums, target, left, middle - 1);
			rights = searchRange(nums, target, middle + 1, right);
			if (lefts[0] == -1 && rights[0] != -1) {
				res[0] = middle;
				res[1] = rights[1];
			} else if (lefts[0] != -1 && rights[0] == -1) {
				res[0] = lefts[0];
				res[1] = middle;
			} else if (lefts[0] != -1) {
				res[0] = lefts[0];
				res[1] = rights[1];
			} else {
				res[0] = middle;
				res[1] = middle;
			}
		}
		return res;
	}

	/**
	 * 最优解
	 */
	public int[] searchRange2(int[] nums, int target) {
		int start = firstGreaterEqual(nums, target);
		if (start == nums.length || nums[start] != target) {
			return new int[]{-1, -1};
		}
		return new int[]{start, firstGreaterEqual(nums, target + 1) - 1};
	}

	/**
	 * find the first number that is greater than or equal to target.
	 * could return nums.length if target is greater than nums[nums.length-1].
	 * actually this is the same as lower_bound in C++ STL.
	 * 找到第一个大于等于target的数
	 */
	private static int firstGreaterEqual(int[] nums, int target) {
		int low = 0, high = nums.length;
		while (low < high) {
			int mid = low + ((high - low) >> 1);
			//low <= mid < high
			if (nums[mid] < target) {
				low = mid + 1;
			} else {
				//should not be mid-1 when A[mid]==target.
				//could be mid even if A[mid]>target because mid<high.
				high = mid;
			}
		}
		return low;
	}
}
