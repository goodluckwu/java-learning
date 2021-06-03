package io.github.wuzhihao7.commonstext;


import org.apache.commons.text.StringSubstitutor;

public class StringSubstitutorDemo {
    public static void main(String[] args) {
        StringSubstitutor stringSubstitutor = new StringSubstitutor(System::getProperty);
        System.out.println(stringSubstitutor.replace("${user.home}"));
    }
}
