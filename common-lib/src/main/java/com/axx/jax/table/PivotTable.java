package com.axx.jax.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.amx.utils.CollectionUtil;
import com.amx.utils.Constants;
import com.amx.utils.StringUtils.StringMatcher;;

public class PivotTable {

	public static final Pattern FUN_AS_ALIAS_DEFAULT = Pattern
			.compile("^(sum|any|count|ucount) (.+) (AS|as|As|aS) (.+) DEFAULT (.+)$");
	public static final Pattern FUN_AS_ALIAS = Pattern.compile("^(sum|any|count|ucount) (.+) (AS|as|As|aS) (.+)$");
	public static final Pattern ROW_AS_ALIAS_DEFAULT = Pattern.compile("^(.+) (AS|as|As|aS) (.+) DEFAULT (.+)$");
	public static final Pattern ROW_AS_ALIAS = Pattern.compile("^(.+) (AS|as|As|aS) (.+)$");
	public static final Pattern FUN_COLS = Pattern.compile("^(sum|any|count|ucount) (.+)$");
	public static final Pattern COMPUTED = Pattern.compile("^(.+)=(.+)$");
	public static final Pattern NONCOMPUTED = Pattern.compile("^(.+)=(.+)$");

	List<String> rows = CollectionUtil.getList("*");
	List<String> rows_alias = CollectionUtil.getList("*");
	List<String> rows_default = CollectionUtil.getList("");
	List<String> cols = CollectionUtil.getList("*");
	List<String> vals = CollectionUtil.getList("*");
	List<String> aggs = CollectionUtil.getList("*");
	List<String> alias = CollectionUtil.getList("*");
	List<String> computedCols = CollectionUtil.getList();
	List<String> computedVals = CollectionUtil.getList();
	List<String> noncomputedCols = CollectionUtil.getList();
	List<String> noncomputedVals = CollectionUtil.getList();

	public Map<String, PivotBucket> pivotrows;

	public PivotTable() {
		this.pivotrows = new HashMap<String, PivotBucket>();
	}

	public PivotTable(List<String> rows, List<String> cols, List<String> vals, List<String> aggs, List<String> alias,
			List<String> computedCols, List<String> noncomputedVals) {
		this();
		this.rows = rows;
		this.cols = cols;
		this.vals = vals;
		this.aggs = aggs != null ? aggs : this.aggs;
		this.alias = alias != null ? alias : this.alias;
		this.computedCols = computedCols != null ? computedCols : this.aggs;
		this.noncomputedVals = noncomputedVals != null ? noncomputedVals : this.aggs;

		int rowCount = rows.size();
		int valCount = vals.size();
		int colCount = cols.size();
		int computedCount = computedCols.size();
		int noncomputedCount = noncomputedCols.size();

		for (int r = 0; r < rowCount; r++) {
			StringMatcher funkey = new StringMatcher(rows.get(r));
			if (funkey.isMatch(ROW_AS_ALIAS_DEFAULT)) {
				this.rows.set(r, funkey.group(1));
				CollectionUtil.set(this.rows_alias, r, funkey.group(3));
				CollectionUtil.set(this.rows_default, r, funkey.group(4));
			} else if (funkey.isMatch(ROW_AS_ALIAS)) {
				this.rows.set(r, funkey.group(1));
				CollectionUtil.set(this.rows_alias, r, funkey.group(3));
				CollectionUtil.set(this.rows_default, r, null);
			} else {
				CollectionUtil.set(this.rows_alias, r, funkey.toString());
				CollectionUtil.set(this.rows_default, r, null);
			}
		}

		for (int v = 0; v < valCount; v++) {
			StringMatcher funkey = new StringMatcher(vals.get(v));
			if (funkey.isMatch(FUN_AS_ALIAS_DEFAULT)) {
				CollectionUtil.set(this.aggs, v, funkey.group(1));
				this.vals.set(v, funkey.group(2));
				CollectionUtil.set(this.alias, v, funkey.group(4));
			} else if (funkey.isMatch(FUN_AS_ALIAS)) {
				CollectionUtil.set(this.aggs, v, funkey.group(1));
				this.vals.set(v, funkey.group(2));
				CollectionUtil.set(this.alias, v, funkey.group(4));
			} else if (funkey.isMatch(ROW_AS_ALIAS)) {
				CollectionUtil.set(this.aggs, v, "count");
				this.vals.set(v, funkey.group(1));
				CollectionUtil.set(this.alias, v, funkey.group(3));
			} else if (funkey.isMatch(FUN_COLS)) {
				CollectionUtil.set(this.aggs, v, funkey.group(1));
				this.vals.set(v, funkey.group(2));
				CollectionUtil.set(this.alias, v, funkey.group(2));
			} else {
				CollectionUtil.set(this.aggs, v, "count");
				this.alias.set(v, funkey.toString());
			}
		}

		for (int cp = 0; cp < computedCount; cp++) {
			StringMatcher funkey = new StringMatcher(computedCols.get(cp));
			if (funkey.isMatch(COMPUTED)) {
				CollectionUtil.set(this.computedCols, cp, funkey.group(1));
				CollectionUtil.set(this.computedVals, cp, funkey.group(2));
			}
		}

		for (int ncp = 0; ncp < noncomputedCount; ncp++) {
			StringMatcher funkey = new StringMatcher(noncomputedCols.get(ncp));
			if (funkey.isMatch(NONCOMPUTED)) {
				CollectionUtil.set(this.noncomputedCols, ncp, funkey.group(1));
				CollectionUtil.set(this.noncomputedVals, ncp, funkey.group(2));
			}
		}

	}

