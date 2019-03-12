package com.undergrowth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.lang3.time.DateUtils;
import org.assertj.core.util.Lists;

/**
 * 慢sql分析
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-01-15-15:39
 */
public class AnalyzeSlowSqlTest {

    public static void main(String[] args) throws IOException, ParseException {
        if (args.length != 1) {
            System.out.println("请输入慢日志文件路径");
            System.exit(1);
        }
        File slowSqlFile = new File(args[0]);
        if (!slowSqlFile.exists()) {
            System.out.println("输入的慢日志文件路径不存在");
            return;
        }
        String splitTime = "# Time";
        String splitUser = "# User@Host";
        String splitQueryTime = "# Query_time";
        String colonSep = ":";
        String andSep = "@";
        Pattern pattern = Pattern.compile("([a-zA-Z_]*:)");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(slowSqlFile)));
        String tmp = null;
        Boolean isNew = true;
        StringBuilder sqlQueryBuilder = new StringBuilder();
        List<QueryObjectTest> queryObjectTestList = Lists.newArrayList();
        QueryObjectTest queryObject = new QueryObjectTest();
        while ((tmp = reader.readLine()) != null) {
            if (tmp.startsWith(splitTime) && isNew) {
                queryObject.setTime(DateUtils.parseDate(tmp.split(colonSep)[1], "yyyy-MM-dd HH:mm:ss"));
                isNew = false;
            }
            if (tmp.startsWith(splitUser)) {
                queryObject.setUser(tmp.split(andSep)[1]);
                queryObject.setHost(tmp.split(andSep)[2]);
            }
            if (tmp.startsWith(splitQueryTime)) {

            }
        }
    }


}