package mvp;

import android.os.AsyncTask;

import myinterface.IDataRequestListener;

public class CircleModel {


    public CircleModel() {
        //
    }

    public void deleteCircle(final IDataRequestListener listener) {
        requestServer(listener);
    }

    public void addFavort(final IDataRequestListener listener) {
        requestServer(listener);
    }

    public void deleteFavort(final IDataRequestListener listener) {
        requestServer(listener);
    }

    public void addComment(final IDataRequestListener listener) {
        requestServer(listener);
    }

    public void deleteComment(final IDataRequestListener listener) {
        requestServer(listener);
    }

    /**
     * @param listener 设定文件
     * @return void    返回类型
     * @Description: 与后台交互,
     */
    private void requestServer(final IDataRequestListener listener) {
        new AsyncTask<Object, Integer, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                //和后台交互
                return null;
            }

            protected void onPostExecute(Object result) {
                listener.loadSuccess(result);
            }

        }.execute();
    }

}
