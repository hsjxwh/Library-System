package org.powernode.springboot.bean.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
//当用excel来插入图书馆藏的数据的时候，关于此次传输的结果
@Getter
@Setter
public class ImportBooksByExcelRes {
    List<String> sentences;
    private int totalCount =0;
    private int successCount =0;
    private int failCount =0;
    public ImportBooksByExcelRes() {
        sentences=new ArrayList<>();
    }

    public void addSentences(String sentence) {
        sentences.add(sentence);
    }

    public void addSuccess() {
        successCount++;
    }

    public void addFail() {
        failCount++;
    }

    public void addCount() {
        totalCount++;
    }
}
