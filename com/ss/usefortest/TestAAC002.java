package com.ss.usefortest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by juneday on 2016/11/24.
 */
public class TestAAC002 {


    public static void main(String agrs[]){
        String aae522 ="aac002+340703198001010537+340703198001010553";
        int end =aae522.indexOf(";");
        if (end>-1){
            aae522 =aae522.substring(0,end);
        }
        Map personMap= new HashMap();
        String[] dataArray = aae522.split("\\u002B"); // +
        if (dataArray[0].equals("aac003")) {  //关键新只可能变更其中的一项信息,所以只去第一个
            personMap.put("aac003", dataArray[2]);
        } else if (dataArray[0].equals("aac002")) {
            personMap.put("aac002", dataArray[2]);
//            String aac058 =otherDao.getAac058(personMap);
//            personMap.put("aac058", aac058);
                    /*if(ValidateUtil.areEqual(aac058,YHConstants.AAC058_01)) {
                        personMap.put("aac147", dataArray[2]);
                    }*/
        }
    }
}
