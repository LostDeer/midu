package aduio.midu.utils;

import android.os.AsyncTask;

/**
 * Created by ${LostDeer} on 2017/11/21.
 * Github:https://github.com/LostDeer
 * 3个参数  5个方法
 * “启动任务执行的输入参数”、“后台任务执行的进度”、“后台计算结果的类型
 */
public class MyAsyncTask extends AsyncTask<Integer, Integer, String> {
    /**
     * 1.开始执行异步线程
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * 2.耗时操作,并返回结果
     */
    @Override
    protected String doInBackground(Integer... integers) {
        //3.动态进度 调用onProgressUpdate
//        publishProgress(integers);
        return null;
    }

    /**
     * 4.进度
     * @param values
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    /**
     * 5.结束返回结果
     * @param s
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
