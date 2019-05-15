package leetcode.easy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 1、Two Sum
 * Given an array of integers, return indices of the two numbers such that they add up to a specific target.
 * You may assume that each input would have exactly one solution, and you may not use the same element twice.
 * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
 * 你可以假设每种输入只会对应一个答案。但是，你不能重复利用这个数组中同样的元素。
 * Example:
 * Given nums = [2, 7, 11, 15], target = 9,
 * Because nums[0] + nums[1] = 2 + 7 = 9,
 * return [0, 1].
 */
public class Solution1 {
	/**
	 * 1、暴力解法lue
	 * 2、下面这解法使用的是Map，是可行的
	 */
	public int[] twoSum1(int[] nums, int target) {
		if (nums == null || nums.length < 2) {
			return null;
		}
		int[] result = new int[2];
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i = 0; i < nums.length; i++) {
			if (map.containsKey(target - nums[i])) {
				result[0] = map.get(target - nums[i]);
				result[1] = i;
				return result;
			}
			map.put(nums[i], i);
		}
		return null;
	}

	public int[] twoSum(int[] nums, int target) {
		HashMap<Integer, ArrayList<Integer>> map = new HashMap<>();
		int[] results = new int[2];
		// HashMap用于记录排序前数组元素对应下标
		for (int i = 0; i < nums.length; i++) {
			if (map.containsKey(nums[i])) {
				map.get(nums[i]).add(i);
				continue;
			}
			map.put(nums[i], new ArrayList<Integer>());
			map.get(nums[i]).add(i);
		}
		int i = 0, j = nums.length - 1;
		// 排序后双指针求解
		Arrays.sort(nums);
		while (i < j) {
			if (nums[i] + nums[j] == target) {
				int index1 = map.get(nums[i]).get(0);
				// 重复元素已经访问过一次，从对应位置列表中剔除
				map.get(nums[i]).remove(0);
				int index2 = map.get(nums[j]).get(0);
				// 保证 results[0] < result[1]
				results[0] = Math.min(index1, index2) + 1;
				results[1] = Math.max(index1, index2) + 1;
				return results;
			}
			if (nums[i] + nums[j] > target) {
				j--;
			} else {
				i++;
			}
		}
		return results;
	}
}