	public PivotBucket getRow(String rowId) {
		if (!this.pivotrows.containsKey(rowId)) {
			this.pivotrows.put(rowId, new PivotBucket());
		}
		return this.pivotrows.get(rowId);
	}

	public void add(Map<String, Object> item) {
		StringBuilder rowId = new StringBuilder();
		for (String rowKey : rows) {
			rowId.append(item.getOrDefault(rowKey, "#"));
		}

		StringBuilder colId = new StringBuilder();
		for (String colKey : cols) {
			colId.append(item.getOrDefault(colKey, "#"));
		}

		PivotBucket bucket = getRow(rowId.toString());
		bucket.getCol(colId.toString()).add(item);
	}

	public void calculate() {
		for (Entry<String, PivotBucket> rowEntrySet : pivotrows.entrySet()) {
			PivotBucket row = rowEntrySet.getValue();
			int rowCount = rows.size();
			int valCount = vals.size();
			int colCount = cols.size();

			int noncomputedColsCount = noncomputedCols.size();

			if (colCount > 0) {
				for (Entry<String, PivotBucket> colEntrySet : row.pivotcols.entrySet()) {
					PivotBucket col = colEntrySet.getValue();

					for (int r = 0; r < rowCount; r++) {
						String funkey = rows.get(r);
						String funkeyDefault = rows_default.get(r);
						row.result.put(rows_alias.get(r), col.any(funkey, funkeyDefault));
					}

					StringBuilder rowKey = new StringBuilder();
					for (int c = 0; c < colCount; c++) {
						rowKey.append(col.any(cols.get(c), Constants.BLANK));
					}

					for (int i = 0; i < valCount; i++) {
						String fun = aggs.get(i);
						String funkey = vals.get(i);
						String funkeyAlias = alias.get(i);
						this.calculate(row, funkeyAlias + "_" + rowKey.toString(), fun, funkey, col, Constants.BLANK);
					}

					row.exp(computedCols, computedVals);

					for (int i = 0; i < noncomputedColsCount; i++) {
						String fun = noncomputedVals.get(i);
						String funkey = noncomputedCols.get(i);
						row.result.put(funkey, fun);
					}

				}
			} else {
				for (int r = 0; r < rowCount; r++) {
					String funkey = rows.get(r);
					row.result.put(funkey, row.any(funkey, Constants.BLANK));
				}
				for (int i = 0; i < valCount; i++) {
					String fun = aggs.get(i);
					String funkey = vals.get(i);
					this.calculate(row, funkey, fun, funkey, row, Constants.BLANK);
				}
			}

		}
	}

	public List<Map<String, Object>> toBulk() {
		List<Map<String, Object>> bulk = new ArrayList<Map<String, Object>>();
		for (Entry<String, PivotBucket> e : this.pivotrows.entrySet()) {
			bulk.add(e.getValue().result);
		}
		return bulk;
	}

	private void calculate(PivotBucket row, String rowKey, String fun, String funkey, PivotBucket col,
			Object emptyValue) {
		switch (fun) {
		case "any":
			row.result.put(rowKey, col.any(funkey, emptyValue));
			break;
		case "sum":
			row.result.put(rowKey, col.sum(funkey));
			break;
		case "avg":
			row.result.put(rowKey, col.avg(funkey));
			break;
		case "ucount":
			row.result.put(rowKey, col.ucount(funkey));
			break;
		default:
			row.result.put(rowKey, col.count());
			break;
		}
	}

}