package showdoc;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Test {

    public static void main(String[] args) throws SQLException, IOException {


        String str = "-  ods_custom_service_log_history_callout , 外部表 \n" +
                "\n" +
                "|字段|类型|注释|备注|\n" +
                "|:----    |:-------    |-- -|------      |\n" +
                "|id|bigint|自增id|年后|\n" +
                "|custom_id|bigint|客服id|\t|\n" +
                "|user_id|bigint|用户id|\t|\n" +
                "|user_name|string|用户名称|\t|\n" +
                "|service_type|int|1：客户来电 2 电话回访|\t|\n" +
                "|product_id|bigint|产品id|\t|\n" +
                "|product_name|string|产品名称|\t|\n" +
                "|content|string|服务信息|\t|\n" +
                "|create_time|timestamp|创建时间|\t|\n" +
                "|update_time|timestamp|更新时间|\t|\n" +
                "|is_deleted|boolean|是否删除|\t|\n" +
                "|return_visit_time|bigint|回访时间|\t|\n" +
                "|connection_time|bigint|通话时间|\t|\n" +
                "|first_node|string|问题类型|\t|\n" +
                "|second_node|string|问题类型|\t|\n" +
                "|third_node|string|问题类型|\t|\n" +
                "|custom_name|string|客服名称|\t|\n" +
                "|mobile|string|手机号|\t|\n" +
                "|user_type|int|1 普通用户  2 未注册用户|\t|\n" +
                "\t最近更新时间: 2019-11-13 17:32:37";

            String[] split = str.split("\n");

        HashMap<String, String> dbTableColumn2Remark  = new HashMap<String, String>();

            for(int i = 4 ; i<split.length - 1 ; i++){
                String remark = "";
                String[] strings = split[i].split("\\|");
                String column = strings[1];
                if(strings.length == 5){
                    remark = strings[4];
                }
                String key = "hive_ods" + "_" + "src_test" + "_" + column;
                dbTableColumn2Remark.put(key,remark);
            }

        for (Map.Entry<String, String> entry : dbTableColumn2Remark.entrySet()) {
            System.out.println(entry.getKey() + "  " + entry.getValue());
        }


    }


}
