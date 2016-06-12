package com.snowstore.terra.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.snowstore.terra.service.EnvService;
import com.snowstore.terra.service.SystemService;
import com.snowstore.terra.vo.EnvDetail;
import com.snowstore.terra.vo.SystemTree;

@Controller("/")
public class IndexController {

	@Autowired
	private SystemService systemService;
	@Autowired
	private EnvService envService;

	@RequestMapping("/")
	public String index() {
		return "/index";
	}

	@RequestMapping("/login")
	public String login() {
		return "/login";
	}

	@RequestMapping("/upload")
	@ResponseBody
	public Properties upload(MultipartFile file) throws IOException {
		return PropertiesLoaderUtils.loadProperties(new ByteArrayResource(file.getBytes()));
	}

	@RequestMapping("/createSystem")
	@ResponseBody
	public Long createSystem(String properties, String systemName, String systemCode, String systemVersion,
			String systemEnv) throws IOException {
		return systemService.createSystem(properties, systemName, systemCode, systemVersion, systemEnv);
	}

	@RequestMapping("/systemTree")
	@ResponseBody
	public SystemTree[] systemTree() throws IOException {
		return systemService.getSystemAstree();
	}

	@RequestMapping("/envDetail")
	@ResponseBody
	public EnvDetail envDetail(Long envId) throws IOException {
		return envService.getEvnDetail(envId);
	}

	@RequestMapping("/renameVersionOrEnv")
	@ResponseBody
	public void renameVersionOrEnv(String nodeId, String text) throws IOException {
		systemService.renameVersionOrEnv(nodeId, text);
	}

	@RequestMapping("/copyVersionOrEnv")
	@ResponseBody
	public void copyVersionOrEnv(String nodeId) throws IOException {
		systemService.copyVersionOrEnv(nodeId);
	}

	@RequestMapping("/deleteVersionOrEnv")
	@ResponseBody
	public void deleteyVersionOrEnv(String nodeId) throws IOException {
		systemService.deleteVersionOrEnv(nodeId);
	}

	@RequestMapping("/updateAcl")
	@ResponseBody
	public void updateAcl(String envId) throws IOException {
		envService.updateAcl(Long.valueOf(envId));
	}

	@RequestMapping("/updateProperty")
	@ResponseBody
	public Long updateProperty(Long envPropertyId, String propertyValue,String propertyMemo,String propertyLabel) {
		return envService.updateProperty(envPropertyId, propertyValue,propertyMemo,propertyLabel);
	}
	@RequestMapping("/getHistory")
	@ResponseBody
	public EnvDetail getHistory(String envId) throws IOException {
		return envService.getPropertyHistory(envId);
	}

	@RequestMapping("/addProperty")
	@ResponseBody
	public Long addProperty(Long envId, String propertyKey, String propertyValue,String propertyLabel) {
		return envService.addProperty(envId, propertyKey, propertyValue,propertyLabel);
	}

	@RequestMapping("/deleteProperty")
	@ResponseBody
	public Long deleteProperty(Long envPropertyId) {
		return envService.deleteProperty(envPropertyId);
	}

	@RequestMapping("/export")
	public ResponseEntity<byte[]> export(Long envId) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		String fileName = new String("application.properties");// 为了解决中文名称乱码问题
		headers.setContentDispositionFormData("attachment", fileName);
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		byte[] result = envService.export(envId);
		return new ResponseEntity<byte[]>(result, headers, HttpStatus.CREATED);
	}

	@RequestMapping("/getconfig")
	@ResponseBody
	public Map<String, String> rest(String acl, HttpServletRequest request) {
		Map map = envService.export(acl, request.getRemoteAddr());
		return map;
	}

	@RequestMapping("/validateSystemCode")
	@ResponseBody
	public void validateSystemCode(String systemCode) {
		systemService.validateSystemCode(systemCode);
	}

	@RequestMapping("/validatePropertyKey")
	@ResponseBody
	public void validatePropertyKey(Long envId, String propertyKey) {
		systemService.validatePropertyKey(envId, propertyKey);
	}

	@ExceptionHandler
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public String exception(RuntimeException e) {
		e.printStackTrace();
		return e.getMessage();
	}
}