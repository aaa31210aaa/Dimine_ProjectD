package mvp;


import entity.User;
import myinterface.ICircleViewUpdate;
import myinterface.IDataRequestListener;

/**
 * 通知model请求服务器和通知view更新
 */
public class CirclePresenter {
    private CircleModel mCircleModel;
    private ICircleViewUpdate mCircleView;

    public CirclePresenter(ICircleViewUpdate view) {
        this.mCircleView = view;
        mCircleModel = new CircleModel();
    }

    /**
     * @param circleId
     * @return void    返回类型
     * @throws
     * @Title: deleteCircle
     * @Description: 删除动态
     */
    public void deleteCircle(final String circleId) {
        mCircleModel.deleteCircle(new IDataRequestListener() {

            @Override
            public void loadSuccess(Object object) {
                mCircleView.update2DeleteCircle(circleId);
            }
        });
    }

    /**
     * @param circlePosition
     * @return void    返回类型
     * @throws
     * @Title: addFavort
     * @Description: 点赞
     */
//    public void addFavort(final int circlePosition) {
//        mCircleModel.addFavort(new IDataRequestListener() {
//
//            @Override
//            public void loadSuccess(Object object) {
//                mCircleView.update2AddFavorite(circlePosition);
//            }
//        });
//    }

    /**
     * @param @param circlePosition
     * @param @param favortId
     * @return void    返回类型
     * @throws
     * @Title: deleteFavort
     * @Description: 取消点赞
     */
//    public void deleteFavort(final int circlePosition, final String favortId) {
//        mCircleModel.deleteFavort(new IDataRequestListener() {
//
//            @Override
//            public void loadSuccess(Object object) {
//                mCircleView.update2DeleteFavort(circlePosition, favortId);
//            }
//        });
//    }

    /**
     * @param circlePosition
     * @param type           0：发布评论  1：回复评论
     * @param replyUser      回复评论时对谁的回复
     * @return void    返回类型
     * @throws
     * @Title: addComment
     * @Description: 增加评论
     */
    public void addComment(final int circlePosition, final int type, final User replyUser) {
        mCircleModel.addComment(new IDataRequestListener() {

            @Override
            public void loadSuccess(Object object) {
                mCircleView.update2AddComment(circlePosition, type, replyUser);
            }

        });
    }

    /**
     * @param @param circlePosition
     * @param @param commentId
     * @return void    返回类型
     * @throws
     * @Title: deleteComment
     * @Description: 删除评论
     */
    public void deleteComment(final int circlePosition, final String commentId) {
        mCircleModel.addComment(new IDataRequestListener() {

            @Override
            public void loadSuccess(Object object) {
                mCircleView.update2DeleteComment(circlePosition, commentId);
            }

        });
    }
}
