<<<<<<< Updated upstream
package com.bootloaderjs;

import java.util.List;

public class ListManager<T> {

	List<T> list = null;

	Integer index = 0;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public ListManager(List<T> list) {
		this.list = list;
	}

	public T pickRandom() {
		if (list != null) {
			return list.get(Random.getInt(0, list.size() - 1));
		}
		return null;
	}

	public T pickNext(int offset) {
		if (list != null) {
			int size = list.size();
			int next = (offset + 1) % size;
			return list.get(next);
		}
		return null;
	}

}
=======
package com.bootloaderjs;

import java.util.List;

public class ListManager<T> {

	List<T> list = null;

	Integer index = 0;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public ListManager(List<T> list) {
		this.list = list;
	}

	public T pickRandom() {
		if (list != null) {
			return list.get(Random.getInt(0, list.size() - 1));
		}
		return null;
	}

	public T pickNext(int offset) {
		if (list != null) {
			int size = list.size();
			int next = (offset + 1) / size;
			return list.get(next);
		}
		return null;
	}

}
>>>>>>> Stashed changes
