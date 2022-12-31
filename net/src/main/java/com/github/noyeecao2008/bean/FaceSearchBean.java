package com.github.noyeecao2008.bean;

import java.util.List;

public class FaceSearchBean {

    /**
     * error_code : 0
     * error_msg : SUCCESS
     * log_id : 2992569851
     * timestamp : 1672498192
     * cached : 0
     * result : {"face_token":"4c2cd902de5dfc3cfbc6dbf692cec33a","user_list":[{"group_id":"F2QR_ttt","user_id":"ny","user_info":"","score":12.209049224854}]}
     */

    private int error_code;
    private String error_msg;
    private long log_id;
    private int timestamp;
    private int cached;
    private ResultBean result;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getCached() {
        return cached;
    }

    public void setCached(int cached) {
        this.cached = cached;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * face_token : 4c2cd902de5dfc3cfbc6dbf692cec33a
         * user_list : [{"group_id":"F2QR_ttt","user_id":"ny","user_info":"","score":12.209049224854}]
         */

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
             * group_id : F2QR_ttt
             * user_id : ny
             * user_info :
             * score : 12.209049224854
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
            return "ResultBean{" +
                    "face_token='" + face_token + '\'' +
                    ", user_list=" + user_list +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "FaceSearchBean{" +
                "error_code=" + error_code +
                ", error_msg='" + error_msg + '\'' +
                ", log_id=" + log_id +
                ", timestamp=" + timestamp +
                ", cached=" + cached +
                ", result=" + result +
                '}';
    }
}
