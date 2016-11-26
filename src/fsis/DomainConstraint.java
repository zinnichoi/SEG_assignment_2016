package fsis;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @overview Defines a domain constraint of some attribute
 * @author dmle
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainConstraint {
  public String type() default "null";
  public boolean mutable() default true;
  public boolean optional() default true;
  public int length() default  -1;
  public double min() default Double.NaN;
  public double max() default Double.NaN;
}
