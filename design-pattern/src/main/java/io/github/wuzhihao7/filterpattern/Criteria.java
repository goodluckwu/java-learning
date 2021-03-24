package io.github.wuzhihao7.filterpattern;

import java.util.List;

public interface Criteria {
    List<Person> meetCriteria(List<Person> persons);
}
