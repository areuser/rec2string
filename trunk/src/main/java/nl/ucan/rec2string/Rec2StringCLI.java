package nl.ucan.rec2string;

import nl.ucan.rec2string.compiler.CodeGen;
import org.apache.commons.cli.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;


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
public class Rec2StringCLI {
    private static Log log = LogFactory.getLog(Rec2StringCLI.class);
    private HashMap<String,String> properties = new HashMap();

    /**
     * command line options
     */
    public static void main(String[] s) throws Exception {
        try {
            ApplicationContext context = SpringContext.acquireApplicationContext();
            JdbcTemplate jdbcTemplate = (JdbcTemplate)context.getBean("jdbcTemplate");
            Rec2StringCLI commandLine = new Rec2StringCLI(s);
            HashMap<String,String> properties = commandLine.getProperties();
            CodeGen codeGen = (CodeGen)context.getBean("codeGen");
            log.info("generator starts processing");
            codeGen.processAsDeclared();
            log.info("generator finished processing");
            System.exit(0);
		}catch(Throwable e) {
            e.printStackTrace();
            System.exit(1);
		}
    }

    private HashMap getProperties() {
        return this.properties;
    }
    public Rec2StringCLI(String[] s) {
		Options options = new Options();
		CommandLineParser parser = new BasicParser();
		CommandLine cmd;
		try{
			cmd = parser.parse(options, s);
            if ( cmd.getArgList().size() > 0 ) {
                throw new IllegalStateException("unsupported command line options used");
            }
		}catch (ParseException pe){
		    log.error("generator failed, error : ",pe);
			HelpFormatter formatter = new HelpFormatter();
            formatter.defaultWidth=150;
			formatter.printHelp(pe.getMessage(),"",options,"example of usage : all configuration parameters are part of configuration.xml \n objects that should be generated should be specified as property of codegen as part of the configuration file.");
            throw new IllegalStateException("exception raised, command line options are not correctly specified");
		}
    }

    protected void finalize() throws Throwable {
        // when the HUP signal is executed ( kill <pid> ) finalize is called
    }
}
