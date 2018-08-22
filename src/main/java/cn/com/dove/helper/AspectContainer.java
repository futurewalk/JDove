package cn.com.dove.helper;

import java.lang.reflect.Method;
import java.util.Map;

public class AspectContainer {
	 private Method method;
	 private String optType;

	 public Method getMethod() {
		  return method;
	 }

	 public void setMethod(Method method) {
		  this.method = method;
	 }

	 public String getOptType() {
		  return optType;
	 }

	 public void setOptType(String optType) {
		  this.optType = optType;
	 }
}