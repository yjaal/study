package leetcode.sort.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * 给出一个区间的集合，请合并所有重叠的区间。
 * <p>
 * 示例 1:
 * 输入: [[1,3],[2,6],[8,10],[15,18]]
 * 输出: [[1,6],[8,10],[15,18]]
 * 解释: 区间 [1,3] 和 [2,6] 重叠, 将它们合并为 [1,6].
 * <p>
 * 示例 2:
 * 输入: [[1,4],[4,5]]
 * 输出: [[1,5]]
 * 解释: 区间 [1,4] 和 [4,5] 可被视为重叠区间。
 */
public class Solution56 {

	public static void main(String[] args) {
		Solution56 s = new Solution56();
		int[][] nums = new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}};
		int[][] res = s.merge(nums);
		for (int[] num : res) {
			System.out.println(Arrays.toString(num));
		}
	}

	/**
	 * 时间复杂度太高
	 */
	public int[][] merge(int[][] intervals) {
		if (intervals == null || intervals.length < 2) {
			return intervals;
		}
		//按二维数组第一位排序
		Arrays.sort(intervals, Comparator.comparingInt(o -> o[0]));
		List<int[]> res = new ArrayList<>();
		res.add(intervals[0]);
		for (int i = 1; i < intervals.length; i++) {
			int last = res.size() - 1;
			int[] tmps = res.get(last);
			if (tmps[1] >= intervals[i][0]) {
				if (tmps[1] < intervals[i][1]) {
					res.remove(last);
					res.add(new int[]{tmps[0], intervals[i][1]});
				}
			} else {
				res.add(intervals[i]);
			}
		}
		//注意转化方式
		return res.toArray(new int[][]{new int[res.size()]});
	}

	/**
	 * 最优解
	 */
	public int[][] merge1(int[][] intervals) {
		if (intervals == null || intervals.length < 2) {
			return intervals;
		}
		int rows = intervals.length;
		int count = rows;
		for (int i = 0; i < rows - 1; i++) {
			for (int j = i + 1; j < rows; j++) {
				if (canMerge(intervals, i, j)) {
					count--;
					break;
				}
			}
		}
		int[][] res = new int[count][2];
		int index = 0;
		for (int[] row : intervals) {
			if (row != null) {
				res[index] = row;
				index++;
			}
		}
		return res;
	}

	private boolean canMerge(int[][] intervals, int first, int second) {
		//判断能否合并
		boolean flag = (intervals[first][0] < intervals[second][0] && intervals[first][1] < intervals[second][0])
				|| (intervals[second][0] < intervals[first][0] && intervals[second][1] < intervals[first][0]);
		if (!flag) {
			intervals[second] = new int[]{Math.min(intervals[first][0], intervals[second][0]),
					Math.max(intervals[first][1], intervals[second][1])};
			intervals[first] = null;
			return true;
		}
		return false;
	}

}
