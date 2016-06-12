package com.snowstore.terra.compare;

import java.lang.reflect.Method;
import java.text.Collator;
import java.util.Comparator;

public class TerraComparator implements Comparator {
	private Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);

	private String[] sortBy;

	private int order;

	public TerraComparator(String[] sortBy, int order) {
		this.sortBy = sortBy;
		this.order = order;
	}

	/*
	 * 定义排序规则 如果按照不止一个属性进行排序 这按照属性的顺序进行排序,类似sql order by 即只要比较出同位置的属性就停止
	 */
	public int compare(Object o1, Object o2) {
		for (int i = 0; i < sortBy.length; i++) {
			Object value1 = getFieldValueByName(sortBy[i], o1);
			Object value2 = getFieldValueByName(sortBy[i], o2);
			if (value1 instanceof Integer && value2 instanceof Integer) {
				int v1 = Integer.parseInt(value1.toString());
				int v2 = Integer.parseInt(value2.toString());
				if (v1 > v2) {
					return 1 * this.order;
				} else if (v1 < v2) {
					return -1 * this.order;
				}
			} else {
				int result = cmp.compare(value1, value2);
				if (result != 0) {
					return result * this.order;
				}
			}
		}
		return -1 * this.order;
	}

	/**
	 * 
	 * 通过反射，获取属性值
	 * 
	 * @param fieldName
	 * @param o
	 * @return
	 */
	private Object getFieldValueByName(String fieldName, Object o) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = o.getClass().getMethod(getter, new Class[] {});

			Object value = method.invoke(o, new Object[] {});
			return value;
		} catch (Exception e) {
			System.out.println("属性不存在");
			return null;
		}
	}

}
