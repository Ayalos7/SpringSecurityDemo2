package com.jb.mySpringSecurity.JWT;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserModel {
    private String userName;
    private String password;
}
