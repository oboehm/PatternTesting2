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

import java.util.Enumeration;

/**
 * The Class PlainFormatter.
 *
 * @author <a href="mailto=matt@ojojo.com">Matt Smith</a>
 * @version $Id: PlainFormatter.java,v 1.4 2016/01/06 20:47:32 oboehm Exp $
 */
public final class PlainFormatter implements ResultFormatter
{

   /**
    * Format.
    *
    * @param ajcFileResults the ajc file results
    * @return the string
    * @see patterntesting.tool.aspectj.ResultFormatter#format(java.util.Enumeration)
    */
   public String format(final Enumeration<AjcFileResult> ajcFileResults)
   {
      StringBuffer buffer = new StringBuffer();
      while(ajcFileResults.hasMoreElements())
      {
         AjcFileResult result = ajcFileResults.nextElement();
         buffer.append(result.toString());
         buffer.append("\n");
      }
      return buffer.toString();
   }

}
