package com.undergrowth.refactoring;

/**
 * Description: TODO(展现类型)
 *
 * @author <a href="undergrowth@126.com">undergrowth</a>
 * @version 1.0.0
 * @date 2016年7月8日
 */
public enum ShowType {
    TXT(1), HTML(2);

    private int typeCode;

    ShowType(int typeCode) {
        this.typeCode = typeCode;
    }

    public ShowType getValueOf(String showType) {
        if ("txt".equals(showType)) {
            return TXT;
        } else if ("html".equals(showType)) {
            return HTML;
        }
        return null;
    }

}
