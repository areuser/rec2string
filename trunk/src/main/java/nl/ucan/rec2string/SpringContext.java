package nl.ucan.rec2string;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/*
* Copyright 2008 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
* author : Arnold Reuser
* since  : 0.1
*/
public class SpringContext {
  public static Log log = LogFactory.getLog(SpringContext.class);
  public static ApplicationContext CONTEXT = acquireApplicationContext();
  
  
  public static ApplicationContext acquireApplicationContext() {
      String applicationContext = System.getProperty("applicationcontext");
      if ( StringUtils.isNotBlank(applicationContext)) {      
          log.info("applicationontext is specified, therefore configurationfile is read from "+applicationContext);              
          return new FileSystemXmlApplicationContext(applicationContext);    
      } else {
          log.info("applicationontext is not specified, therefore configuration.xml is read from classpath");
          return new ClassPathXmlApplicationContext("configuration.xml");
      }
  }
     
}
