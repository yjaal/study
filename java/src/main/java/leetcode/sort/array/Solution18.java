package leetcode.sort.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 18. 四数之和
 * <p>
 * 给定一个包含 n 个整数的数组 nums 和一个目标值 target，判断 nums 中是否存在四个元素 a，b，c 和 d ，使得 a + b + c + d 的值与 target 相等？找出所有满足条件且不重复的四元组。
 * 注意：
 * 答案中不可以包含重复的四元组。
 * <p>
 * 示例：
 * 给定数组 nums = [1, 0, -1, 0, -2, 2]，和 target = 0。
 * 满足要求的四元组集合为：
 * [
 * [-1,  0, 0, 1],
 * [-2, -1, 1, 2],
 * [-2,  0, 0, 2]
 * ]
 */
public class Solution18 {
	public static void main(String[] args) {
		Solution18 s = new Solution18();
		int[] nums = new int[]{1, 0, -1, 0, -2, 2};
		List<List<Integer>> res = s.fourSum(nums, 0);
		for (List<Integer> list : res) {
			list.forEach(System.out::print);
			System.out.println(" ");
		}
	}

	public List<List<Integer>> fourSum(int[] nums, int target) {
		int len = nums.length;
		if (len < 4) {
			return new ArrayList<>();
		}
		List<List<Integer>> res = new ArrayList<>();
		Arrays.sort(nums);
		for (int i = 0; i < len - 3; i++) {
			if (i > 0 && nums[i] == nums[i - 1]) continue;
			for (int j = i + 1; j < len - 2; j++) {
				if (j > i + 1 && nums[j] == nums[j - 1]) continue;
				int low = j + 1, high = len - 1;
				while (low < high) {
					int sum = nums[i] + nums[j] + nums[low] + nums[high];
					if (sum == target) {
						res.add(Arrays.asList(nums[i], nums[j], nums[low], nums[high]));
						while (low < high && nums[low] == nums[low + 1]) low++;
						while (low < high && nums[high] == nums[high - 1]) high--;
						low++;
						high--;
					} else if (sum < target) {
						while (low < high && nums[low] == nums[low + 1]) low++;
						low++;
					} else {
						while (low < high && nums[high] == nums[high - 1]) high--;
						high--;
					}
				}
			}
		}
		return res;
	}

	//优化
	public List<List<Integer>> fourSum1(int[] nums, int target) {
		List<List<Integer>> list = new ArrayList();
		Arrays.sort(nums);
		for (int i = 0; i < nums.length - 3; i++) {
			if (nums[i] + nums[i + 1] + nums[i + 2] + nums[i + 3] > target) {
				break;
			}
			if (nums[i] + nums[nums.length - 3] + nums[nums.length - 2] + nums[nums.length - 1] < target) {
				continue;
			}
			if (i > 0 && nums[i] == nums[i - 1]) continue;
			for (int j = i + 1; j < nums.length - 2; j++) {
				if (nums[i] + nums[j] + nums[j + 1] + nums[j + 2] > target) {
					break;
				}
				if (nums[i] + nums[j] + nums[nums.length - 2] + nums[nums.length - 1] < target) {
					continue;
				}
				if (j > i + 1 && nums[j] == nums[j - 1]) continue;
				int left = j + 1;
				int right = nums.length - 1;
				while (left < right) {
					int var = nums[i] + nums[j] + nums[left] + nums[right];
					if (var == target) {
						list.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));
						while (left < right && nums[left] == nums[left + 1]) left++;
						while (left < right && nums[right] == nums[right - 1]) right--;
						left++;
						right--;
					} else if (var < target) {
						while (left < right && nums[left] == nums[left + 1]) left++;
						left++;
					} else {
						while (left < right && nums[right] == nums[right - 1]) right--;
						right--;
					}
				}
			}
		}
		return list;
	}

	//最佳解答
	public List<List<Integer>> fourSum2(int[] nums, int target) {
		List<List<Integer>> list = new ArrayList<>();
		if (nums.length < 4) {
			return list;
		}
		Arrays.sort(nums);
		for (int i = 0; i < nums.length - 3; i++) {
			if (nums[i] + nums[i + 1] + nums[i + 2] + nums[i + 3] > target)
				break; // first candidate too large, search finished
			if (nums[i] + nums[nums.length - 1] + nums[nums.length - 2] + nums[nums.length - 3] < target)
				continue; // first candidate too small
			if (i > 0 && nums[i] == nums[i - 1])
				continue; // prevents duplicate result in ans list
			for (int j = i + 1; j < nums.length - 2; j++) {
				if (nums[i] + nums[j] + nums[j + 1] + nums[j + 2] > target)
					break; // second candidate too large
				if (nums[i] + nums[j] + nums[nums.length - 1] + nums[nums.length - 2] < target)
					continue; // second candidate too small
				if (j > i + 1 && nums[j] == nums[j - 1])
					continue; // prevents duplicate results in ans list
				int l = j + 1;
				int h = nums.length - 1;
				while (l < h) {
					int sum = nums[i] + nums[j] + nums[l] + nums[h];
					if (sum == target) {
						list.add(Arrays.asList(nums[i], nums[j], nums[l], nums[h]));
						while (l < h && nums[l] == nums[l + 1]) l++;
						while (l < h && nums[h] == nums[h - 1]) h--;
						l++;
						h--;
					} else if (sum < target) {
						while (l < h && nums[l] == nums[l + 1]) l++;
						l++;
					} else {
						while (l < h && nums[h] == nums[h - 1]) h--;
						h--;
					}
				}
			}
		}
		return list;
	}
}
