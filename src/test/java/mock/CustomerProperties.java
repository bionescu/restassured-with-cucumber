package mock;

import java.io.Serializable;

/**
 * Created by bionescu on 03.03.2018.
 */
public class CustomerProperties implements Serializable {

    Integer age;

    Boolean active;

    String date_of_birth;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }
}
