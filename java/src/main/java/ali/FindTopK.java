package ali;

/**
 * 无序数组中查找最大的k个数
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 */
public class FindTopK {

	/**
	 * 找到无序数组中最小的k个数 时间复杂度O(Nlogk)
	 * 过程：
	 * 1.一直维护一个有k个数的大根堆，这个堆代表目前选出来的k个最小的数
	 * 在堆里的k个元素中堆顶的元素是最小的k个数中最大的那个。
	 * 2.接下来，遍历整个数组，遍历过程中看当前数是否比堆顶元素小：
	 * 如果是，就把堆顶元素替换成当前的数，然后从堆顶的位置调整整个堆，让替
	 * 换操作后堆的最大元素继续处在堆顶的位置；
	 * 如果不是，则不进行任何操作，继续遍历下一个数；
	 * 3.在遍历完成后，堆中的k个数就是所有数组中最小的k个数
	 */
	public int[] findTopK(int[] arr, int k) {
		if (null == arr || arr.length <= 0 || k <= 0 || k > arr.length) {
			return arr;
		}
		int[] kHeap = new int[k];
		for (int i = 0; i < k; i++) {
			createHeap(kHeap, arr[i], i);
		}
		for (int i = k; i != arr.length; i++) {
			if (arr[i] > kHeap[0]) {
				kHeap[0] = arr[i];
				insert2Heap(kHeap, 0, k);
			}
		}
		return kHeap;
	}


	private void insert2Heap(int[] kHeap, int curIdx, int k) {
		int left = curIdx * 2 + 1;
		int right = curIdx * 2 + 2;
		int parent = curIdx;
		while (left < k) {
			if (kHeap[curIdx] > kHeap[left]) {
				parent = left;
			}
			if (right < k && kHeap[curIdx] > kHeap[right]) {
				parent = right;
			}
			if (parent != curIdx) {
				swap(kHeap, parent, curIdx);
			} else {
				break;
			}
			curIdx = parent;
			left = curIdx * 2 + 1;
			right = curIdx * 2 + 2;
		}
	}

	/**
	 * 创建堆（小根堆）。
	 * 注意：在查找最大的k个数时要创建小根堆，反之则要创建大根堆
	 */
	private void createHeap(int[] kHeap, int curVal, int idx) {
		kHeap[idx] = curVal;
		while (idx != 0) {
			int parent = (idx - 1) / 2;
			if (kHeap[parent] > kHeap[idx]) {
				swap(kHeap, parent, idx);
				idx = parent;
			} else {
				break;
			}
		}
	}

	private void swap(int[] arr, int index1, int index2) {
		int tmp = arr[index1];
		arr[index1] = arr[index2];
		arr[index2] = tmp;
	}
}
