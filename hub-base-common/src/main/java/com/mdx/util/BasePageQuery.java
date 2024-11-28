package com.mdx.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Positive;

import static com.baomidou.mybatisplus.core.metadata.OrderItem.asc;
import static com.baomidou.mybatisplus.core.metadata.OrderItem.desc;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 分页查询条件
 */
@Data
@ApiModel(description = "查询条件")
public abstract class BasePageQuery {

	@ApiModelProperty("当前页")
	@Positive
	private int current = 1;

	@ApiModelProperty("每页的数量")
	@Positive
	private int size = 10;

	@ApiModelProperty("正序")
	private String asc;

	@ApiModelProperty("倒序")
	private String desc;

	/**
	 * 生成QueryWrapper
	 * @param <T> 类型
	 * @return QueryWrapper
	 */
	public abstract <T> QueryWrapper<T> wrapperQuery();

	/**
	 * 排序
	 * @param defaultItems 排序规则
	 * @param <T>
	 * @return
	 */
	public <T> Page<T> getPage(OrderItem... defaultItems) {
		Page<T> page = new Page<>(current, size);
		if (isNotBlank(asc)) {
			page.addOrder(asc(humpToUnderline(asc)));
		} else if (isNotBlank(desc)) {
			page.addOrder(desc(humpToUnderline(desc)));
		} else {
			page.addOrder(defaultItems);
		}
		return page;
	}

	/**
	 * 下划线转驼峰
	 *
	 * @param para 字符串
	 * @return String
	 */
	public static String underlineToHump(String para) {
		StringBuilder result = new StringBuilder();
		String[] a = para.split("_");
		for (String s : a) {
			if (result.length() == 0) {
				result.append(s.toLowerCase());
			} else {
				result.append(s.substring(0, 1).toUpperCase());
				result.append(s.substring(1).toLowerCase());
			}
		}
		return result.toString();
	}

	/**
	 * 驼峰转下划线
	 *
	 * @param para 字符串
	 * @return String
	 */
	public static String humpToUnderline(String para) {
		para = lowerFirst(para);
		StringBuilder sb = new StringBuilder(para);
		int temp = 0;
		for (int i = 0; i < para.length(); i++) {
			if (Character.isUpperCase(para.charAt(i))) {
				sb.insert(i + temp, "_");
				temp += 1;
			}
		}
		return sb.toString().toLowerCase();
	}

	/**
	 * 首字母变小写
	 *
	 * @param str 字符串
	 * @return {String}
	 */
	public static String lowerFirst(String str) {
		char firstChar = str.charAt(0);
		if (firstChar >= 'A' && firstChar <= 'Z') {
			char[] arr = str.toCharArray();
			arr[0] += ('a' - 'A');
			return new String(arr);
		}
		return str;
	}

}
