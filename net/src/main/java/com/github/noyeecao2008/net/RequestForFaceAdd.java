package com.github.noyeecao2008.net;

public class RequestForFaceAdd extends RequestForFace {

    public  RequestForFaceAdd(String userId, String imageFace) {
        super();
        /**
         * 参数	必选	类型	说明
         * image_type	是	string	图片类型
         * BASE64:图片的base64值，base64编码后的图片数据，编码后的图片大小不超过2M；
         * URL:图片的 URL地址( 可能由于网络等原因导致下载图片时间过长)；
         * FACE_TOKEN：人脸图片的唯一标识，调用人脸检测接口时，会为每个人脸图片赋予一个唯一的FACE_TOKEN，同一张图片多次检测得到的FACE_TOKEN是同一个。
         */
        this.param("image_type", "BASE64");


        /**
         * 参数	必选	类型	说明
         * quality_control	否	string	图片质量控制
         * NONE: 不进行控制
         * LOW:较低的质量要求
         * NORMAL: 一般的质量要求
         * HIGH: 较高的质量要求
         * 默认 NONE
         * 若图片质量不满足要求，则返回结果中会提示质量检测失败
         */
        this.param("quality_control", "LOW");

        /**
         * 参数	必选	类型	说明
         * liveness_control	否	string	活体检测控制
         * NONE: 不进行控制
         * LOW:较低的活体要求(高通过率 低攻击拒绝率)
         * NORMAL: 一般的活体要求(平衡的攻击拒绝率, 通过率)
         * HIGH: 较高的活体要求(高攻击拒绝率 低通过率)
         * 默认NONE
         * 若活体检测结果不满足要求，则返回结果中会提示活体检测失败
         */
        this.param("liveness_control", "NONE");

        /**
         * 参数	必选	类型	说明
         * action_type	否	string	操作方式
         * APPEND: 当user_id在库中已经存在时，对此user_id重复注册时，新注册的图片默认会追加到该user_id下
         * REPLACE : 当对此user_id重复注册时,则会用新图替换库中该user_id下所有图片
         * 默认使用APPEND
         */
        this.param("action_type", "REPLACE");

        /**
         * 参数	必选	类型	说明
         * face_sort_type	否	int	人脸检测排序类型
         * 0:代表检测出的人脸按照人脸面积从大到小排列
         * 1:代表检测出的人脸按照距离图片中心从近到远排列
         * 默认为0
         */
        this.param("face_sort_type", 0);

        /**
         * 参数	必选	类型	说明
         * user_id	是	string	用户id（由数字、字母、下划线组成），长度限制128B
         * user_info	否	string	用户资料，长度限制256B 默认空
         */
        this.param("user_id", userId).param("user_info", "");

        /**
         * 参数	必选	类型	说明
         * group_id	是	string	用户组id，标识一组用户（由数字、字母、下划线组成），长度限制48B。产品建议：根据您的业务需求，可以将需要注册的用户，按照业务划分，分配到不同的group下，例如按照会员手机尾号作为groupid，用于刷脸支付、会员计费消费等，这样可以尽可能控制每个group下的用户数与人脸数，提升检索的准确率
         *
         * @return
         */
        this.param("group_id", "F2QR_" + getDeviceId());

        /**
         * 参数	必选	类型	说明
         * image	是	string	图片信息(总数据大小应小于10M)，图片上传方式根据image_type来判断。
         * 注：组内每个uid下的人脸图片数目上限为20张
         *
         * @param image
         * @return
         */
        this.param("image", imageFace);
    }

    private RequestForFaceAdd() {

    }
}
