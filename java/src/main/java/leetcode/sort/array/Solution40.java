package leetcode.sort.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 40. 组合总和 II
 * <p>
 * 给定一个数组 candidates 和一个目标数 target ，找出 candidates 中所有可以使数字和为 target 的组合。
 * candidates 中的每个数字在每个组合中只能使用一次。
 * <p>
 * 说明：
 * 所有数字（包括目标数）都是正整数。
 * 解集不能包含重复的组合。
 * <p>
 * 示例 1:
 * 输入: candidates = [10,1,2,7,6,1,5], target = 8,
 * 所求解集为:
 * [
 * [1, 7],
 * [1, 2, 5],
 * [2, 6],
 * [1, 1, 6]
 * ]
 * <p>
 * 示例 2:
 * 输入: candidates = [2,5,2,1,2], target = 5,
 * 所求解集为:
 * [
 * [1,2,2],
 * [5]
 * ]
 */
public class Solution40 {
	public static void main(String[] args) {
		Solution40 s = new Solution40();
		int[] nums = new int[]{2, 5, 2, 1, 2};
		List<List<Integer>> res = s.combinationSum2(nums, 5);
		for (List<Integer> list : res) {
			list.forEach(System.out::print);
			System.out.println(",");
		}
	}

	public List<List<Integer>> combinationSum2(int[] candidates, int target) {
		Arrays.sort(candidates);
		List<List<Integer>> res = new ArrayList<>();
		if (candidates == null || candidates.length == 0) {
			return res;
		}
		func(res, new ArrayList<>(), candidates, target, 0);
		return res;
	}

	private void func(List<List<Integer>> res, List<Integer> list, int[] candidates, int target, int start) {
		if (target < 0) {
			return;
		} else if (target == 0) {
			res.add(new ArrayList<>(list));
		}
		for (int i = start; i < candidates.length; i++) {
			if (i != start && candidates[i] == candidates[i - 1]) {
				continue;
			}
			list.add(candidates[i]);
			func(res, list, candidates, target - candidates[i], i + 1);
			list.remove(list.size() - 1);
		}
	}
}
