package com.foodspider.model.response_model;

import com.foodspider.model.narrowed_model.NarrowedUser;

public class LoginResponse extends ResponseBase {
    public NarrowedUser userBase;
    public Boolean isAdmin;
}
