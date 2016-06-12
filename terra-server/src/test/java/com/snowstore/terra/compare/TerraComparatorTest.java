package com.snowstore.terra.compare;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TerraComparatorTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testCompare() {

		Bean b1 = new Bean();
		b1.setName("apple");
		b1.setPost("研发工程师");
		b1.setAge(32);

		Bean b2 = new Bean();
		b2.setName("banada");
		b2.setPost("部署工程师");
		b2.setAge(27);

		Bean b3 = new Bean();
		b3.setName("helen");
		b3.setPost("部署工程师");
		b3.setAge(28);

		Bean b4 = new Bean();
		b4.setName("xiao");
		b4.setPost("架构工程师");
		b4.setAge(32);

		List<Bean> list = new ArrayList<Bean>();
		list.add(b1);
		list.add(b2);
		list.add(b3);
		list.add(b4);

		String[] sortBy = new String[] { "name", "post" };
		int orderBy = 1;// 1:升序，-1：降序
		TerraComparator myCmp = new TerraComparator(sortBy, orderBy);
		Collections.sort(list, myCmp);
		System.out.println("--------------------按名字和职位升序----------------------------");
		for (Bean b : list) {
			System.out.println("name:" + b.getName() + ",post:" + b.getPost() + ",age:" + b.getAge());
		}

		sortBy = new String[] { "post", "age" };
		myCmp = new TerraComparator(sortBy, orderBy);
		Collections.sort(list, myCmp);
		System.out.println("--------------------按职位和年龄升序----------------------------");
		for (Bean b : list) {
			System.out.println("post:" + b.getPost() + ",age:" + b.getAge() + ",name:" + b.getName());
		}

		sortBy = new String[] { "age", "name" };
		myCmp = new TerraComparator(sortBy, orderBy);
		Collections.sort(list, myCmp);
		System.out.println("--------------------按年龄和名字升序----------------------------");
		for (Bean b : list) {
			System.out.println("age:" + b.getAge() + ",name:" + b.getName() + ",post:" + b.getPost());
		}

	}

	/**
	 * 
	 * 待排序对象
	 * 
	 */
	private static class Bean {
		private String name = null;

		private String post = null;

		private Integer age = null;

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}

		public String getPost() {
			return post;
		}

		public void setPost(String post) {
			this.post = post;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
