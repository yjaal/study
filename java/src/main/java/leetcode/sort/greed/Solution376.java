package leetcode.sort.greed;

/**
 * 376. 摆动序列
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * 如果连续数字之间的差严格地在正数和负数之间交替，则数字序列称为摆动序列。第一个差（如果存在的话）可能是正数或负数。
 * 少于两个元素的序列也是摆动序列。
 * 例如， [1,7,4,9,2,5] 是一个摆动序列，因为差值 (6,-3,5,-7,3) 是正负交替出现的。
 * 相反, [1,4,7,2,5] 和 [1,7,4,5,5] 不是摆动序列，第一个序列是因为它的前两个差值都是正数，第二个序列是因为它的最后一个差值为零。
 * 给定一个整数序列，返回作为摆动序列的最长子序列的长度。 通过从原始序列中删除一些（也可以不删除）元素来获得子序列，
 * 剩下的元素保持其原始顺序。
 * <p>
 * 示例 1:
 * 输入: [1,7,4,9,2,5]
 * 输出: 6
 * 解释: 整个序列均为摆动序列。
 * <p>
 * 示例 2:
 * 输入: [1,17,5,10,13,15,10,5,16,8]
 * 输出: 7
 * 解释: 这个序列包含几个长度为 7 摆动序列，其中一个可为[1,17,10,13,10,16,8]。
 * <p>
 * 示例 3:
 * 输入: [1,2,3,4,5,6,7,8,9]
 * 输出: 2
 * <p>
 * 进阶:你能否用 O(n) 时间复杂度完成此题?
 */
public class Solution376 {

	/**
	 * 最优解
	 * 首先观察数据以及题意要求，理解本题的本质，后一个数比前面小-大-小-大这样
	 * 然后举例子，比如实例2中，前6位，1 17 5 10 13 15，可以构成的摇摆数列有三个，那么选哪个显而易见
	 * 所以贪心策略就出来了：在递增子序列部分，选最大的数，贪心尽量使递增子序列下一个数成为摇摆序列。递减相反
	 * 即贪心的只保留递增或者递减子序列中的首尾元素
	 * 证明贪心比较麻烦，可以简单看下有无反例，其次可以手撸对数器证明
	 * 设计代码可以采用状态机思考 begin、up、down，如果up与down切换，那么count++（num[i]与num[i—1]比）
	 */
	public int wiggleMaxLength(int[] nums) {
		if (nums.length <= 1) {
			return nums.length;
		}
		int len = nums.length;
		int up = 1, down = 1;
		for (int i = 1; i < len; i++) {
			if (nums[i - 1] < nums[i]) {
				down = up + 1;
			}
			if (nums[i - 1] > nums[i]) {
				up = down + 1;
			}
		}
		return Math.max(up, down);
	}

	public int wiggleMaxLength1(int[] nums) {
		if (nums.length <= 1) {
			return nums.length;
		}
		int len = nums.length;
		int counter = 1;
		int flag = 0;
		for (int i = 1; i < len; i++) {
			//flag=1表示 后>前
			if ((nums[i - 1] > nums[i]) && (flag == 0 || flag == 1)) {
				counter++;
				flag = -1;
			} else if ((nums[i - 1] < nums[i]) && (flag == 0 || flag == -1)) {
				counter++;
				flag = 1;
			}
		}
		return counter;
	}
}
