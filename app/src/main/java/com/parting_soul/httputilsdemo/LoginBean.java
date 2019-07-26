package com.parting_soul.httputilsdemo;

/**
 * @author parting_soul
 * @date 2019/4/22
 */
public class LoginBean {
    /**
     * code : 200
     * message : 操作成功
     * data : {"session":"96dca22b408c4cd49c887b33093d4a40","isNewUser":false,"user":{"sex":0,"nickname":"用户45535","id":52,"avatar":null,"username":"133****5535"}}
     */

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * session : 96dca22b408c4cd49c887b33093d4a40
         * isNewUser : false
         * user : {"sex":0,"nickname":"用户45535","id":52,"avatar":null,"username":"133****5535"}
         */

        private String session;
        private boolean isNewUser;
        private UserBean user;

        public String getSession() {
            return session;
        }

        public void setSession(String session) {
            this.session = session;
        }

        public boolean isIsNewUser() {
            return isNewUser;
        }

        public void setIsNewUser(boolean isNewUser) {
            this.isNewUser = isNewUser;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * sex : 0
             * nickname : 用户45535
             * id : 52
             * avatar : null
             * username : 133****5535
             */

            private int sex;
            private String nickname;
            private int id;
            private Object avatar;
            private String username;

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public Object getAvatar() {
                return avatar;
            }

            public void setAvatar(Object avatar) {
                this.avatar = avatar;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }
        }
    }
}
