package com.YaNan.frame.plugs.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * 
 * 
 * @author Administrator
 *
 */
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
@Target(ElementType.TYPE )
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {

	boolean multi() default false;
	
	
}
