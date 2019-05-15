package leetcode.easy;

/**
 * 69. x 的平方根
 * <p>
 * 实现 int sqrt(int x) 函数。
 * <p>
 * 计算并返回 x 的平方根，其中 x 是非负整数。
 * <p>
 * 由于返回类型是整数，结果只保留整数的部分，小数部分将被舍去。
 * <p>
 * 示例 1:
 * <p>
 * 输入: 4
 * 输出: 2
 * 示例 2:
 * <p>
 * 输入: 8
 * 输出: 2
 * 说明: 8 的平方根是 2.82842...,
 * 由于返回类型是整数，小数部分将被舍去。
 */
public class Solution69 {
	public static void main(String[] args) {
		Solution69 s = new Solution69();
		int res = s.mySqrt1(4000);
		System.out.println(res);
	}

	//这种方式时间复杂度太高，不应该使用除2或乘2来寻找左右范围
	public int mySqrt(int x) {
		if (x == 0) {
			return 0;
		}
		if (x <= 3) {
			return 1;
		}
		int high = x / 2;
		while (high * high > x) {
			high /= 2;
		}
		if (x == high * high) {
			return high;
		}
		high *= 2;
		int low = 2;
		while (low * low < x) {
			low *= 2;
		}
		if (x == low * low) {
			return low;
		}
		low /= 2;
		for (; low <= high; low++) {
			if (low * low > x) {
				break;
			}
		}
		return low - 1;
	}

	//采用二分法寻找左右范围
	public int mySqrt1(int x) {
		if (x <= 1) {
			return x;
		}
		int l = 0;
		int r = x;
		int mid = 0;
		while (l <= r) {
			mid = l + ((r - l) >> 1);//这里取mid方式较好
			if (mid <= x / mid) {
				if (mid + 1 > x / (mid + 1)) return mid;
				else l = mid + 1;
			} else {
				r = mid - 1;
			}
		}
		return -1;
	}

	//最佳解答
	public int mySqrt2(int x) {
		if (x <= 1) {
			return x;
		}
		int l = 1;
		int h = x;
		while (l < h) {
			int mid = l + (h - l) / 2;
			if (x / mid >= mid) {
				l = mid + 1;
			} else {
				h = mid;
			}
		}
		return h - 1;
	}
}
