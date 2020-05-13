package showdoc;

import bean.PartitionMsg;
import bean.TableMsg;
import bean.TotalTable;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import utils.ReadMysqlObj;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReadSchema {

    private QueryRunner queryRunner = null;
    ReadMysqlObj readMysqlObj = null;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ReadSchema() {
    }

    public ReadSchema(Properties pros) {
        queryRunner = new QueryRunner();
        readMysqlObj = new ReadMysqlObj(pros);
    }


    //查询出某一个值
    public Object getForValue(String sql ,Object ...args){

        Connection connection = null;
        try{
            connection = readMysqlObj.getConnection();
            Object value = queryRunner.query(connection, sql, new ScalarHandler<Object>(), args);
            return value;
        }catch (Exception e){

        }finally {
            readMysqlObj.release(connection);
        }
        return null;
    }



    //查询指定库下所有的表
    public List<TotalTable> getAllTables(String sql, Object ...args){

        Connection connection = null;
        try{
            connection = readMysqlObj.getConnection();
            return queryRunner.query(connection,sql,new BeanListHandler<TotalTable>(TotalTable.class),args);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            readMysqlObj.release(connection);
        }
        return null;
    }

    //查询出一张表结构
    public  List<TableMsg> getTableMsgForList(String sql, Object... args){
        Connection connection = null;
        try {
            connection = readMysqlObj.getConnection();

            return queryRunner.query(connection,sql,new BeanListHandler<TableMsg>(TableMsg.class),args);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            readMysqlObj.release(connection);
        }
        return null;
    }


    //将一张表结构使用 markDown语法封装
    public  String getStringWithMarkDown(List<TableMsg> list){

        String tableName = list.listIterator().next().getTableName();
        String tableType = list.listIterator().next().getTableType();
        String partition = list.listIterator().next().isParition()?"分区表" : "";

        tableType = "EXTERNAL_TABLE".equals(tableType) ? "外部表" : "内部表";


        StringBuilder sb = new StringBuilder("-  " + tableName  + " : " + tableType + " " + partition + "\n\n");

        sb.append("|字段|类型|注释|备注|\n");
        sb.append("|:----    |:-------    |-- -|------      |\n");

        for (TableMsg tableMsg : list) {

            String columnComment = tableMsg.getColumnComment() == null || "".equals(tableMsg.getColumnComment()) || "\t".equals(tableMsg.getColumnComment()) ? "\t" : tableMsg.getColumnComment().replaceAll("\r\n", " ");
            String columnRemark = tableMsg.getColumnRemark() == null || "".equals(tableMsg.getColumnRemark()) || "\t".equals(tableMsg.getColumnRemark()) ? "\t" : tableMsg.getColumnRemark().replaceAll("\n", " ");

            String line = "|" + tableMsg.getColumnName() + "|" + tableMsg.getColumnType() +
                    "|" + columnComment.replaceAll("\\|"," ") + "|" + columnRemark + "|" + "\n";
            sb.append(line);
        }


        String dateStr = this.format.format(new Date());
        sb.append("\t最近更新时间: "+dateStr);

        return sb.toString();
    }

    //填充tableMsgForList里每张表的 remark属性.
    public void fillRemark(String db, List<TableMsg> tableMsgForList, HashMap<String, String> dbTableColumn2Remark) {

        for (TableMsg tableMsg : tableMsgForList) {
            String tableName = tableMsg.getTableName();
            String columnName = tableMsg.getColumnName();
            String key = db + "_" + tableName + "_" + columnName;
            String value = dbTableColumn2Remark.get(key);
            value = value == null ? "" : value;
            tableMsg.setColumnRemark(value);
        }
    }

    //判断是否是分区表
    public void isPartition(String tableId, List<TableMsg> tableMsgForList) {
        Connection connection = null;
        try{
            connection = readMysqlObj.getConnection();
            String sql = "select pkey_name partitionName,pkey_type partitionType,pkey_comment partitionComment " +
                    "from partition_keys where tbl_id = ? order by integer_idx";
            List<PartitionMsg> query = queryRunner.query(connection, sql, new BeanListHandler<PartitionMsg>(PartitionMsg.class), tableId);

            //不是分区表
            if(query.size() == 0){
                for (TableMsg tableMsg : tableMsgForList) {
                    tableMsg.setParition(false);
                }
            }else{
                //是分区表
                String tableName = tableMsgForList.listIterator().next().getTableName();
                String tableType = tableMsgForList.listIterator().next().getTableType();


                for (PartitionMsg partitionMsg : query) {

                    String partitionComment = partitionMsg.getPartitionComment();
                    String comment = partitionComment == null ? "" : partitionComment;

                    tableMsgForList.add(new TableMsg(tableName,tableType, partitionMsg.getPartitionName(),
                            partitionMsg.getPartitionType(),comment));
                }
                for (TableMsg tableMsg : tableMsgForList) {
                    tableMsg.setParition(true);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            readMysqlObj.release(connection);
        }

    }



}
