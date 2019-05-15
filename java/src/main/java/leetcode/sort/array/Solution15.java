package leetcode.sort.array;

import java.util.*;

/**
 * 15. 三数之和
 * <p>
 * 给定一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，使得 a + b + c = 0 ？找出所有满足条件且不重复的三元组。
 * 注意：答案中不可以包含重复的三元组。
 * 例如, 给定数组 nums = [-1, 0, 1, 2, -1, -4]，
 * 满足要求的三元组集合为：
 * [
 * [-1, 0, 1],
 * [-1, -1, 2]
 * ]
 */
public class Solution15 {
	public static void main(String[] args) {
		Solution15 s = new Solution15();
		int[] nums = new int[]{-1, 0, 1, 2, -1, -4};
		List<List<Integer>> listList = s.threeSum1(nums);
		for (List<Integer> list : listList) {
			list.forEach(System.out::print);
			System.out.print(" ");
		}
	}

	//这里是2sum的解法，我们可以以此为基础来解答这道题
	public int[] twoSum(int[] nums, int target) {
		//最好算一下初始容量，提高效率
		int initialCapacity = (int) (nums.length / 0.75F + 1.0F);
		Map<Integer, Integer> map = new HashMap<>(initialCapacity);
		for (int i = 0; i < nums.length; i++) {
			//如果不存在
			if (!map.containsKey(target - nums[i])) {
				map.put(nums[i], i);
			} else {
				return new int[]{map.get(target - nums[i]), i};
			}
		}
		return null;
	}

	//这里在解答的过程中可能会出现重复情况
	public List<List<Integer>> threeSum(int[] nums) {
		List<List<Integer>> listList = new ArrayList<>();
		for (int i = 0; i < nums.length; i++) {
			//最好算一下初始容量，提高效率
			int initialCapacity = (int) (nums.length / 0.75F + 1.0F);
			Map<Integer, Integer> map = new HashMap<>(initialCapacity);
			for (int j = i + 1; j < nums.length; j++) {
				int key = -nums[i] - nums[j];
				if (!map.containsKey(key)) {
					map.put(nums[j], j);
				} else {
					listList.add(Arrays.asList(nums[i], key, nums[j]));
				}
			}
		}
		return listList;
	}

	//鉴于上面的问题，下面进行优化
	public List<List<Integer>> threeSum1(int[] nums) {
		List<List<Integer>> res = new ArrayList<>();
		//这里由于返回的是值，而不是像2sum返回的是下标，所以可以排序
		Arrays.sort(nums);
		for (int i = 0; i < nums.length - 2; i++) {
			if (i > 0 && nums[i] == nums[i - 1]) {
				continue;
			}
			//采用左右聚龙
			int low = i + 1, high = nums.length - 1, target = -nums[i];
			while (low < high) {
				if (nums[low] + nums[high] == target) {
					res.add(Arrays.asList(nums[i], nums[low], nums[high]));
					//下面两步是为了去重
					while (low < high && nums[low] == nums[low + 1]) low++;
					while (low < high && nums[high] == nums[high - 1]) high--;
					low++;
					high--;
				} else if (nums[low] + nums[high] < target) {
					while (low < high && nums[low] == nums[low + 1]) low++;
					low++;
				} else {
					while (low < high && nums[high] == nums[high - 1]) high--;
					high--;
				}
			}
		}
		return res;
	}


}
