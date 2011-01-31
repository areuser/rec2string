package nl.ucan.rec2string.compiler;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class CodeNodeDAO {
    private static Log log = LogFactory.getLog(CodeNodeDAO.class);
    private JdbcTemplate jdbcTemplate;

    public CodeNodeDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private List<CodeNode> aquireCodeNodes(String packageName,String objectName,String owner) {
        //
        // determine persisted nodes
        final List<CodeNode> nodes = new ArrayList<CodeNode>();
        try {
          String sql = null;
          Object[] params = null;
          int[] types = null;
          if ( StringUtils.isBlank(packageName) ) {
              sql =   "select * from all_arguments where object_name = upper(?) and owner = upper(?) order by sequence asc  ";
              params = new Object[] {objectName,owner};
              types = new int[] {Types.VARCHAR,Types.VARCHAR};
          } else {
              sql =   "select * from all_arguments where package_name = upper(?) and object_name = upper(?) and owner = upper(?) order by sequence asc  ";
              params = new Object[] {packageName,objectName,owner};
              types = new int[] {Types.VARCHAR,Types.VARCHAR,Types.VARCHAR};              
          }
          jdbcTemplate.query(sql,params,types, new RowCallbackHandler() {
                public void processRow(ResultSet rs) throws SQLException {
                    CodeNode codeNode = new CodeNode();
                    codeNode.setArgument(rs.getString("ARGUMENT_NAME"));
                    codeNode.setObjectName(rs.getString("OBJECT_NAME"));
                    codeNode.setPackageName(rs.getString("PACKAGE_NAME"));
                    codeNode.setTypeName(rs.getString("TYPE_NAME"));
                    codeNode.setSubTypeName(rs.getString("TYPE_SUBNAME"));
                    codeNode.setSequence(rs.getInt("SEQUENCE"));
                    codeNode.setObjectId(rs.getInt("OBJECT_ID"));
                    codeNode.setDataLevel(rs.getInt("DATA_LEVEL"));
                    codeNode.setDataType(rs.getString("DATA_TYPE"));
                    codeNode.setSubProgramId(rs.getInt("SUBPROGRAM_ID"));
                    nodes.add(codeNode);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        // determine hierarchy
        Map<Integer,CodeNode>  slots = new HashMap<Integer,CodeNode>();
        for ( CodeNode node : nodes ) {
            // Check which data is double.
            CodeNode parent = slots.get(node.getDataLevel()-1);
            if ( parent != null ) {
                parent.getChildNodes().add(node);
                node.setParent(parent);
            }
            slots.put(node.getDataLevel(),node);
        }
        return nodes;
    }

    public List<CodeNode> findCodeNodes(String functionName,String owner) {
        String packageName = null;
        String objectName = functionName;
        return aquireCodeNodes(packageName,objectName,owner);
    }

    public List<CodeNode> findCodeNodes(String packageName,String functionName,String owner) {
        String objectName = functionName;
        return aquireCodeNodes(packageName,objectName,owner);
    }

}
