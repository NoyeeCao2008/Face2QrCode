package com.github.noyeecao2008.net;

public class RequestForFaceSearch extends RequestForFace {
    public RequestForFaceSearch(String imageFaceBase64) {
        /**
         * 参数	必选	类型	说明
         * image_type	是	string	图片类型
         * BASE64:图片的base64值，base64编码后的图片数据，编码后的图片大小不超过2M；
         * URL:图片的 URL地址( 可能由于网络等原因导致下载图片时间过长)；
         * FACE_TOKEN: 人脸图片的唯一标识，调用人脸检测接口时，会为每个人脸图片赋予一个唯一的FACE_TOKEN，同一张图片多次检测得到的FACE_TOKEN是同一个。
         */
        this.param("image_type", "BASE64");

        /**
         * 参数	必选	类型	说明
         * image	是	string	图片信息(总数据大小应小于10M)，图片上传方式根据image_type来判断
         */
        this.param("image", imageFaceBase64);

        /**
         * 参数	必选	类型	说明
         * group_id_list	是	string	从指定的group中进行查找 用逗号分隔，上限10个
         */
        this.param("group_id_list", getGroupId());

        /** 参数	必选	类型	说明
         * quality_control	否	string	图片质量控制
         * NONE: 不进行控制
         * LOW:较低的质量要求
         * NORMAL: 一般的质量要求
         * HIGH: 较高的质量要求
         * 默认 NONE
         * 若图片质量不满足要求，则返回结果中会提示质量检测失败
         *
         * 参数	必选	类型	说明
         * liveness_control	否	string	活体检测控制
         * NONE: 不进行控制
         * LOW:较低的活体要求(高通过率 低攻击拒绝率)
         * NORMAL: 一般的活体要求(平衡的攻击拒绝率, 通过率)
         * HIGH: 较高的活体要求(高攻击拒绝率 低通过率)
         * 默认NONE
         * 若活体检测结果不满足要求，则返回结果中会提示活体检测失败
         *
         * 参数	必选	类型	说明
         * user_id	否	string	当需要对特定用户进行比对时，指定user_id进行比对。即人脸认证功能。
         *
         * 参数	必选	类型	说明
         * max_user_num	否	unit32	查找后返回的用户数量。返回相似度最高的几个用户，默认为1，最多返回50个。
         *
         * 参数	必选	类型	说明
         * face_sort_type	否	int	人脸检测排序类型
         * 0:代表检测出的人脸按照人脸面积从大到小排列
         * 1:代表检测出的人脸按照距离图片中心从近到远排列
         * 默认为0
         *
         * 参数	必选	类型	说明
         * match_threshold	否	int	匹配阈值（设置阈值后，score低于此阈值的用户信息将不会返回） 最大100 最小0 默认0
         * 此阈值设置得越高，检索速度将会越快，推荐使用默认阈值80
         * 说明：如果使用base 64格式的图片，两张请求的图片请分别进行base64编码。
         */
    }

}
