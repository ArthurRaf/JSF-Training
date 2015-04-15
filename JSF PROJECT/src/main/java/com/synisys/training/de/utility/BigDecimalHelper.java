package com.synisys.training.de.utility;

import java.math.BigDecimal;


public class BigDecimalHelper {
	
	public static boolean equals(BigDecimal a, BigDecimal b){
		return a==null && b==null || a!=null && a.compareTo(b)==0;
	}
	
	public static int hash(BigDecimal a){
		return a==null?0: Double.valueOf(a.doubleValue()).hashCode();
	}
}
