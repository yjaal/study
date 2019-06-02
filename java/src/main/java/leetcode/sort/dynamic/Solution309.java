package leetcode.sort.dynamic;

/**
 * 309. 最佳买卖股票时机含冷冻期
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * 给定一个整数数组，其中第 i 个元素代表了第 i 天的股票价格 。​
 * 设计一个算法计算出最大利润。在满足以下约束条件下，你可以尽可能地完成更多的交易（多次买卖一支股票）:
 * 你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
 * 卖出股票后，你无法在第二天买入股票 (即冷冻期为 1 天)。
 * <p>
 * 示例:
 * 输入: [1,2,3,0,2]
 * 输出: 3
 * 解释: 对应的交易状态为: [买入, 卖出, 冷冻期, 买入, 卖出]
 */
public class Solution309 {
	public static void main(String[] args) {
		Solution309 s = new Solution309();
		System.out.println(s.maxProfit(new int[]{1, 2, 3, 0, 2}));
	}

	/**
	 * buy[i]表示第i天买入时的最大利润（最后一个操作是买）
	 * sell[i]表示第i天卖出时的最大利润（最后一个操作是卖）
	 */
	public int maxProfit(int[] prices) {
		if (null == prices || prices.length <= 1) {
			return 0;
		}
		int n = prices.length;
		int[] buy = new int[n];
		int[] sell = new int[n];
		buy[0] = -prices[0];
		sell[0] = 0;
		//因为冷冻期，所以前一天只能为买，所以比较的是buy[0]=-prices[0],而大前天无买卖，所以利润为0，则得到0-prices[1]
		buy[1] = Math.max(buy[0], 0 - prices[1]);
		sell[1] = Math.max(sell[0], buy[0] + prices[1]);
		for (int i = 2; i < n; ++i) {
			//因为冷冻期，所以前一天只能为买，所以比较的是buy[i - 1]
			buy[i] = Math.max(buy[i - 1], sell[i - 2] - prices[i]);
			//比较的是前一天卖出的最大利润和前一天买入的最大利润+今天卖出的价格
			sell[i] = Math.max(sell[i - 1], buy[i - 1] + prices[i]);
		}
		return sell[n - 1];
	}

	/**
	 * 最优解
	 */
	public int maxProfit1(int[] prices) {
		int sell = 0, prevSell = 0, buy = Integer.MIN_VALUE, prevBuy;
		for (int price : prices) {
			prevBuy = buy;
			buy = Math.max(prevSell - price, prevBuy);
			prevSell = sell;
			sell = Math.max(prevBuy + price, prevSell);
		}
		return sell;
	}
}
