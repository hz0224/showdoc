package bean;

public class PartitionMsg {
    private String partitionName; //分区字段
    private String partitionType; //分区字段类型
    private String partitionComment; //分区字段注释


    public String getPartitionName() {
        return partitionName;
    }

    public void setPartitionName(String partitionName) {
        this.partitionName = partitionName;
    }

    public String getPartitionType() {
        return partitionType;
    }

    public void setPartitionType(String partitionType) {
        this.partitionType = partitionType;
    }

    public String getPartitionComment() {
        return partitionComment;
    }

    public void setPartitionComment(String partitionComment) {
        this.partitionComment = partitionComment;
    }

    @Override
    public String toString() {
        return "PartitionMsg{" +
                "partitionName='" + partitionName + '\'' +
                ", partitionType='" + partitionType + '\'' +
                ", partitionComment='" + partitionComment + '\'' +
                '}';
    }
}
