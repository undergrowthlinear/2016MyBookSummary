package com.undergrowth.google.gson.jsonadapter;

import com.google.gson.annotations.JsonAdapter;

/**
 * 用户信息
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2017-05-12-17:24
 */
@JsonAdapter(UserJsonAdapter.class)
public class UserInfo {

    public String firstName;
    public String lastName;

    public UserInfo(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserInfo)) {
            return false;
        }

        UserInfo userInfo = (UserInfo) o;

        if (!firstName.equals(userInfo.firstName)) {
            return false;
        }
        return lastName.equals(userInfo.lastName);

    }

    @Override
    public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        return result;
    }
}