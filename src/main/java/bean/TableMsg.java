package bean;

//封装表的结构信息

public class TableMsg {

    private String tableName;       //表名
    private String tableType;       //表的类型
    private String columnName;      //列名
    private String columnType;   //列的类型
    private String columnComment;    //列的注释
    private String columnRemark;    //列的备注
    private boolean isParition; //是否是分区表

    public TableMsg() {
    }

    public TableMsg(String tableName, String tableType, String columnName, String columnType, String columnComment) {
        this.tableName = tableName;
        this.tableType = tableType;
        this.columnName = columnName;
        this.columnType = columnType;
        this.columnComment = columnComment;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public String getColumnRemark() {
        return columnRemark;
    }

    public void setColumnRemark(String columnRemark) {
        this.columnRemark = columnRemark;
    }

    public boolean isParition() {
        return isParition;
    }

    public void setParition(boolean parition) {
        isParition = parition;
    }

    @Override
    public String toString() {
        return "TableMsg{" +
                "tableName='" + tableName + '\'' +
                ", tableType='" + tableType + '\'' +
                ", columnName='" + columnName + '\'' +
                ", columnType='" + columnType + '\'' +
                ", columnComment='" + columnComment + '\'' +
                ", columnRemark='" + columnRemark + '\'' +
                ", isParition=" + isParition +
                '}';
    }
}
