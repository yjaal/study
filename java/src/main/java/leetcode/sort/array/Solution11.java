package leetcode.sort.array;

/**
 * 11. 盛最多水的容器
 *
 * 给定 n 个非负整数 a1，a2，...，an，每个数代表坐标中的一个点 (i, ai) 。
 * 在坐标内画 n 条垂直线，垂直线 i 的两个端点分别为 (i, ai) 和 (i, 0)。
 * 找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
 *
 * 说明：你不能倾斜容器，且 n 的值至少为 2。
 *
 * 示例:
 * 输入: [1,8,6,2,5,4,8,3,7]
 * 输出: 49
 */
public class Solution11 {

	public static void main(String[] args) {
		Solution11 s = new Solution11();
		int[] arr = new int[]{1,8,6,2,5,4,8,3,7};
		int res = s.maxArea(arr);
		System.out.println(res);
	}

	public int maxArea(int[] height) {
		if (height.length <= 1) {
			return 0;
		}
		int i = 0, j = height.length - 1, res = 0;
		while (i < j) {
			int h = Math.min(height[i], height[j]);
			res = Math.max(res, h * (j - i));
			if (height[i] > height[j]) {
				j--;
			} else {
				i++;
			}
		}
		return res;
	}
}
