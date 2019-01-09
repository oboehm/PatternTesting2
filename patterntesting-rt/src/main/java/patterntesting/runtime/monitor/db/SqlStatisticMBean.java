/*
 * $Id: SqlStatisticMBean.java,v 1.2 2016/12/10 20:55:22 oboehm Exp $
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
 * (c)reated 16.04.2014 by oliver (ob@oasd.de)
 */

package patterntesting.runtime.monitor.db;

import patterntesting.runtime.jmx.Description;
import patterntesting.runtime.monitor.ProfileStatisticMBean;

/**
 * The Interface SqlStatisticMBean.
 *
 * @author oliver
 * @since 1.4.2 (16.04.2014)
 * @deprecated since 2.0, use {@link clazzfish.jdbc.SqlStatisticMBean}
 */
@Deprecated
@Description("SQL statistic for different SQL statements")
public interface SqlStatisticMBean extends ProfileStatisticMBean {

}
