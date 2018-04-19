package com.undergrowth;

import java.util.Date;

/**
 * 查询对象
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-01-16-10:48
 */
public class QueryObjectTest {

    private Date time;
    private String user;
    private String host;
    private Double queryTime;
    private Integer rowsExamined;
    private String sql;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Double getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(Double queryTime) {
        this.queryTime = queryTime;
    }

    public Integer getRowsExamined() {
        return rowsExamined;
    }

    public void setRowsExamined(Integer rowsExamined) {
        this.rowsExamined = rowsExamined;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}