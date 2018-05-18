package com.ikuboo.tools.limiter;


import com.ikuboo.tools.limiter.exception.InitScriptExcetion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Load Script
 * @author yuanchunsen@jd.com
 * 2018/5/18.
 */
public class Script {

    private static final String script;

    //加载脚本
    static {
        StringBuilder sb = new StringBuilder();
        InputStream stream = Script.class.getClassLoader().getResourceAsStream("limit.lua");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        try {
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str).append(System.lineSeparator());
            }
            stream.close();
            br.close();

        } catch (IOException e) {
            throw new InitScriptExcetion("limit.lua init error",e);
        }
        script = sb.toString();
    }

    public static String getScript() {
        return script;
    }
}
