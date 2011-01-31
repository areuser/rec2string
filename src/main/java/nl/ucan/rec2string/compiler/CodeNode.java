package nl.ucan.rec2string.compiler;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

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
public class CodeNode {
  private List<CodeNode> childNodes = new ArrayList();
  private CodeNode  parent;
  private String argument;
  private String typeName;
  private Integer sequence;
  private String subTypeName;
  private Integer objectId;
  private Integer dataLevel;
  private Integer subProgramId;
  private String dataType;
  private String objectName;
  private String packageName;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getSubTypeName() {
        return subTypeName;
    }

    public void setSubTypeName(String subTypeName) {
        this.subTypeName = subTypeName;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getDataLevel() {
        return dataLevel;
    }

    public void setDataLevel(Integer dataLevel) {
        this.dataLevel = dataLevel;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<CodeNode> getChildNodes() {
        return childNodes;
    }
    public boolean isLeaf() {
        return !isNode();
    }
    public boolean isNode() {
        boolean isPlainLeaf = this.getChildNodes().size() == 0;
        boolean isNode = false;
        if ( !isPlainLeaf) {
            //
            // if dataType = TABLE and typeName = VARCHARS_T, NODES_T, ...
            // then the node is still a leaf, otherwise it's a node
            if ( "TABLE".equals(this.getDataType())) {
                if ( "VARCHARS_T".equals(this.getTypeName())) {
                    isNode = false;
                } else if ( "NUMBERS_T".equals(this.getTypeName())) {
                    isNode = false;
                } else if ( "VARCHARS_TT".equals(this.getTypeName())) {
                    isNode = false;
                } else {
                    isNode = true;
                }
            } else {
                isNode = true;
            }
        }
        return isNode;
    }

    public String acquireNestedPath() {
        if ( this.getParent() != null ) {
            return StringUtils.lowerCase(this.getParent().acquirePath()+"."+this.getArgument());
        } else return this.getArgument();
    }
    public String acquireNestedPath(CodeNode upToParent) {
        if ( upToParent != null && !ObjectUtils.equals(this.getParent(),upToParent)) {
            return StringUtils.lowerCase(this.getParent().acquirePath()+"."+this.getArgument());
        } else return StringUtils.lowerCase(this.getArgument());
    }

    public String acquirePath() {
        return StringUtils.lowerCase(this.getArgument()); 
    }

    public CodeNode getParent() {
        return parent;
    }

    public void setParent(CodeNode parent) {
        this.parent = parent;
    }

    public Integer getSubProgramId() {
        return subProgramId;
    }

    public void setSubProgramId(Integer subProgramId) {
        this.subProgramId = subProgramId;
    }

    public boolean equals(Object o) {
        CodeNode codeNode = (CodeNode) o;
        return ObjectUtils.equals(codeNode.getSequence(),this.getSequence());
    }

    public int hashCode() {
        return this.getSequence();
    }

    public String toString() {
        return this.getPackageName()+"-"+this.getObjectName()+"-"+this.getSequence() +"-"+this.getSubProgramId()+"-"+this.getDataLevel()+"-"+this.getArgument()+" ("+this.getDataType()+") "+this.getTypeName()+"."+this.getSubTypeName();

    }

}
