package com.meneguello.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclarePrecedence;

@Aspect
@DeclarePrecedence("AsyncAspect, FXThreadAspect")
public class PrecedenceAspect {

}
