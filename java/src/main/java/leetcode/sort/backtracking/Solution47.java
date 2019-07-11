package leetcode.sort.backtracking;

import java.util.*;

/**
 * 47. 全排列 II
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * 给定一个可包含重复数字的序列，返回所有不重复的全排列。
 * <p>
 * 示例:
 * 输入: [1,1,2]
 * 输出:
 * [
 * [1,1,2],
 * [1,2,1],
 * [2,1,1]
 * ]
 */
public class Solution47 {
	public static void main(String[] args) {
		Solution47 s = new Solution47();
		List<List<Integer>> listList = s.permuteUnique(new int[]{1, 1, 2});
		System.out.println("ok");
	}

	public List<List<Integer>> permuteUnique(int[] nums) {
		List<List<Integer>> results = new ArrayList<>();
		boolean[] flags = new boolean[nums.length];
		recursion(results, new ArrayList<>(), nums, flags);
		return results;
	}

	/**
	 * 这种方式不行
	 */
	private void recursion(List<List<Integer>> results, List<Integer> tempResult, int[] nums, boolean[] flags) {
		//判断：判断tempResult中的数据的长度是否等于nums的长度，如果相等，则相当于这是一个求解答案,添加到答案中
		if (tempResult.size() == nums.length) {
			results.add(new ArrayList<>(tempResult));
		} else {//如果不等于nums的长度，说明还没有添加完成
			for (int i = 0; i < nums.length; i++) {
				if (!tempResult.contains(nums[i]) || !flags[i]) {
					//添加数据
					tempResult.add(nums[i]);
					flags[i] = true;
					//递归添加数据
					recursion(results, tempResult, nums, flags);
					//退一步重新一遍跳到下面循环添加新的东西
					tempResult.remove(tempResult.size() - 1);
				}
			}
		}
	}

	/**
	 * 在46题基础上进行优化，使用set集合过滤重复
	 */
	public List<List<Integer>> permuteUnique1(int[] nums) {
		List<List<Integer>> res = new ArrayList<>();
		if (nums == null || nums.length == 0) {
			return res;
		}
		permute(res, nums, 0);
		return res;
	}

	private void permute(List<List<Integer>> res, int[] nums, int index) {
		if (index == nums.length) {
			List<Integer> temp = new ArrayList<>();
			for (int num : nums) {
				temp.add(num);
			}
			res.add(temp);
			return;
		}
		Set<Integer> appeared = new HashSet<>();
		for (int i = index; i < nums.length; ++i) {
			if (appeared.add(nums[i])) {
				swap(nums, index, i);
				permute(res, nums, index + 1);
				swap(nums, index, i);
			}
		}
	}

	private void swap(int[] nums, int i, int j) {
		int save = nums[i];
		nums[i] = nums[j];
		nums[j] = save;
	}

	/**
	 * 最优解
	 */
	public List<List<Integer>> permuteUnique2(int[] nums) {
		List<List<Integer>> res = new ArrayList<>();
		boolean[] visited = new boolean[nums.length];
		Arrays.sort(nums);
		backtrack(res, nums, new ArrayList<Integer>(), visited);
		return res;
	}

	private void backtrack(List<List<Integer>> res, int[] nums, ArrayList<Integer> tmp, boolean[] visited) {
		if (tmp.size() == nums.length) {
			res.add(new ArrayList<>(tmp));
			return;
		}
		int pre = Integer.MAX_VALUE;
		for (int i = 0; i < nums.length; i++) {
			if (visited[i]) {
				continue;
			}
			if (nums[i] == pre) {
				continue;
			}
			visited[i] = true;
			tmp.add(nums[i]);
			backtrack(res, nums, tmp, visited);
			visited[i] = false;
			tmp.remove(tmp.size() - 1);
			pre = nums[i];
		}
	}
}
