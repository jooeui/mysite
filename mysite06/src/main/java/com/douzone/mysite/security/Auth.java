package com.douzone.mysite.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})		// class는 Target ElementType을 TYPE으로 써줌 - Admin 같은 경우는 모두 제한을 해야하므로 class에 사용
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {
//	public String value() default "USER";
	public String role() default "USER";	// controller에서 @Auth, @Auth()를 입력하여도 default인 USER가 적용
	// public boolean test() default false;
}
