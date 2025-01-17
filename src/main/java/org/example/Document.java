package org.example;

import com.hankcs.hanlp.seg.common.Term;
import lombok.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Document {

    //是否是内规文件（若为否，department为空）
    boolean isInternal;
    //文件名称
    String text;
    //所属部门（内规）
    String department;
    //分词列表
    List<Term> terms = new ArrayList<>();

    Map<String, Integer> termMap = new HashMap<>();
    Map<String, Double> TFMap = new HashMap<>();
    Map<String, Double> IDFMap = new HashMap<>();
    Map<String, Double> TF_IDFMap = new HashMap<>();


    /**
     * 预处理
     */
    public void preHandle() {

    }

    /**
     * 计算IF-IDF
     */
    public void calIF_IDF() {
        for (String word : TFMap.keySet()) {
            if (IDFMap.containsKey(word)) {
                Double valueTF = TFMap.get(word);
                Double valueIDF = IDFMap.get(word);
                TF_IDFMap.put(word, valueIDF * valueTF);
            } else {
                System.err.println("This word exist in TFMap but don't exist in IDFMap:" + word);
            }
        }
        assert TFMap.size() == TF_IDFMap.size() : "size of TFMap is not equal TF_IDFMap.size";
    }

    /**
     * 计算TF
     * @param standard 使用哪种标准进行计算，true表示除数为term总数
     */
    public void calTF(boolean standard) {
        int maxFre = 0;
        for (Term term : terms) {
            String word = term.toString();
            if(termMap.get(word) == null){
                termMap.put(word, 1);
            } else{
                termMap.put(word, termMap.get(word) + 1);
                maxFre = Math.max(maxFre, termMap.get(word));
            }
        }
        int wordLen = terms.size();
        for (String word : termMap.keySet()) {
            double tf;
            if (standard) {
                tf = (double) termMap.get(word) / wordLen;
            } else {
                tf = (double) termMap.get(word) / maxFre;
            }
            TFMap.put(word, tf);
        }
    }
}
