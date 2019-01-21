/* 
 *========================================================================
 * 
 * Copyright 2001-2004 Vincent Massol & Matt Smith.
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
 *========================================================================
 */
package patterntesting.tool.aspectj;

/**
 * Simple aspect to trigger an Ajc error.
 * 
 * @author <a href="mailto:vmassol@apache.org">Vincent Massol</a>
 * @version $Id: TestAspect.aj,v 1.1 2010/01/05 17:09:48 oboehm Exp $
 * @since 0.5: adapted to AspectJ 5
 */
public aspect TestAspect
{
    pointcut testError() :
        execution(public void Test.testError*());
    
    pointcut testWarning() :
    	execution(public void Test.testWarning*());
    
    declare error: testError() : 
        "Test error";
    
    declare warning: testWarning() :
        "Test warning";
    
}
