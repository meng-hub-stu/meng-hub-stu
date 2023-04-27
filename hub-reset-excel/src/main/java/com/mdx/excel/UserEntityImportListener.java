package com.mdx.excel;

import lombok.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author Mengdl
 * @date 2023/03/13
 */
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserEntityImportListener extends BaseImportListener<UserImport>{



    @Override
    protected UserImport convertEntity(CellDataDTO cellData) {
        String name = cellData.get(Head.NAME);
        String age = cellData.get(Head.AGE);
        String date = cellData.get(Head.DATE);
        UserImport userImport = null;
        try {
            userImport = UserImport.builder()
                    .age(Integer.parseInt(age))
                    .time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date))
                    .name(name)
                    .build();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return userImport;
    }

    @Override
    protected Enum<? extends HeadEnum>[] getHead() {
        return Head.values();
    }

    @AllArgsConstructor
    @Getter
    private enum Head implements HeadEnum {
        /**
         * 姓名
         */
        NAME("姓名"),
        /**
         * 年龄
         */
        AGE("年龄"),
        /**
         * 时间
         */
        DATE("操作时间");

        private final String fieldName;

        @Override
        public Integer getIndex() {
            return ordinal();
        }

    }

}
