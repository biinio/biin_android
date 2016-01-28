package io.biin.biin.dal.models;

import java.util.List;

/**
 * Created by Ivan on 1/27/16.
 */
public class Biinnie  extends BiinRequest{
    public String _id;
    public String identifier;
    public String firstName;
    public String lastName;
    public String biinName;
    public String gender;
    public List<Category> categories;
    public String followers;
    public String following;
    public String url;
    public String email;
    public String birthDate;
    public String isEmailVerified;
}
