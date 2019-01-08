package utils;

/**
 * 网络连接工具类
 */
public class MyHttpUtils {
    //内网端口IP地址
//    public static String host = "http://192.168.5.228:9697/DMMES/mobile/";

    //public static String host = "http://192.168.5.112:8188/DMMES/mobile/";
    //public static String host = "http://192.168.188.158:8188/DMMES/mobile/";

    //外网端口IP地址
//    public static String host = "http://192.168.0.13:8094/DMMES/mobile/";
    public static String host = "http://218.76.35.118:8888/dmmes/mobile/";

    //办理
    public static String ApiBl() {
        //内网端口IP地址
//        return "http://192.168.5.228:9697/DMMES/";

//          return "http://192.168.5.112:8188/DMMES/";
//        return "http://192.168.188.158:8188/DMMES/";

        //外网端口IP地址
        return "http://218.76.35.118:8888/dmmes/";
    }

    //登陆
    public static String ApiLogin() {
        return host + "login.action";
    }

    //待办事宜
    public static String ApiDbsy() {
        return host + "task/taskList.action";
    }

    //通知公告
    public static String ApiNotification() {
        return host + "message/message.action";
    }


    //周计划月计划外部布局
    public static String ApiPlanWeeklyMonth() {
        return host + "month/getGongXuByLoginUser.action";
    }


    //月计划详情
    public static String ApiPlanMonthlyDetail() {
        return host + "month/monthplan.action";
    }

    //周计划详情
    public static String ApiPlanWeeklyDetail() {
        return host + "week/weekplan.action";
    }

    //生产界面接口
    public static String ApiProduction() {
        return host + "productionbill/getMenu.action";
    }

    public static String ApiProductionByGx() {
        return host + "productionbill/getTargetByGx.action";
    }

    //班组接口
    public static String ApiProductionByUser() {
        return host + "productionbill/getTeamByLoginUser.action";
    }

    //班次接口
    public static String ApiProductionByBc() {
        return host + "productionbill/getClasses.action";
    }

    //通过班组获取设备接口
    public static String ApiProductionDeviceByTeam() {
        return host + "productionbill/getDeviceByTeam.action";
    }

    //通过工序查找未分配的作业地点
    public static String ApiSelectProjectByGx() {
        return host + "productionbill/selectProjectByGx.action";
    }

    //通过工序和作业地点查找工艺信息
    public static String ApiSelectGyByGxAndProject() {
        return host + "productionbill/selectGyByGxAndProject.action";
    }

    public static String ApiPassWordChange() {
        return host + "password/change.action";
    }

    //新增台账
    public static String ApiAddProductionBill() {
        return host + "productionbill/addProductionBill2.action";
    }

    //历史台账查询
    public static String ApiDisplayTargetByGx() {
        return host + "productionbill/getAppDisplayTargetByGx.action";
    }


    //预警告警
    public static String ApiAlarmList() {
        return host + "alarm/list.action";
    }

    //待办事宜签收
    public static String ApiClaim() {
        return host + "task/claim.action";
    }

    //日报
    public static String ApiDayReport() {
        return host + "productdayrpt/getGongYiByLoginUser.action";
    }

    //首页日报图表详情
    public static String ApiDataByRpt() {
        return host + "productdayrpt/getMonthDataByRpt.action";
    }

    //周报
    public static String ApiWeekrpt() {
        return host + "productweekrpt/getGongYiByLoginUse.action";
    }

    //周报点进去后的当月数据
    public static String ApiMonthWeekByRpt() {
        return host + "productweekrpt/getMonthWeekByRpt.action";
    }

    //评论列表接口
    public static String ApiCommentList() {
        return host + "comment/list.action";
    }

    //添加评论接口
    public static String ApiAddComment() {
        return host + "comment/addComment.action";
    }

    //通过作业地点，工艺，工序给指标赋值
    public static String ApiGetTargetValue() {
        return host + "productionbill/getTargetValues.action";
    }

}
