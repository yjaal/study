package leetcode.sort.backtracking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 46. 全排列
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * 给定一个没有重复数字的序列，返回其所有可能的全排列。
 * <p>
 * 示例:
 * 输入: [1,2,3]
 * 输出:
 * [
 * [1,2,3],
 * [1,3,2],
 * [2,1,3],
 * [2,3,1],
 * [3,1,2],
 * [3,2,1]
 * ]
 */
public class Solution46 {

	/**
	 * 本题可以采用回溯法+递归实现，下面以输入为[1,2,3,]为例，对后面的代码进行一个直观的分析。
	 * tempResult变化情况如下（tempResult为下面代码中的一个变量）：
	 * <p>
	 * []---[1]---[1,2]---[1,2,3](添加)---[1,2]---
	 * [1]---[1,3]---[1,3,2](添加)---[1,3]---[1]---
	 * []---[2]---[2,1]---[2,1,3](添加)---[2,1]---
	 * [2]---[2,3]---[2,3,1](添加)---[2,3]---[2]---
	 * []---[3]---[3,1]---[3,1,2](添加)---[3,1]---
	 * [3]---[3,2]---[3,2,1](添加)
	 */
	public List<List<Integer>> permute(int[] nums) {
		List<List<Integer>> results = new ArrayList<>();
		recursion(results, new ArrayList<>(), nums);
		return results;
	}

	private void recursion(List<List<Integer>> results, List<Integer> tempResult, int[] nums) {
		//判断：判断tempResult中的数据的长度是否等于nums的长度，如果相等，则相当于这是一个求解答案,添加到答案中
		if (tempResult.size() == nums.length) {
			results.add(new ArrayList<>(tempResult));
		} else {//如果不等于nums的长度，说明还没有添加完成
			for (int num : nums) {
				if (!tempResult.contains(num)) {
					//添加数据
					tempResult.add(num);
					//递归添加数据
					recursion(results, tempResult, nums);
					//退一步重新一遍跳到下面循环添加新的东西
					tempResult.remove(tempResult.size() - 1);
				}
			}
		}
	}

	/**
	 * 最优解
	 */
	public List<List<Integer>> permute1(int[] nums) {
		List<List<Integer>> ret = new ArrayList<>();
		ArrayList<Integer> numList = new ArrayList<>();
		for (int num : nums) {
			numList.add(num);
		}
		backt(ret, numList, 0);
		return ret;
	}

	private void backt(List<List<Integer>> ret, ArrayList<Integer> numList, int start) {
		//需要复制老的列表
		if (start == numList.size()) {
			ret.add(new ArrayList<>(numList));
			return;
		}
		for (int i = start; i < numList.size(); i++) {
			Collections.swap(numList, start, i);
			backt(ret, numList, start + 1);
			Collections.swap(numList, start, i);
		}
	}

}
