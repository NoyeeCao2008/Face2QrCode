package com.github.noyeecao2008.bean;

import java.util.List;

/**
 * face_token : fid
 * user_list : [{"group_id":"test1","user_id":"u333333","user_info":"Test User","score":99.3}]
 */
public class FaceSearchBean {
    private String face_token;
    private List<UserListBean> user_list;

    public String getFace_token() {
        return face_token;
    }

    public void setFace_token(String face_token) {
        this.face_token = face_token;
    }

    public List<UserListBean> getUser_list() {
        return user_list;
    }

    public void setUser_list(List<UserListBean> user_list) {
        this.user_list = user_list;
    }

    public static class UserListBean {
        /**
         * group_id : test1
         * user_id : u333333
         * user_info : Test User
         * score : 99.3
         */

        private String group_id;
        private String user_id;
        private String user_info;
        private double score;

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_info() {
            return user_info;
        }

        public void setUser_info(String user_info) {
            this.user_info = user_info;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        @Override
        public String toString() {
            return "UserListBean{" +
                    "group_id='" + group_id + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", user_info='" + user_info + '\'' +
                    ", score=" + score +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "FaceSearchBean{" +
                "face_token='" + face_token + '\'' +
                ", user_list=" + user_list +
                '}';
    }
}
