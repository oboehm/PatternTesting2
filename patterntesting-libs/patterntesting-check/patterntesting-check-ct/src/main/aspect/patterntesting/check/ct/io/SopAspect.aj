/*
 * $Id: SopAspect.aj,v 1.2 2015/02/13 21:48:33 oboehm Exp $
 *
 * Copyright (c) 2008 by Oliver Boehm
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express orimplied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * (c)reated 14.09.2008 by oliver (ob@oasd.de)
 */
package patterntesting.check.ct.io;

import patterntesting.annotation.check.ct.*;

/**
 * Pattern Test that ensures that <code>System.out</code> and
 * <code>System.err</code> are not used in the code. This is
 * to prevent the usage of <code>System.out.println("")</code>
 * calls (and variations).
 *
 * Note: With AspectJ it is not possible to write a compile time
 * check that only ensures that <code>println()</code> calls are
 * not allowed. It is possible for runtime checks but we have
 * preferred to make a compile-time test for simplicity. Thus,
 * code that manipulate <code>System.out</code> and
 * <code>System.err</code> for other purpose that using the
 * <code>println()</code> methods will need to be added to the
 * list of items to exclude from the tests, hence the abstract
 * <code>allowedCode</code> pointcut.
 *
 * The difference to AbstractSopTest is that is not abstract - the check
 * is on by default and you have not to define the abstract pointcuts
 * "applicationCode" or "allowedCode". And it is only a warning when
 * the user violates the check which can be disabled by the annotations
 * "@SystemOutNeeded" and "@SystemErrNeeded".
 *
 * @author <a href="boehm@javatux.de">oliver</a>
 * @since 14.09.2008
 * @version $Revision: 1.2 $
 *
 * @see AbstractSopAspect
 */
public aspect SopAspect extends AbstractSopAspect {

    /**
     * Look at the whole code to detect System.out violation
     * (except main methods)
     */
    public pointcut applicationCode() :
    	within(*..*)
    	&& !withincode(
    	        /** e.g. for the hello-world example System.out is ok */
    	        
        	    /**
        	     * Allowed code.
        	     */
        	    public static void *..*.main(String[]))
    	;

    /**
     * You can suppress the System.out warning by the annotation
     * <code>@SuppressSystemOutWarning</code>.
     */
    @SuppressWarnings("javadoc")
    public pointcut allowedCode() :
    	@within(SuppressSystemOutWarning)
    	|| @withincode(SuppressSystemOutWarning)
    	;

}
