package com.mdx.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.read.listener.ModelBuildEventListener;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static cn.hutool.core.bean.BeanUtil.isEmpty;
import static cn.hutool.core.bean.BeanUtil.isNotEmpty;
import static cn.hutool.core.convert.Convert.toStr;
import static java.util.Arrays.stream;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author Mengdl
 * @date 2023/03/13
 */
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Slf4j
public abstract class BaseImportListener<E extends ExcelEntity> extends ModelBuildEventListener {

    /**
     * 最大导入条数
     * TODO 暂时上限调至5000条
     */
    protected int maxCount = 500;

    /**
     * 行数
     */
    protected int row = 1;

    /**
     * 正常条数
     */
    protected int successCount = 0;

    /**
     * 异常条数
     */
    protected int errorCount = 0;

    /**
     * 缓存的数据列表
     */
    protected List<E> list = new ArrayList<>();

    /**
     * 成功的缓存数据列表
     */
    protected List<E> successList = new ArrayList<>();

    /**
     * 告警提示
     */
    protected List<String> warningPrompts = new ArrayList<>();

    /**
     * 错误提示
     */
    protected List<String> errorPrompts = new ArrayList<>();

    /**
     * 成功标识
     */
    private boolean success = false;

    /**
     * 错误提示
     */
    private String exceptionMsg = null;

    protected boolean headCheck = false;

    protected boolean hasError = false;

    private Map<Integer, List<String>> errorMap = Maps.newHashMap();

    private Map<Integer, List<String>> warnMap = Maps.newHashMap();

    @Override
    public void invokeHead(Map<Integer, CellData> cellDataMap, AnalysisContext context) {
        if (headRowNumber() == row) {
            Enum<? extends HeadEnum>[] headData = getHead();
            headCheck = cellDataMap.size() == headData.length && stream(headData).allMatch(head ->
                    ((HeadEnum) head).getFieldName().equals(toStr(cellDataMap.get(((HeadEnum) head).getIndex()), null)));
            if (log.isDebugEnabled()) {
                log.debug("cellSize:{},cellData:{},headerSize:{},headerDate:{}", cellDataMap.size(), cellDataMap, headData.length, headData);
            }
        }
    }

    @Override
    public boolean hasNext(AnalysisContext context) {
        row++;
        boolean hasNext = getMaxCount() >= successCount + errorCount;
        if (!hasNext) {
            exceptionMsg = "最多支持导入" + getMaxCount() + "条数据！";
        }
        hasNext = (headCheck || row <= headRowNumber()) && hasNext;
        if (!hasNext) {
            doFinish();
        }
        return hasNext;
    }

    @Override
    public void invoke(Map<Integer, CellData> cellDataMap, AnalysisContext context) {
        hasError = false;
        int currentErrorSize = errorPrompts.size();
        try {
            E entity = convertEntity(new CellDataDTO(cellDataMap));

            if (errorPrompts.size() > currentErrorSize) {
                errorCount++;
            } else {
                successCount++;
                successList.add(entity);
            }
            if (null != entity) {
                list.add(entity);
            }
        } catch (Exception e) {
            log.error("导入异常", e);
            addErrorPrompt("内部服务异常！");
            errorCount++;
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        doFinish();
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) {
        exception.printStackTrace();
    }

    protected void addErrorPrompt(String prompt) {
        if (isNotEmpty(prompt)) {
            List<String> list = this.errorMap.getOrDefault(row - headRowNumber() - 1, Lists.newArrayList());
            list.add(prompt);
            errorMap.put(row - headRowNumber() - 1, list);
            errorPrompts.add("第" + row + "行：" + prompt);
            hasError = true;
        }
    }

    protected void addErrorPrompt(HeadEnum head, String prompt) {
        addErrorPrompt("【" + head.getFieldName() + "】" + prompt);
    }

    protected void addWarningPrompts(String prompt) {
        List<String> list = this.warnMap.getOrDefault(row - headRowNumber() - 1, Lists.newArrayList());
        list.add(prompt);
        warnMap.put(row - headRowNumber() - 1, list);
        warningPrompts.add("第" + row + "行：" + prompt);
    }

    protected void doFinish() {
        if (!headCheck) {
            exceptionMsg = "导入模板表头校验不通过，请检查导入模板！";
        }
        if (isEmpty(list) && errorMap.isEmpty() && warnMap.isEmpty()) {
            exceptionMsg = "有问题";
        }
        success = errorCount == 0 && null == exceptionMsg && isNotEmpty(list);
    }

    protected abstract E convertEntity(CellDataDTO cellData) throws ParseException;

    protected abstract Enum<? extends HeadEnum>[] getHead();

    protected int headRowNumber() {
        return 5;
    }

    protected interface HeadEnum {

        /**
         * 列索引
         *
         * @return 索引
         */
        Integer getIndex();

        /**
         * 字段名称
         *
         * @return 字段名称
         */
        String getFieldName();

    }

    @AllArgsConstructor
    @Getter
    @ToString
    protected static class CellDataDTO {

        private final Map<Integer, CellData> cellDataMap;

        public String get(HeadEnum head) {
            Object data = cellDataMap.get(head.getIndex());
            return null != data ? String.valueOf(data).trim() : null;
        }

        @SafeVarargs
        public final String getStr(HeadEnum head, BiPredicate<String, String>... checks) {
            return get(head, this::toStr, checks);
        }

        @SafeVarargs
        public final <T> T get(HeadEnum head, Function<String, T> convert, BiPredicate<String, T>... checks) {
            return get(head, convertCellData(convert), checks);
        }

        protected <T> BiFunction<String, String, T> convertCellData(Function<String, T> convert) {
            return (fieldName, data) -> {
                try {
                    return convert.apply(data);
                } catch (Exception e) {
                    throw new RuntimeException(fieldName);
                }
            };
        }

        @SafeVarargs
        public final <T> T get(HeadEnum head, BiFunction<String, String, T> convert, BiPredicate<String, T>... checks) {
            T value;
            try {
                value = convert.apply(head.getFieldName(), get(head));
                if (isEmpty(checks)) {
                    return value;
                }
                boolean flag = stream(checks).collect(() -> isNotEmpty(checks),
                        (r, check) -> r = r && check.test(head.getFieldName(), value), (a, b) -> a = a && b);
                return flag ? value : null;
            } catch (Exception e) {
                return null;
            }
        }

        private String toStr(final String value) {
            return isNotBlank(value) ? value : null;
        }

    }

}
