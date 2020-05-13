package showdoc;

import bean.Page;
import bean.TableMsg;
import bean.TotalTable;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        //判断是否传入了参数
        if(args.length==0){
            System.out.println("需要一个参数");
            return;
        }

        //加载传来的配置文件
        Properties pros = loadFile(args[0]);

        ReadSchema readSchema = new ReadSchema(pros);
        WriteSchema writeSchema = new WriteSchema();


        //获取要同步的所有库
        String[] dbs = pros.getProperty("dbs").split(",");

        //获取更新sql语句
        String sql = pros.getProperty("getTablesSql");

        //获取showdoc上数据字典这个项目的 item_id
        int item_id = Integer.parseInt(pros.getProperty("item_id"));

        for (String db : dbs) {

            //获取真正的数据库名
            String[] data = db.split("_");
            String dbName = data[data.length - 1];

            //获取这个数据库的id
            String queryDBIdSql = "select db_id from dbs where name =?";
            Object dbId = readSchema.getForValue(queryDBIdSql, dbName);

            //获取该库下要更新的表.
            List<TotalTable> tableList = readSchema.getAllTables(sql,dbId);
            if(tableList.size() ==0 ) continue;


            //存储备注信息,便于进行join
            HashMap<String, String> dbTableColumn2Remark  = new HashMap<String, String>();

            //保存备注到 dbTableColumn2Remark中。
            writeSchema.parseRemark(db,tableList,dbTableColumn2Remark);

            //存储的是 catName与catId的对应关系
            HashMap<String, Integer> catName2CatId = new HashMap<String, Integer>();

            //清空showdoc上该数据库目录下指定的页面
            writeSchema.cleanShowdocDir(item_id,db,catName2CatId,tableList);


            //pageId 自增
            int pageId  = writeSchema.getMaxPageId();

            ArrayList<Page> pages = new ArrayList<Page>();

            //同步该库下要更新的表
            for (TotalTable table : tableList) {
                //查询出这张表的结构
               String sql1 = "select \n" +
                       "    t.tbl_name tableName,\n" +
                       "    t.tbl_type tableType,\n" +
                       "    c.column_name columnName,\n" +
                       "    c.type_name columnType,\n" +
                       "    c.comment columnComment\n" +
                       "from \n" +
                       "    tbls t,\n" +
                       "    columns_v2 c,\n" +
                       "    sds s\n" +
                       "where\n" +
                       "    t.sd_id = s.sd_id and s.cd_id = c.cd_id and t.tbl_id = ?\n" +
                       "order by \n" +
                       "    c.integer_idx";

                List<TableMsg> tableMsgForList = readSchema.getTableMsgForList(sql1,table.getTableId());
                if(tableMsgForList.size()==0) continue;

                //判断是否是分区表
                readSchema.isPartition(table.getTableId(),tableMsgForList);

                //填充tableMsgForList里每张表的 remark属性.
                readSchema.fillRemark(db,tableMsgForList,dbTableColumn2Remark);

                //将表结构封装成markDown语法.
                String stringWithMarkDown = readSchema.getStringWithMarkDown(tableMsgForList);

                pageId++;
                Page page = new Page(pageId,1,"showdoc",item_id,catName2CatId.get(db),table.getTableName(),
                        "",stringWithMarkDown,99,System.currentTimeMillis());
                pages.add(page);
            }

            //批量插入
            writeSchema.insertBatchToShowDoc(pages);
        }
    }

    //加载配置文件
    private static Properties loadFile(String path) {
        Properties pros = new Properties();

        try {
            BufferedReader in = new BufferedReader( new InputStreamReader(new FileInputStream(path),"UTF-8"));
            pros.load(in);
        } catch (Exception e){
            e.printStackTrace();
        }
        return pros;
    }

}
