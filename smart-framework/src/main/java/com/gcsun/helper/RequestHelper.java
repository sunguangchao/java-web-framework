package com.gcsun.helper;

import com.gcsun.bean.FormParam;
import com.gcsun.bean.Param;
import com.gcsun.util.ArrayUtil;
import com.gcsun.util.CodecUtil;
import com.gcsun.util.StreamUtil;
import com.gcsun.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by 11981 on 2017/9/17.
 */
public final class RequestHelper {
    /**
     * 创建请求对象
     */
    public static Param createParam(HttpServletRequest request) throws IOException{
        List<FormParam> formParamList = new ArrayList<FormParam>();
        //formParamList.addAll((request));
        formParamList.addAll(parseInputStream(request));
        return new Param(formParamList);
    }

    private static List<FormParam> parseParameterNames(HttpServletRequest request){
        List<FormParam> formParamList = new ArrayList<FormParam>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()){
            String fieldName = paramNames.nextElement();
            String[] fieldValues = request.getParameterValues(fieldName);
            if (ArrayUtil.isNotEmpty(fieldValues)){
                Object fieldValue;
                if (fieldValues.length == 1){
                    fieldValue = fieldValues[0];
                }else{
                    StringBuilder sb = new StringBuilder();
                    for (int i=0; i < fieldValues.length; i++){
                        sb.append(fieldValues[i]);
                        if (i != fieldValues.length-1){
                            sb.append(StringUtil.SEPARETOR);
                        }
                    }
                    fieldValue = sb.toString();
                }
                formParamList.add(new FormParam(fieldName, fieldValue));
            }

        }
        return formParamList;
    }

    private static List<FormParam> parseInputStream(HttpServletRequest request)throws IOException{
        List<FormParam> formParamList = new ArrayList<FormParam>();
        String body = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
        if (StringUtil.isNotEmpty(body)){
            String[] kvs = StringUtil.spiltString(body, "&");
            if (ArrayUtil.isNotEmpty(kvs)){
                for (String kv : kvs){
                    String[] array = StringUtil.spiltString(kv, "&");
                    if (ArrayUtil.isNotEmpty(array) && array.length == 2){
                        String fieldName = array[0];
                        String fieldValue = array[1];
                        formParamList.add(new FormParam(fieldName, fieldValue));
                    }
                }

            }
        }
        return formParamList;
    }
}
