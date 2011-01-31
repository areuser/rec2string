package nl.ucan.rec2string.compiler;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
public class CodeGen {
    private String        dir;
    private String        fileName;
    private CodeNodeDAO codeNodeDAO;
    public  List<String>   process;
    public  List<PackageWriter>   writers;
    public CodeGen(String dir,String fileName,CodeNodeDAO codeNodeDAO) {
        this.dir = dir;
        this.fileName = fileName;
        this.codeNodeDAO = codeNodeDAO;
    }

    public void processAsDeclared() throws IOException {
        List<CodeNode> codeNodes =  new ArrayList<CodeNode>();
        if ( process.size() > 0 ) {
            for ( String element : process ) {
                String[] ownerObject = StringUtils.split(element,';');
                String owner = StringUtils.upperCase(ownerObject[0]);
                String[] objectName = StringUtils.split(ownerObject[1],'.');
                if ( objectName.length == 0 ) throw new IllegalStateException("declaration of property process is incorrect, please specify to be generated plsql objects ");
                if ( objectName.length == 1 ) {
                    String methodName  = StringUtils.upperCase(objectName[0]);
                    codeNodes.addAll(codeNodeDAO.findCodeNodes(methodName,owner));
                }
                if ( objectName.length == 2 ) {
                    String packageName = StringUtils.upperCase(objectName[0]);
                    String methodName  = StringUtils.upperCase(objectName[1]);
                    codeNodes.addAll(codeNodeDAO.findCodeNodes(packageName,methodName,owner));
                }
            }
        }
        for (PackageWriter writer : writers ) {
            writer.writeToFile(dir,fileName,codeNodes);
        }        
    }
    public void setProcess(List<String> process)  {
          this.process = process;
    }

    public void setWriters(List<PackageWriter> writers) {
        this.writers = writers;
    }
}


