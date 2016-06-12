package com.snowstore.terra.client;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.snowstore.terra.client.load.FileLoad;
import com.snowstore.terra.client.load.HttpLoad;
import com.snowstore.terra.client.save.FileSave;

public class TerraPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	/**
	 * @see 本地新增 terra_local.properties文件 内容为acl=xxx xxx为服务端环境代码
	 * @see applicationContext 文件 配置 bean
	 *      class="com.snowstore.terra.client.TerraPropertyPlaceholderConfigurer"
	 *      即可
	 */
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {

		HttpLoad httpLoad = new HttpLoad();
		props = httpLoad.load();
		if (null == props || props.isEmpty()) {
			FileLoad fileLoad = new FileLoad();
			props = fileLoad.load();
		} else {
			FileSave save = new FileSave();
			save.save(props);
		}
		System.getProperties().putAll(props);

		super.processProperties(beanFactoryToProcess, props);
	}

}
