package nl.ucan.rec2string;

import nl.ucan.rec2string.compiler.CodeNode;
import nl.ucan.rec2string.compiler.PackageWriter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

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
public class Rec2StringWriter implements PackageWriter {
    private static Log log = LogFactory.getLog(Rec2StringWriter.class);
    private Set<String> knownFunctions  = new HashSet<String>();

    public String getFileContent(String location) throws IOException  {
        URL url = ResourceUtils.getURL(location);
        URLConnection urlConnection = url.openConnection();
        String content = IOUtils.toString(urlConnection.getInputStream());
        return content;
    }

//    // type t_employees_type is table of emp%rowtype index by binary_integer;
//    public boolean isRowtype(CodeNode node) {
//           datatype = plsqlrecord
//           parent.datatype = plsqltable
//
//
//    }

    public String pks_fnc(CodeNode function) throws IOException {
        String value = "";
        if ( function.isNode()) {
            String pks_fnc_tpl = getFileContent("classpath:rec2string/pks_fnc.tpl");
            value = String.format(pks_fnc_tpl
                                   ,"toString"
                                   ,"pp_rec"
                                   ,StringUtils.lowerCase((function.getTypeName()+"."+function.getSubTypeName()))) +"\n";
        }  else if (function.getDataLevel() == 0  ) {
        }
        return value;
    }
    public String pkb_fnc(CodeNode parent) throws IOException {
        String value = "";
        if ( parent.isNode()) {
            String block = "";
            String prefix = "";
            if ( "PL/SQL TABLE".equals(parent.getDataType())) {
                for ( CodeNode child : parent.getChildNodes()) {
                    if ( block.length() >0 ) block += "\n \t \t \t \t";
                    block  += prefix+"'record' ,toString(pp_rec(i))";
                    prefix = ",";
                }
                String pkb_fnc_table = getFileContent("classpath:rec2string/pkb_fnc_table.tpl");
                value = String.format(pkb_fnc_table
                                     ,"toString"
                                     ,"pp_rec"
                                     ,StringUtils.lowerCase((parent.getTypeName()+"."+parent.getSubTypeName())));
            } else {
                boolean firstChild = true;
                Set ignore = new HashSet(Arrays.asList(new String[]{"pp_dummy","pp_new","donotchange1","donotchange2","donotchange3","donotchange4","donotchange5","pp_mutation"}));
                for ( CodeNode child : parent.getChildNodes()) {
                    List<String> labels = new ArrayList<String>();
                    labels.add(StringUtils.lowerCase(child.getArgument()));
                    if ( firstChild ) {
                        block  += String.format("acquirePart(pp_partName => vPrefix||'%1$s',pp_partValue => toString(pp_rec => pp_rec.%2$s,pp_quietCut => pp_quietCut,pp_prefix => vPrefix||'%1$s' ),pp_quietCut => pp_quietCut) \n",StringUtils.join(labels,'.'),child.getArgument());
                    } else {
                        block  += String.format(",acquirePart(pp_partName => vPrefix||'%1$s',pp_partValue => toString(pp_rec => pp_rec.%2$s,pp_quietCut => pp_quietCut,pp_prefix => vPrefix||'%1$s' ),pp_quietCut => pp_quietCut) \n",StringUtils.join(labels,'.'),child.getArgument());
                    }
                    firstChild = false;
                }
                String pkb_fnc = getFileContent("classpath:rec2string/pkb_fnc.tpl");
                value = String.format(pkb_fnc
                                     ,"toString"
                                     ,"pp_rec"
                                     ,StringUtils.lowerCase((parent.getTypeName()+"."+parent.getSubTypeName()))
                                     ,block)+"\n";
            }
        }  else if (parent.getDataLevel() == 0  ) {
        }
        return value;
    }
    public boolean known(CodeNode function) {
        String unique =  function.getTypeName()+"."+function.getSubTypeName();
        log.info(function);
        if (knownFunctions.contains(unique)) {
            return true;
        } else {
            knownFunctions.add(unique);
            return false;
        }
    }

    public void writeToFile(String outputDir,String fileName,List<CodeNode> codeNodes) throws IOException {
        String pks_fnc = "";
        String pkb_fnc = "";
        for ( CodeNode function : codeNodes ) {
            if ( function.isNode() ||  function.getDataLevel() == 0 ) {
                if ( !known(function) ) {
                    pks_fnc += pks_fnc(function);
                    pkb_fnc += pkb_fnc(function);
                }
            }
        }
        String pkb = getFileContent("classpath:rec2string/pkb.tpl");
        FileUtils.writeStringToFile(new File(outputDir,fileName+".pkb"),String.format(pkb,fileName,pkb_fnc));
        String pks = getFileContent("classpath:rec2string/pks.tpl");
        FileUtils.writeStringToFile(new File(outputDir,fileName+".pks"),String.format(pks,fileName,pks_fnc));
    }

}