package com.ikuboo.tools.limiter;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LimiterAnn {
    int limiter();
}
