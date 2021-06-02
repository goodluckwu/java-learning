package io.github.wuzhihao7.commonstext;


import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.lookup.StringLookup;

public class StringSubstitutorDemo {
    public static void main(String[] args) {
        StringSubstitutor stringSubstitutor = new StringSubstitutor(new StringLookup() {
            @Override
            public String lookup(String key) {
                return System.getProperty(key);
            }
        });
        System.out.println(stringSubstitutor.replace("${user.home}"));
    }
}
