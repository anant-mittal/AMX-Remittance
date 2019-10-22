package com.axx.jax.table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;

public class PivotBucket {
	public List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
	public Map<String, Object> result = new HashMap<String, Object>();
	public Map<String, PivotBucket> pivotcols;

	public PivotBucket() {
		this.pivotcols = new HashMap<String, PivotBucket>();
	}

	public PivotBucket getCol(String colId) {
		if (!this.pivotcols.containsKey(colId)) {
			this.pivotcols.put(colId, new PivotBucket());
		}
		return this.pivotcols.get(colId);
	}

	public Object any(String rowId, Object defaultValue) {
		for (Map<String, Object> map : rows) {
			Object x = map.getOrDefault(rowId, defaultValue);
			if (ArgUtil.is(x)) {
				return x;
			}
		}
		return defaultValue;
	}

	public Object sum(String rowId) {
		BigDecimal sum = new BigDecimal(0);
		for (Map<String, Object> map : rows) {
			BigDecimal x = ArgUtil.parseAsBigDecimal(map.get(rowId), BigDecimal.ZERO);
			sum = sum.add(x);
		}
		return sum;
	}

	public Object avg(String rowId) {
		BigDecimal sum = new BigDecimal(0);
		int count = 0;
		for (Map<String, Object> map : rows) {
			BigDecimal x = ArgUtil.parseAsBigDecimal(map.get(rowId), BigDecimal.ZERO);
			sum = sum.add(x);
			count++;
		}
		if (count > 0) {
			return sum.divide(new BigDecimal(count));
		} else {
			return 0;
		}
	}

	public Object count() {
		return rows.size();
	}

	public Object ucount(String rowId) {
		Map<String, Object> uniqueMap = new HashMap<String, Object>();
		for (Map<String, Object> map : rows) {
			String x = ArgUtil.parseAsString(map.get(rowId));
			if (ArgUtil.is(x)) {
				uniqueMap.put(x, x);
			}
		}
		return uniqueMap.keySet().size();
	}

	public void add(Map<String, Object> e) {
		rows.add(e);
	}

}