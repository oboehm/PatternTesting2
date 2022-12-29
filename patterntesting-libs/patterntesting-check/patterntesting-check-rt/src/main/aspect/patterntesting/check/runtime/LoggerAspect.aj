/*
 * $Id: LoggerAspect.aj,v 1.5 2016/12/18 21:59:31 oboehm Exp $
 *
 * Copyright (c) 2014 by Oliver Boehm
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
 * (c)reated 06.02.2014 by oliver (ob@oasd.de)
 */

package patterntesting.check.runtime;

/**
 * If you use a static {@link org.slf4j.Logger} it can happen that you use the
 * logger for the wrong class. This can happen if you copy the create statement
 * from another class and forget to change the classname. E.g. if you copy
 * 
 * <pre>
 * private static final Logger log = LoggerFactory.getLogger(MyClass.class);
 * </pre>
 * 
 * at the beginning of AnotherClass, you should not forget to change the
 * argument of {@link org.slf4j.LoggerFactory#getLogger(Class)} to
 * "AnotherClass.class".
 * <p>
 * This aspect reminds you and prints a warning if you forgot it.
 * </p>
 * 
 * @author oliver
 * @since 1.4.1 (06.02.2014)
 */
public aspect LoggerAspect extends AbstractLoggerAspect {

    /**
     * Application code is the whole application.
     */
    public pointcut applicationCode() :
        !suppressWarning();

    private pointcut suppressWarning() :
        execution(@SuppressLoggerWarning * *..*(*, ..))
        || execution(@SuppressLoggerWarning *..new(*, ..))
        || execution(@SuppressLoggerWarning *..*$*.new(..))
        || within(@SuppressLoggerWarning *..*);

}
