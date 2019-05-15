package leetcode.sort.array;

/**
 * 39. 组合总和
 * * 给定一个无重复元素的数组 candidates 和一个目标数 target ，找出 candidates 中所有可以使数字和为 target 的组合。
 * candidates 中的数字可以无限制重复被选取。
 * <p>
 * 说明：
 * 所有数字（包括 target）都是正整数。
 * 解集不能包含重复的组合。
 * <p>
 * 示例 1:
 * 输入: candidates = [2,3,6,7], target = 7,
 * 所求解集为:
 * [
 * [7],
 * [2,2,3]
 * ]
 * <p>
 * 示例 2:
 * 输入: candidates = [2,3,5], target = 8,
 * 所求解集为:
 * [
 * [2,2,2,2],
 * [2,3,3],
 * [3,5]
 * ]
 */

import java.util.ArrayList;
import java.util.List;

public class Solution39 {

	public static void main(String[] args) {
		Solution39 s = new Solution39();
		int[] nums = new int[]{2, 3, 5};
		List<List<Integer>> res = s.combinationSum(nums, 8);
		for (List<Integer> list : res) {
			list.forEach(System.out::print);
			System.out.println(",");
		}
	}

	public List<List<Integer>> combinationSum(int[] candidates, int target) {
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
			list.add(candidates[i]);
			func(res, list, candidates, target - candidates[i], i);
			list.remove(list.size() - 1);
		}
	}
}
